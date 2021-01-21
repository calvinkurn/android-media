package com.tokopedia.play.view.activity

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayNewChannelInteractor
import com.tokopedia.play.view.contract.PlayPiPCoordinator
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.viewcomponent.SwipeContainerViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play_common.util.PlayVideoPlayerObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalContent#PLAY_DETAIL}
 */
class PlayActivity : BaseActivity(), PlayNewChannelInteractor, PlayNavigation, PlayPiPCoordinator, SwipeContainerViewComponent.DataSource {

    @Inject
    lateinit var playLifecycleObserver: PlayVideoPlayerObserver

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var pageMonitoring: PlayPltPerformanceCallback

    @Inject
    lateinit var playParentViewModelFactory: PlayParentViewModel.Factory

    private lateinit var viewModel: PlayParentViewModel

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val swipeContainerView by viewComponent(isEagerInit = true) {
        SwipeContainerViewComponent(
                container = it,
                rootId = R.id.vp_fragment,
                fragmentManager = supportFragmentManager,
                lifecycle = lifecycle,
                dataSource = this,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        val channelId = intent?.data?.lastPathSegment
        intent.putExtra(PLAY_KEY_CHANNEL_ID, channelId)

        setupViewModel(channelId)
        setupPage()
        setupObserve()

//        setupView(channelId)
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fragment = supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG)
        val channelId = intent?.data?.lastPathSegment
        if (fragment != null && fragment is PlayFragment && channelId != null) {
            fragment.onNewChannelId(channelId)
        }
    }

    override fun onNewChannel(channelId: String?) {
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.fl_fragment, getFragment(channelId.orEmpty()), PLAY_FRAGMENT_TAG)
//                .commit()
    }

    override fun onEnterPiPMode() {
        lifecycle.removeObserver(playLifecycleObserver)
        onBackPressed(isSystemBack = false)
    }

    /**
     * Swipe Container View Component
     */
    override fun getFragment(channelId: String): Fragment {
        val fragmentFactory = supportFragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, PlayFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    private fun inject() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(this))
                .build()
                .inject(this)
    }

//    private fun getFragment(channelId: String?): Fragment {
//        return getPlayFragment(channelId)
//    }

    private fun setupViewModel(channelId: String?) {
        val viewModelFactory = object : AbstractSavedStateViewModelFactory(this, intent?.extras ?: Bundle()) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                return playParentViewModelFactory.create(handle) as T
            }
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayParentViewModel::class.java)

    }

    private fun setupPage() {
        lifecycle.addObserver(playLifecycleObserver)
    }

    private fun setupObserve() {
        observeChannelList()
    }

    private fun observeChannelList() {
        viewModel.observableChannelIdList.observe(this, Observer {
            swipeContainerView.setChannelIds(it)
        })
    }

    private fun setupView(channelId: String?) {
        if (supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG) == null) {
            onNewChannel(channelId)
        }
    }

    private fun getPlayFragment(channelId: String?): Fragment {
        val fragmentFactory = supportFragmentManager.fragmentFactory
        return fragmentFactory.instantiate(classLoader, PlayFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    override fun onBackPressed(isSystemBack: Boolean) {
        val fragment = supportFragmentManager.findFragmentByTag(PLAY_FRAGMENT_TAG)
        if (fragment != null && fragment is PlayFragment) {
            if (!fragment.onBackPressed()) {
                if (isSystemBack && orientation.isLandscape) fragment.onOrientationChanged(ScreenOrientation.Portrait, false)
                else {
                    if (isTaskRoot) {
                        val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
                        startActivity(intent)
                        finish()
                    } else {
                        fragment.setResultBeforeFinish()
                        supportFinishAfterTransition()
                    }
                }
            }
        } else super.onBackPressed()
    }

    override fun onBackPressed() {
        onBackPressed(true)
    }

    fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageMonitoring.getPltPerformanceData()
    }

    private fun startPageMonitoring() {
        pageMonitoring.startPlayMonitoring()
        pageMonitoring.startPreparePagePerformanceMonitoring()
    }

    companion object {
        private const val PLAY_FRAGMENT_TAG = "FRAGMENT_PLAY"
    }
}