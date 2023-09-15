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
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.content.common.util.Router
import com.tokopedia.floatingwindow.FloatingWindowAdapter
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.PLAY_KEY_CHANNEL_RECOMMENDATION
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.cast.PlayCastNotificationAction
import com.tokopedia.play.view.storage.PlayQueryParamStorage
import com.tokopedia.play.databinding.ActivityPlayBinding
import com.tokopedia.play.di.PlayInjector
import com.tokopedia.play.util.PlayCastHelper
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.view.contract.PlayFullscreenManager
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.contract.PlayPiPCoordinator
import com.tokopedia.play.view.fragment.PlayFragment
import com.tokopedia.play.view.fragment.PlayVideoFragment
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.type.ScreenOrientation2
import com.tokopedia.play.view.type.isCompact
import com.tokopedia.play.view.viewcomponent.FragmentErrorViewComponent
import com.tokopedia.play.view.viewcomponent.FragmentUpcomingViewComponent
import com.tokopedia.play.view.viewcomponent.LoadingViewComponent
import com.tokopedia.play.view.viewcomponent.SwipeCoachMarkViewComponent
import com.tokopedia.play.view.viewcomponent.SwipeContainerViewComponent
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play_common.lifecycle.lifecycleBound
import com.tokopedia.play_common.model.result.PageResultState
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.viewcomponent.viewComponent
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

/**
 * Created by jegul on 29/11/19
 * Applink: [com.tokopedia.applink.internal.ApplinkConstInternalContent.PLAY_DETAIL], [com.tokopedia.play.PLAY_APP_LINK], [com.tokopedia.play.PLAY_RECOMMENDATION_APP_LINK]
 * Query parameters:
 * - source_type: String
 * - source_id: String
 *
 * Example: tokopedia://play/12345?source_type=SHOP&source_id=123
 */
@Suppress("LateinitUsage")
class PlayActivity :
    BaseActivity(),
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

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var analytic: PlayAnalytic

    @Inject
    lateinit var queryParamStorage: PlayQueryParamStorage

    private lateinit var binding: ActivityPlayBinding

    private lateinit var viewModel: PlayParentViewModel

    private val pipAdapter: FloatingWindowAdapter by lifecycleBound(
        creator = { FloatingWindowAdapter(this) }
    )

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    private val orientation: ScreenOrientation2
        get() = ScreenOrientation2.get(this)

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
    private val swipeCoachMarkView by viewComponent {
        SwipeCoachMarkViewComponent(it)
    }
    private val ivLoading by viewComponent { LoadingViewComponent(it, R.id.iv_loading) }
    private val fragmentErrorView by viewComponent {
        FragmentErrorViewComponent(startChannelId, it, R.id.fl_global_error, supportFragmentManager)
    }

    private val fragmentUpcomingView by viewComponent {
        FragmentUpcomingViewComponent(it, R.id.fcv_upcoming, supportFragmentManager)
    }

    private var mIsExpectingOrientationChange = false

    /**
     * Applink
     */
    private val startChannelId: String
        get() {
            return if (lastPathSegment == PLAY_KEY_CHANNEL_RECOMMENDATION) "0" else intent?.data?.lastPathSegment.orEmpty()
        }

    private val lastPathSegment: String
        get() {
            return intent?.data?.lastPathSegment.orEmpty()
        }

    val activeFragment: PlayFragment?
        get() = try { swipeContainerView.getActiveFragment() as? PlayFragment } catch (e: Throwable) { null }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        supportFragmentManager.fragmentFactory = fragmentFactory

        startPageMonitoring()
        super.onCreate(savedInstanceState)

        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PlayCastHelper.getCastContext(this)

        removePip()

        setupIntentExtra()

        onExitFullscreen()

        setupViewModel()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        PlayCastNotificationAction.showRedirectButton(applicationContext, false)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        PlayCastNotificationAction.showRedirectButton(applicationContext, true)
    }

    override fun onDestroy() {
        super.onDestroy()
        mIsExpectingOrientationChange = false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val newBundle = intent.extras ?: Bundle()

        val lastSegment = intent.data?.lastPathSegment.orEmpty()
        val newSegment = if (lastSegment == PLAY_KEY_CHANNEL_RECOMMENDATION) "0" else lastSegment

        newBundle.putString(PLAY_KEY_CHANNEL_ID, newSegment)
        viewModel.setNewChannelParams(newBundle)
    }

    override fun onEnterPiPMode() {
        onBackPressed(isSystemBack = false)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        swipeContainerView.setEnableSwiping(!orientation.isLandscape || !orientation.isCompact)
        swipeContainerView.refocusFragment()

        if (orientation.isLandscape && orientation.isCompact && !mIsExpectingOrientationChange) {
            activeFragment?.sendTrackerWhenRotateFullScreen()
        }

        if (mIsExpectingOrientationChange) mIsExpectingOrientationChange = false
    }

    override fun changeOrientation(screenOrientation: ScreenOrientation, isTilting: Boolean) {
        if (requestedOrientation != screenOrientation.requestedOrientation && !onInterceptOrientationChangedEvent(screenOrientation)) {
            mIsExpectingOrientationChange = true
            requestedOrientation = screenOrientation.requestedOrientation
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

    override fun onPageSelected(position: Int) {
        queryParamStorage.pageSelected = position

        activeFragment?.setFragmentActive(position)
        swipeCoachMarkView.hideAnimated()
    }

    override fun onShouldLoadNextPage() {
        viewModel.loadNextPage()
    }

    private fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        if (swipeContainerView.scrollState != ViewPager2.SCROLL_STATE_IDLE) return true

        return activeFragment?.onInterceptOrientationChangedEvent(newOrientation) ?: true
    }

    private fun inject() {
        PlayInjector.get(this)
            .inject(this)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this, getViewModelFactory()).get(PlayParentViewModel::class.java)
    }

    private fun setupObserve() {
        observeChannelList()
        observeFirstChannelEvent()
    }

    @OptIn(ExperimentalTime::class)
    private fun observeChannelList() {
        viewModel.observableChannelIdsResult.observe(this) {
            when (val state = it.state) {
                PageResultState.Loading -> {
                    fragmentErrorViewOnStateChanged(shouldShow = false)
                    if (it.currentValue.isEmpty()) ivLoading.show() else ivLoading.hide()
                }
                is PageResultState.Fail -> {
                    pageMonitoring.invalidate()
                    ivLoading.hide()
                    if (it.currentValue.isEmpty()) fragmentErrorViewOnStateChanged(shouldShow = true)
                }
                is PageResultState.Success -> run {
                    pageMonitoring.startRenderPerformanceMonitoring()
                    ivLoading.hide()
                    fragmentErrorViewOnStateChanged(shouldShow = false)

                    if (!state.isFirstPage || it.currentValue.isEmpty()) return@run
                    val firstChannel = it.currentValue.first()

                    analytic.openScreen(
                        firstChannel.id,
                        firstChannel.channelDetail.channelInfo.channelType
                    )
                    lifecycleScope.launchWhenResumed {
                        delay(COACHMARK_START_DELAY_IN_SEC.toDuration(DurationUnit.SECONDS))
                        swipeCoachMarkView.showAnimated()
                    }
                }
                is PageResultState.Upcoming -> {
                    ivLoading.hide()
                    fragmentUpcomingView.safeInit((it.state as PageResultState.Upcoming).channelId)
                }
                is PageResultState.Archived -> {
                    pageMonitoring.invalidate()
                    ivLoading.hide()
                    fragmentErrorViewOnStateChanged(shouldShow = true)
                }
            }

            if (it.state is PageResultState.Success) {
                fragmentUpcomingView.safeRelease()
                swipeContainerView.setChannelIds(it.currentValue.map { channel -> channel.id })
            }
        }
    }

    private fun observeFirstChannelEvent() {
        viewModel.observableFirstChannelEvent.observe(
            this,
            EventObserver {
                swipeContainerView.reset()
            }
        )
    }

    override fun onBackPressed(isSystemBack: Boolean) {
        val fragment = activeFragment
        if (fragment is PlayFragment) {
            if (!fragment.onBackPressed()) {
                if (isSystemBack &&
                    orientation.isLandscape &&
                    orientation.isCompact
                ) {
                    changeOrientation(ScreenOrientation.Portrait, false)
                } else {
                    if (isTaskRoot) {
                        gotoHome()
                    } else {
                        fragment.setResultBeforeFinish()
                        supportFinishAfterTransition()
                    }
                }
            }
        } else {
            if (isTaskRoot) {
                gotoHome()
            } else {
                fragmentUpcomingView.getActiveFragment()?.setResultBeforeFinish()
                fragmentErrorView.activeFragment?.onBackPressed(startChannelId)
                supportFinishAfterTransition()
            }
        }
    }

    private fun gotoHome() {
        val intent = router.getIntent(this, ApplinkConst.HOME)
        startActivity(intent)
        finish()
    }

    override fun requestEnableNavigation() {
        swipeContainerView.setEnableSwiping(
            !orientation.isLandscape || !orientation.isCompact
        )
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
        return swipeContainerView.hasNextPage() && orientation.isPortrait
    }

    fun getViewModelFactory(): ViewModelProvider.Factory {
        return object : AbstractSavedStateViewModelFactory(this, intent?.extras ?: Bundle()) {
            override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
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

        private const val COACHMARK_START_DELAY_IN_SEC = 1
    }
}
