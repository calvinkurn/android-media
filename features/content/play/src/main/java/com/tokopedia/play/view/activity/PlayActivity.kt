package com.tokopedia.play.view.activity

import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.PlaySensorOrientationManager
import com.tokopedia.play.view.contract.PlayFullscreenManager
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.contract.PlayPiPCoordinator
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.viewcomponent.FragmentErrorViewComponent
import com.tokopedia.play.view.viewcomponent.LoadingViewComponent
import com.tokopedia.play.view.viewcomponent.SwipeContainerViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 * Applink: [com.tokopedia.applink.internal.ApplinkConstInternalContent.PLAY_DETAIL], [com.tokopedia.play.PLAY_APP_LINK]
 * Query parameters:
 * - source_type: String
 * - source_id: String
 *
 * Example: tokopedia://play/12345?source_type=SHOP&source_id=123
 */
class PlayActivity : BaseActivity(),
        PlayNavigation,
        PlayPiPCoordinator,
        SwipeContainerViewComponent.DataSource,
        SwipeContainerViewComponent.Listener,
        PlayOrientationListener,
        PlayFullscreenManager {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var pageMonitoring: PlayPltPerformanceCallback

    @Inject
    lateinit var playParentViewModelFactory: PlayParentViewModel.Factory

    @Inject
    lateinit var playPreference: PlayPreference

    private lateinit var orientationManager: PlaySensorOrientationManager

    private lateinit var viewModel: PlayParentViewModel

    private val pipAdapter: FloatingWindowAdapter by lifecycleBound(
            creator = { FloatingWindowAdapter(this) }
    )

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val swipeContainerView by viewComponent(isEagerInit = true) {
        SwipeContainerViewComponent(
                container = it,
                rootId = R.id.vp_fragment,
                fragmentManager = supportFragmentManager,
                lifecycle = lifecycle,
                dataSource = this,
                listener = this
        )
    }
    private val ivLoading by viewComponent { LoadingViewComponent(it, R.id.iv_loading) }
    private val fragmentErrorView by viewComponent {
        FragmentErrorViewComponent(startChannelId, it, R.id.fl_global_error, supportFragmentManager)
    }

    /**
     * Applink
     */
    private val startChannelId: String
        get() = intent?.data?.lastPathSegment.orEmpty()

    val activeFragment: PlayFragment?
        get() = try { swipeContainerView.getActiveFragment() as? PlayFragment } catch (e: Throwable) { null }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        removePip()

        setupIntentExtra()

        onExitFullscreen()

        setupViewModel()
        setupPage()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        orientationManager.enable()
        volumeControlStream = AudioManager.STREAM_MUSIC
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        orientationManager.disable()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newBundle = intent.extras

        if (newBundle != null) {
            newBundle.putString(PLAY_KEY_CHANNEL_ID, intent.data?.lastPathSegment.orEmpty())
            viewModel.setNewChannelParams(newBundle)
        }
    }

    override fun onEnterPiPMode() {
        onBackPressed(isSystemBack = false)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        swipeContainerView.setEnableSwiping(!ScreenOrientation.getByInt(newConfig.orientation).isLandscape)
        swipeContainerView.refocusFragment()
    }

    override fun onOrientationChanged(screenOrientation: ScreenOrientation, isTilting: Boolean) {
        if (requestedOrientation != screenOrientation.requestedOrientation && !onInterceptOrientationChangedEvent(screenOrientation)) {
            requestedOrientation = screenOrientation.requestedOrientation

            if (screenOrientation.isLandscape && isTilting) activeFragment?.sendTrackerWhenRotateFullScreen()
        }
    }

    override fun onEnterFullscreen() {
        systemUiVisibility = PlayFullScreenHelper.getHideSystemUiVisibility()
    }

    override fun onExitFullscreen() {
        systemUiVisibility = PlayFullScreenHelper.getShowSystemUiVisibility()
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

    override fun onShouldLoadNextPage() {
        viewModel.loadNextPage()
    }

    override fun onSwipeNextPage() {
        playPreference.setOnboardingShown(viewModel.userId)
    }

    private fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        if (swipeContainerView.scrollState != ViewPager2.SCROLL_STATE_IDLE) return true

        return activeFragment?.onInterceptOrientationChangedEvent(newOrientation) ?: true
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

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, getViewModelFactory()).get(PlayParentViewModel::class.java)
    }

    private fun setupPage() {
        setupOrientation()
    }

    private fun setupOrientation() {
        orientationManager = PlaySensorOrientationManager(this, this)
    }

    private fun setupObserve() {
        observeChannelList()
        observeFirstChannelEvent()
    }

    private fun observeChannelList() {
        viewModel.observableChannelIdsResult.observe(this, Observer {
            when (it.state) {
                PageResultState.Loading -> {
                    fragmentErrorViewOnStateChanged(shouldShow = false)
                    if (it.currentValue.isEmpty()) ivLoading.show() else ivLoading.hide()
                }
                is PageResultState.Fail -> {
                    pageMonitoring.invalidate()
                    ivLoading.hide()
                    if (it.currentValue.isEmpty()) fragmentErrorViewOnStateChanged(shouldShow = true)
                }
                is PageResultState.Success -> {
                    pageMonitoring.startRenderPerformanceMonitoring()
                    ivLoading.hide()
                    fragmentErrorViewOnStateChanged(shouldShow = false)
                }
            }
            swipeContainerView.setChannelIds(it.currentValue)
        })
    }

    private fun observeFirstChannelEvent() {
        viewModel.observableFirstChannelEvent.observe(this, EventObserver {
            swipeContainerView.reset()
        })
    }

    override fun onBackPressed(isSystemBack: Boolean) {
        val fragment = activeFragment
        if (fragment is PlayFragment) {
            if (!fragment.onBackPressed()) {
                if (isSystemBack && orientation.isLandscape) onOrientationChanged(ScreenOrientation.Portrait, false)
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

    override fun requestEnableNavigation() {
        swipeContainerView.setEnableSwiping(!orientation.isLandscape)
    }

    override fun requestDisableNavigation() {
        swipeContainerView.setEnableSwiping(false)
    }

    override fun onBackPressed() {
        onBackPressed(true)
    }

    override fun navigateToNextPage() {
        swipeContainerView.scrollTo(SwipeContainerViewComponent.ScrollDirection.Next, isSmoothScroll = true)
    }

    override fun canNavigateNextPage(): Boolean {
        return swipeContainerView.hasNextPage()
    }

    fun getViewModelFactory(): ViewModelProvider.Factory {
        return object : AbstractSavedStateViewModelFactory(this, intent?.extras ?: Bundle()) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                return playParentViewModelFactory.create(handle) as T
            }
        }
    }

    fun getPerformanceMonitoring(): PlayPltPerformanceCallback = pageMonitoring

    private fun startPageMonitoring() {
        pageMonitoring.startPlayMonitoring()
        pageMonitoring.startPreparePagePerformanceMonitoring()
    }

    private fun fragmentErrorViewOnStateChanged(shouldShow: Boolean) {
        if (shouldShow) {
            fragmentErrorView.safeInit()
            fragmentErrorView.show()
        } else {
            fragmentErrorView.hide()
        }
    }

    private fun setupIntentExtra() {
        intent.putExtra(PLAY_KEY_CHANNEL_ID, startChannelId)
    }

    private fun removePip() {
        pipAdapter.removeByKey(PlayVideoFragment.FLOATING_WINDOW_KEY)
    }

    companion object {
        private const val PLAY_FRAGMENT_TAG = "FRAGMENT_PLAY"
    }
}