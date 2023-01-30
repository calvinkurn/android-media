package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.broadcaster.revamp.Broadcaster
import com.tokopedia.broadcaster.revamp.state.BroadcastInitState
import com.tokopedia.broadcaster.revamp.util.statistic.BroadcasterMetric
import com.tokopedia.broadcaster.revamp.util.view.AspectFrameLayout
import com.tokopedia.config.GlobalConfig
import com.tokopedia.content.common.types.ContentCommonUserType.KEY_AUTHOR_TYPE
import com.tokopedia.content.common.types.ContentCommonUserType.TYPE_UNKNOWN
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.di.DaggerActivityRetainedComponent
import com.tokopedia.play.broadcaster.pusher.PlayBroadcaster
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.view.PlayLivePusherDebugView
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.util.delegate.retainedComponent
import com.tokopedia.play.broadcaster.util.extension.channelNotFound
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.util.idling.PlayBroadcasterIdlingResource
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.contract.PlayBaseCoordinator
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcasterContract
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPreparationFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPermissionFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.fragment.summary.PlayBroadcastSummaryFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.awaitResume
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity : BaseActivity(),
    PlayBaseCoordinator,
    PlayBroadcasterContract,
    PlayBroadcaster.Callback {

    private val retainedComponent by retainedComponent {
        DaggerActivityRetainedComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    @Inject
    lateinit var viewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    @Inject
    lateinit var broadcasterFactory: PlayBroadcaster.Factory

    @Inject
    lateinit var remoteConfig: RemoteConfig

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var globalErrorView: GlobalError
    private lateinit var aspectFrameLayout: AspectFrameLayout
    private lateinit var surfaceView: SurfaceView

    private var isRecreated = false
    private var isResultAfterAskPermission = false
    private var channelType = ChannelStatus.Unknown

    private var toasterBottomMargin = 0

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    private val permissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO)
    private val permissionHelper by lazy { PermissionHelperImpl(this) }

    private lateinit var pageMonitoring: PageLoadTimePerformanceInterface

    private lateinit var pauseLiveDialog: DialogUnify

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var broadcaster: PlayBroadcaster

    private val isPortrait: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setFragmentFactory()
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        initViewModel()
        setContentView(R.layout.activity_play_broadcast)
        isRecreated = (savedInstanceState != null)

        initBroadcaster()

        initView()
        setupView()

        if (savedInstanceState != null) {
            populateSavedState(savedInstanceState)
            requestPermission()
        }

        if (!viewModel.isLiveStreamEnded()) {
            PlayBroadcasterIdlingResource.increment()
            getConfiguration()
        }
        observeConfiguration()

        if (GlobalConfig.DEBUG) setupDebugView()
    }

    private fun initBroadcaster() {
        val handler = Handler(Looper.getMainLooper())
        broadcaster = broadcasterFactory.create(
            activityContext = this,
            handler = handler,
            callback = this,
            remoteConfig = remoteConfig,
        )
    }

    override fun onStart() {
        super.onStart()
        aspectFrameLayout.setAspectRatio(AspectFrameLayout.DEFAULT_RATIO_WINDOW_SIZE)
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        lockOrientation()
        createBroadcaster()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        releaseBroadcaster()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.sendLogs()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyBroadcaster()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
            viewModel.saveState(outState)
            outState.putString(CHANNEL_ID, viewModel.channelId)
            outState.putString(CHANNEL_TYPE, channelType.value)
        } catch (e: Throwable) {}
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) return
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_PERMISSION_CODE) isResultAfterAskPermission = true
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPostResume() {
        super.onPostResume()

        if (isResultAfterAskPermission) {
            if (isRequiredPermissionGranted()) configureChannelType(channelType)
            else showPermissionPage()
        }
        isResultAfterAskPermission = false
    }

    override fun <T : Fragment> navigateToFragment(fragmentClass: Class<out T>, extras: Bundle, sharedElements: List<View>, onFragment: (T) -> Unit) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        if (currentFragment == null || currentFragment::class.java != fragmentClass) {
            destFragment.arguments = extras
            fragmentTransaction
                    .replace(R.id.fl_container, destFragment, fragmentClass.name)
                    .commit()
        }
    }

    override fun onBackPressed() {
        if (shouldClosePage()) return
        super.onBackPressed()
    }

    private fun inject() {
        retainedComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryCreator.create(this)
        ).get(PlayBroadcastViewModel::class.java)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun initView() {
        containerSetup = findViewById(R.id.fl_container)
        globalErrorView = findViewById(R.id.global_error)
        aspectFrameLayout = findViewById(R.id.aspect_ratio_view)
        surfaceView = findViewById(R.id.surface_view)
    }

    private fun setupView() {
        surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (surfaceHolder != null) {
                    return
                }
                surfaceHolder = holder
                // We got surface to draw on, start streamer creation
                createBroadcaster()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                broadcaster.updateSurfaceSize(Broadcaster.Size(width, height))
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                surfaceHolder = null
                releaseBroadcaster()
            }
        })
    }

    private fun getConfiguration() {
        startNetworkMonitoring()
        val authorType = intent.getStringExtra(KEY_AUTHOR_TYPE) ?: TYPE_UNKNOWN
        viewModel.submitAction(PlayBroadcastAction.GetConfiguration(authorType))
    }

    private fun populateSavedState(savedInstanceState: Bundle) {
        val channelId = savedInstanceState.getString(CHANNEL_ID)
        val channelType = savedInstanceState.getString(CHANNEL_TYPE)
        viewModel.restoreState(savedInstanceState)
        channelId?.let { viewModel.setChannelId(it) }
        channelType?.let {
            this.channelType = ChannelStatus.getByValue(it)
        }
    }

    private fun getFragmentByClassName(fragmentClass: Class<out Fragment>): Fragment {
        return fragmentFactory.instantiate(classLoader, fragmentClass.name)
    }

    private fun getCurrentFragment() = supportFragmentManager.findFragmentById(R.id.fl_container)

    private fun shouldClosePage(): Boolean {
        val currentVisibleFragment = getCurrentFragment()
        if (currentVisibleFragment != null && currentVisibleFragment is PlayBaseBroadcastFragment) {
            return currentVisibleFragment.onBackPressed()
        }
        return false
    }

    private fun setLayoutFullScreen() {
        systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    //region observe
    /**
     * Observe
     */
    private fun observeConfiguration() {
        viewModel.observableConfigInfo.observe(this) { result ->
            startRenderMonitoring()
            when(result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    if (!isRecreated) handleChannelConfiguration(result.data)
                    else if (result.data.channelStatus == ChannelStatus.Pause) showDialogContinueLiveStreaming()
                    stopPageMonitoring()

                    if(!PlayBroadcasterIdlingResource.idlingResource.isIdleNow)
                        PlayBroadcasterIdlingResource.decrement()
                }
                is NetworkResult.Fail -> {
                    invalidatePerformanceData()
                    showLoading(false)
                    showToaster(
                            err = result.error,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = { result.onRetry() }
                    )
                }
            }
        }
    }
    //endregion

    private fun handleChannelConfiguration(config: ConfigurationUiModel) {
        this.channelType = config.channelStatus
        if (isRequiredPermissionGranted()) configureChannelType(channelType)
        else requestPermission()
    }

    private fun configureChannelType(channelStatus: ChannelStatus) {
        if (isRecreated) return
        if (!viewModel.isUserLoggedIn) {
            globalErrorView.channelNotFound { this.finish() }
            globalErrorView.show()
            return
        }
        when (channelStatus) {
            ChannelStatus.Pause -> {
                openBroadcastActivePage()
                showDialogContinueLiveStreaming()
            }
            ChannelStatus.Draft,
            ChannelStatus.CompleteDraft,
            ChannelStatus.Unknown, ChannelStatus.Live -> openBroadcastSetupPage()
        }
    }

    private fun requestPermission() {
        permissionHelper.requestMultiPermissionsFullFlow(
                permissions = permissions,
                requestCode = REQUEST_PERMISSION_CODE,
                permissionResultListener = object: PermissionResultListener {
                    override fun onRequestPermissionResult(): PermissionStatusHandler {
                        return {
                            if (isAllGranted()) doWhenResume { configureChannelType(channelType) }
                            else doWhenResume { showPermissionPage() }
                        }
                    }

                    override fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
                        return false
                    }
                }
        )
    }

    private fun isRequiredPermissionGranted() = permissionHelper.isAllPermissionsGranted(permissions)

    fun checkAllPermission() {
        if (isRequiredPermissionGranted()) configureChannelType(channelType)
        else showPermissionPage()
    }

    private fun openBroadcastSetupPage() {
        navigateToFragment(PlayBroadcastPreparationFragment::class.java)
        analytic.openSetupScreen()
    }

    private fun openBroadcastActivePage() {
        navigateToFragment(PlayBroadcastUserInteractionFragment::class.java)
        analytic.openBroadcastScreen(viewModel.channelId)
    }

    private fun showPermissionPage() {
        navigateToFragment(PlayPermissionFragment::class.java)
        analytic.openPermissionScreen()
    }

    private fun openBroadcastSummaryPage() {
        navigateToFragment(PlayBroadcastSummaryFragment::class.java)
        analytic.impressReportPage(viewModel.channelId)
    }

    private fun showDialogWhenUnSupportedDevices() {
        getDialog(
                title = getString(R.string.play_dialog_unsupported_device_title),
                desc = getString(R.string.play_dialog_unsupported_device_desc),
                primaryCta = getString(R.string.play_broadcast_exit),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    finish()
                }
        ).show()
    }

    private fun showToaster(
        err: Throwable,
        duration: Int = Toaster.LENGTH_INDEFINITE,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            toasterBottomMargin = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
        }

        findViewById<View>(android.R.id.content)?.showErrorToaster(
                err = err,
                duration = duration,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    private fun getLoadingFragment(): LoadingDialogFragment {
        return LoadingDialogFragment.get(supportFragmentManager, classLoader)
    }

    private fun isLoadingDialogVisible(): Boolean {
        val loadingDialog = getLoadingFragment()
        return loadingDialog.isVisible
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading && !isLoadingDialogVisible()) {
            getLoadingFragment().show(supportFragmentManager)
        } else if (isLoadingDialogVisible()) {
            getLoadingFragment().dismiss()
        }
    }

    private fun showDialogContinueLiveStreaming() {
        if (!::pauseLiveDialog.isInitialized) {
            pauseLiveDialog = getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    analytic.clickDialogContinueBroadcastOnLivePage(
                        viewModel.channelId,
                        viewModel.channelTitle
                    )
                    broadcaster.resume(shouldContinue = true)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    stopBroadcast()
                    openBroadcastSummaryPage()
                }
            )
        }
        if (!pauseLiveDialog.isShowing) {
            pauseLiveDialog.show()
            analytic.viewDialogContinueBroadcastOnLivePage(viewModel.channelId, viewModel.channelTitle)
        }
    }

    private fun doWhenResume(block: () -> Unit) {
        lifecycleScope.launch {
            awaitResume()
            block()
        }
    }

    private fun startPageMonitoring() {
        pageMonitoring = PageLoadTimePerformanceCallback(
                PLAY_BROADCASTER_TRACE_PREPARE_PAGE,
                PLAY_BROADCASTER_TRACE_REQUEST_NETWORK,
                PLAY_BROADCASTER_TRACE_RENDER_PAGE
        )
        pageMonitoring.startMonitoring(PLAY_BROADCASTER_TRACE_PAGE)
        starPrepareMonitoring()
    }

    private fun starPrepareMonitoring() {
        pageMonitoring.startPreparePagePerformanceMonitoring()
    }

    private fun stopPrepareMonitoring() {
        pageMonitoring.stopPreparePagePerformanceMonitoring()
    }

    private fun startNetworkMonitoring() {
        stopPrepareMonitoring()
        pageMonitoring.startNetworkRequestPerformanceMonitoring()
    }

    private fun stopNetworkMonitoring() {
        pageMonitoring.stopNetworkRequestPerformanceMonitoring()
    }

    private fun startRenderMonitoring() {
        stopNetworkMonitoring()
        pageMonitoring.startRenderPerformanceMonitoring()
    }

    private fun stopRenderMonitoring() {
        pageMonitoring.stopRenderPerformanceMonitoring()
    }

    private fun stopPageMonitoring() {
        stopRenderMonitoring()
        pageMonitoring.stopMonitoring()
    }

    private fun invalidatePerformanceData() {
        pageMonitoring.invalidate()
    }

    @TestOnly
    fun getPltPerformanceResultData(): PltPerformanceData {
        return pageMonitoring.getPltPerformanceData()
    }

    /**
     * Larix
     */
    private var debugView: PlayLivePusherDebugView? = null

    private fun setupDebugView() {
        val ivSetting = findViewById<AppCompatImageView>(R.id.iv_play_bro_debug_mode)
        debugView = findViewById(R.id.view_play_bro_debug)

        ivSetting.show()
        ivSetting.setOnClickListener {
            debugView?.show()
        }
    }

    private fun createBroadcaster() {
        if (isRequiredPermissionGranted()) {
            val holder = surfaceHolder ?: return
            val surfaceSize = Broadcaster.Size(surfaceView.width, surfaceView.height)
            initBroadcasterWithDelay(holder, surfaceSize)
        }
        else showPermissionPage()
    }

    private fun initBroadcasterWithDelay(
        holder: SurfaceHolder,
        surfaceSize: Broadcaster.Size,
    ) {
        lifecycleScope.launch(dispatcher.main) {
            delay(INIT_BROADCASTER_DELAY)
            broadcaster.create(holder, surfaceSize)
        }
    }

    private fun releaseBroadcaster() {
        broadcaster.release()
    }

    private fun destroyBroadcaster() {
        broadcaster.destroy()
    }

    /*
     temporarily lock the orientation
     because we don't handle onConfigurationChanged()
     */
    private fun lockOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
    }

    override fun updateAspectRatio(activeCameraVideoSize: Broadcaster.Size) {
        val width = activeCameraVideoSize.width.toDouble()
        val height = activeCameraVideoSize.height.toDouble()
        val aspectRatio = if (isPortrait) height / width else width / height
        aspectFrameLayout.setAspectRatio(aspectRatio)
        debugView?.logAspectRatio(aspectRatio)
    }

    override fun onBroadcastInitStateChanged(state: BroadcastInitState) {
        if (state is BroadcastInitState.Error) showDialogWhenUnSupportedDevices()
        lifecycleScope.launch(dispatcher.main) {
            debugView?.logBroadcastInitState(state)
        }
    }

    override fun onBroadcastStateChanged(state: PlayBroadcasterState) {
        viewModel.submitAction(PlayBroadcastAction.BroadcastStateChanged(state))
        lifecycleScope.launch(dispatcher.main) {
            debugView?.logBroadcastState(state)
        }
    }

    override fun onBroadcastStatisticUpdate(metric: BroadcasterMetric) {
        viewModel.sendBroadcasterLog(metric)
        lifecycleScope.launch(dispatcher.main) {
            debugView?.logBroadcastStatistic(metric)
        }
    }

    override fun getBroadcaster(): PlayBroadcaster {
        return broadcaster
    }

    private fun stopBroadcast() {
        broadcaster.stop()
        viewModel.stopTimer()
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_TYPE = "channel_type"
        private const val REQUEST_PERMISSION_CODE = 3298
        const val RESULT_PERMISSION_CODE = 3297

        private const val TERMS_AND_CONDITION_TAG = "TNC_BOTTOM_SHEET"
        private const val INIT_BROADCASTER_DELAY = 500L
    }
}
