package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.splitinstall.SplitInstallHelper
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
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
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PLAY_BROADCASTER_TRACE_PAGE
import com.tokopedia.play.broadcaster.analytic.PLAY_BROADCASTER_TRACE_PREPARE_PAGE
import com.tokopedia.play.broadcaster.analytic.PLAY_BROADCASTER_TRACE_RENDER_PAGE
import com.tokopedia.play.broadcaster.analytic.PLAY_BROADCASTER_TRACE_REQUEST_NETWORK
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.analytic.beautification.PlayBroadcastBeautificationAnalyticStateHolder
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.di.DaggerActivityRetainedComponent
import com.tokopedia.play.broadcaster.di.PlayBroadcastInjector
import com.tokopedia.play.broadcaster.di.PlayBroadcastModule
import com.tokopedia.play.broadcaster.pusher.PlayBroadcaster
import com.tokopedia.play.broadcaster.pusher.state.PlayBroadcasterState
import com.tokopedia.play.broadcaster.pusher.view.PlayLivePusherDebugView
import com.tokopedia.play.broadcaster.ui.action.PlayBroadcastAction
import com.tokopedia.play.broadcaster.ui.bridge.BeautificationUiBridge
import com.tokopedia.play.broadcaster.ui.event.PlayBroadcastEvent
import com.tokopedia.play.broadcaster.ui.model.ChannelStatus
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel
import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
import com.tokopedia.play.broadcaster.util.delegate.retainedComponent
import com.tokopedia.play.broadcaster.util.extension.channelNotFound
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.util.idling.PlayBroadcasterIdlingResource
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.util.wrapper.PlayBroadcastValueWrapper
import com.tokopedia.play.broadcaster.view.contract.PlayBaseCoordinator
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcasterContract
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPreparationFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPermissionFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.beautification.BeautificationSetupFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.fragment.summary.PlayBroadcastSummaryFragment
import com.tokopedia.play.broadcaster.view.scale.BroadcasterFrameScalingManager
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.awaitResume
import com.tokopedia.play_common.util.extension.withCache
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */

@Suppress("LateinitUsage")
class PlayBroadcastActivity :
    BaseActivity(),
    PlayBaseCoordinator,
    PlayBroadcasterContract,
    PlayBroadcaster.Callback,
    SurfaceHolder.Callback {

    private val retainedComponent by retainedComponent {
        DaggerActivityRetainedComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .playBroadcastModule(PlayBroadcastModule(this))
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

    @Inject
    lateinit var hydraConfigStore: HydraConfigStore

    @Inject
    lateinit var broadcasterFrameScalingManager: BroadcasterFrameScalingManager

    @Inject
    lateinit var beautificationUiBridge: BeautificationUiBridge

    @Inject
    lateinit var beautificationAnalyticStateHolder: PlayBroadcastBeautificationAnalyticStateHolder

    @Inject
    lateinit var valueWrapper: PlayBroadcastValueWrapper

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var globalErrorView: GlobalError
    private lateinit var aspectFrameLayout: AspectFrameLayout
    private lateinit var surfaceView: SurfaceView
    private lateinit var surfaceCardView: CardView

    private var isRecreated = false
    private var isResultAfterAskPermission = false
    private var channelType = ChannelStatus.Unknown

    private var toasterBottomMargin = 0

    private val offset8 by lazyThreadSafetyNone {
        resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3)
    }
    private val offset16 by lazyThreadSafetyNone {
        resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
    }

    private var systemUiVisibility: Int
        get() = window.decorView.systemUiVisibility
        set(value) {
            window.decorView.systemUiVisibility = value
        }

    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    private val permissionHelper by lazy { PermissionHelperImpl(this) }

    private lateinit var pageMonitoring: PageLoadTimePerformanceInterface

    private lateinit var pauseLiveDialog: DialogUnify

    private var surfaceHolder: SurfaceHolder? = null
    private lateinit var broadcaster: PlayBroadcaster

    private val isPortrait: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    private val dialogUnSupportedDevice by lazyThreadSafetyNone {
        getDialog(
            title = getString(R.string.play_dialog_unsupported_device_title),
            desc = getString(R.string.play_dialog_unsupported_device_desc),
            primaryCta = getString(R.string.play_broadcast_exit),
            primaryListener = { dialog ->
                dialog.dismiss()
                finish()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        loadEffectNativeLibrary()
        setFragmentFactory()
        startPageMonitoring()
        if (savedInstanceState != null) {
            hydraConfigStore.setChannelId(savedInstanceState.getString(CHANNEL_ID).orEmpty())
        }
        super.onCreate(savedInstanceState)
        setupBroadcaster()
        initViewModel()
        observeUiState()
        observeConfiguration()
        setContentView(R.layout.activity_play_broadcast)
        isRecreated = (savedInstanceState != null)

        initView()
        initListener()

        if (savedInstanceState != null) {
            populateSavedState(savedInstanceState)
            requestPermission()
        }

        if (GlobalConfig.DEBUG) setupDebugView()
    }

    override fun onStart() {
        super.onStart()
        aspectFrameLayout.setAspectRatio(AspectFrameLayout.DEFAULT_RATIO_WINDOW_SIZE)
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        lockOrientation()
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
            outState.putString(CHANNEL_ID, viewModel.channelId)
            outState.putString(CHANNEL_TYPE, channelType.value)
            viewModel.saveState(outState)
        } catch (e: Throwable) {}
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (isRequiredPermissionGranted()) createBroadcaster(viewModel.broadcastingConfig)
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_PERMISSION_CODE) isResultAfterAskPermission = true
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPostResume() {
        super.onPostResume()

        if (isResultAfterAskPermission) {
            if (isRequiredPermissionGranted()) {
                configureChannelType(channelType)
            } else {
                showPermissionPage()
            }
        }
        isResultAfterAskPermission = false
    }

    override fun <T : Fragment> navigateToFragment(
        fragmentClass: Class<out T>,
        extras: Bundle,
        sharedElements: List<View>,
        onFragment: (T) -> Unit,
        isAddToBackStack: Boolean
    ) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val destFragment = getFragmentByClassName(fragmentClass)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fl_container)
        if (currentFragment == null || currentFragment::class.java != fragmentClass) {
            destFragment.arguments = extras
            fragmentTransaction
                .apply {
                    if (isAddToBackStack) {
                        add(R.id.fl_container, destFragment, fragmentClass.name)
                        addToBackStack(null)
                    } else {
                        replace(R.id.fl_container, destFragment, fragmentClass.name)
                    }
                }
                .commit()
        }
    }

    override fun onBackPressed() {
        if (shouldClosePage()) return
        super.onBackPressed()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (surfaceHolder != null) {
            return
        }
        surfaceHolder = holder
        if (!::broadcaster.isInitialized) return
        createBroadcaster(viewModel.broadcastingConfig)
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        width: Int,
        height: Int
    ) {
        if (!::broadcaster.isInitialized) return
        broadcaster.updateSurfaceSize(Broadcaster.Size(width, height))
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        surfaceHolder = null
        releaseBroadcaster()
    }

    private fun inject() {
        val component = PlayBroadcastInjector.get() ?: retainedComponent
        component.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            viewModelFactoryCreator.create(this)
        )[PlayBroadcastViewModel::class.java]

        if (!viewModel.isLiveStreamEnded()) {
            PlayBroadcasterIdlingResource.increment()
            getConfiguration()
        }
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupBroadcaster() {
        broadcaster = broadcasterFactory.create(
            activityContext = this,
            handler = Handler(Looper.getMainLooper()),
            callback = this,
            remoteConfig = remoteConfig
        )
    }

    private fun loadEffectNativeLibrary() {
        try {
            SplitInstallHelper.loadLibrary(this, "c++_shared")
            SplitInstallHelper.loadLibrary(this, "effect")
        } catch (throwable: Throwable) {
            Firebase.crashlytics.recordException(throwable)
        }
    }

    private fun observeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.withCache().collectLatest {
                renderFaceFilter(it.prevValue?.beautificationConfig?.selectedFaceFilter, it.value.beautificationConfig.selectedFaceFilter)
                renderPreset(it.prevValue?.beautificationConfig?.selectedPreset, it.value.beautificationConfig.selectedPreset)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is PlayBroadcastEvent.InitializeBroadcaster -> {
                        createBroadcaster(event.data)
                    }
                    is PlayBroadcastEvent.BeautificationRebindEffect -> {
                        rebindEffect(isFirstTimeOpenPage = false)
                    }
                    is PlayBroadcastEvent.BeautificationDownloadAssetFailed -> {
                        analytic.viewFailDownloadPreset(
                            viewModel.selectedAccount,
                            beautificationAnalyticStateHolder.getPageSourceForAnalytic(),
                            event.preset.id
                        )

                        showToaster(
                            err = event.throwable,
                            customErrMessage = getString(R.string.play_broadcaster_fail_download_filter),
                            duration = Toaster.LENGTH_SHORT,
                            actionLabel = getString(R.string.play_broadcaster_retry),
                            actionListener = {
                                analytic.clickRetryDownloadPreset(
                                    viewModel.selectedAccount,
                                    beautificationAnalyticStateHolder.getPageSourceForAnalytic(),
                                    event.preset.id
                                )
                                viewModel.submitAction(PlayBroadcastAction.SelectPresetOption(event.preset))
                            }
                        )
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderFaceFilter(
        prev: FaceFilterUiModel?,
        value: FaceFilterUiModel?
    ) {
        if (prev == value) return
        if (!::broadcaster.isInitialized) return

        val selectedFaceFilter = value ?: return
        applyFaceFilter(listOf(selectedFaceFilter))
    }

    private fun renderPreset(
        prev: PresetFilterUiModel?,
        value: PresetFilterUiModel?
    ) {
        if (prev == value) return
        if (!::broadcaster.isInitialized) return

        val selectedPreset = value ?: return
        applyPreset(selectedPreset)
    }

    private fun applyFaceFilter(
        faceFilters: List<FaceFilterUiModel>,
        withToaster: Boolean = true
    ): Boolean {
        var isAllFilterApplied = true

        faceFilters.forEach { faceFilter ->
            if (faceFilter.isRemoveEffect) {
                broadcaster.removeFaceFilter()
            } else {
                val isSuccess = broadcaster.setFaceFilter(
                    faceFilter.id,
                    if (faceFilter.active) faceFilter.value.toFloat() else 0f
                )

                if (isSuccess) return@forEach

                isAllFilterApplied = false

                if (!withToaster) return@forEach

                analytic.viewFailApplyBeautyFilter(
                    account = viewModel.selectedAccount,
                    page = beautificationAnalyticStateHolder.pageSource.mapToAnalytic(),
                    customFace = faceFilter.id
                )

                showToaster(
                    err = Exception("fail to apply face filter : ${faceFilter.id}"),
                    customErrMessage = getString(R.string.play_broadcaster_fail_apply_filter),
                    duration = Toaster.LENGTH_SHORT,
                    actionLabel = getString(R.string.play_broadcaster_retry),
                    actionListener = {
                        analytic.clickRetryApplyBeautyFilter(
                            account = viewModel.selectedAccount,
                            page = beautificationAnalyticStateHolder.pageSource.mapToAnalytic(),
                            customFace = faceFilter.id
                        )
                        applyFaceFilter(listOf(faceFilter))
                    }
                )
            }
        }

        return isAllFilterApplied
    }

    private fun applyPreset(
        preset: PresetFilterUiModel,
        withToaster: Boolean = true
    ): Boolean {
        return if (preset.isRemoveEffect) {
            broadcaster.removePreset()
            true
        } else {
            val isSuccess = broadcaster.setPreset(preset.id, preset.value.toFloat())

            if (!isSuccess && withToaster) {
                showToaster(
                    err = Exception("fail to apply preset : ${preset.id}"),
                    customErrMessage = getString(R.string.play_broadcaster_fail_apply_filter),
                    duration = Toaster.LENGTH_SHORT,
                    actionLabel = getString(R.string.play_broadcaster_retry),
                    actionListener = {
                        applyPreset(preset)
                    }
                )
            }

            isSuccess
        }
    }

    private fun rebindEffect(isFirstTimeOpenPage: Boolean) {
        if (viewModel.isBeautificationEnabled) {
            val isAllFaceFilterApplied = if (viewModel.selectedFaceFilter?.isRemoveEffect == true) {
                viewModel.selectedFaceFilter?.let { applyFaceFilter(listOf(it), withToaster = false) }.orTrue()
            } else {
                applyFaceFilter(viewModel.faceFiltersWithoutNoneOption, withToaster = false)
            }

            val isPresetApplied = viewModel.selectedPreset?.let { applyPreset(it, withToaster = false) }.orTrue()

            if (isFirstTimeOpenPage && (!isAllFaceFilterApplied || !isPresetApplied)) {
                analytic.viewFailReapplyBeautyFilter(account = viewModel.selectedAccount)

                showToaster(
                    err = Exception("fail to apply face filter & preset for the first time"),
                    customErrMessage = getString(R.string.play_broadcaster_fail_save_filter),
                    duration = valueWrapper.rebindEffectToasterDuration,
                    actionLabel = getString(R.string.play_broadcaster_retry),
                    actionListener = {
                        analytic.clickRetryReapplyBeautyFilter(account = viewModel.selectedAccount)
                        rebindEffect(true)
                    }
                )
            }
        } else {
            broadcaster.removeFaceFilter()
            broadcaster.removePreset()
        }
    }

    private fun initView() {
        containerSetup = findViewById(R.id.fl_container)
        globalErrorView = findViewById(R.id.global_error)
        aspectFrameLayout = findViewById(R.id.aspect_ratio_view)
        surfaceView = findViewById(R.id.surface_view)
        surfaceCardView = findViewById(R.id.surface_card_view)

        surfaceView.holder.addCallback(this)
    }

    private fun initListener() {
        broadcasterFrameScalingManager.setListener(object : BroadcasterFrameScalingManager.Listener {
            override fun onStartScaleDown() {
                surfaceCardView.radius = offset8.toFloat()
            }

            override fun onStartScaleUp() {
                surfaceCardView.radius = 0f
            }
        })

        aspectFrameLayout.setOnClickListener {
            getCurrentFragment()?.let {
                val isFaceFilterBottomSheetShown = BeautificationSetupFragment.getFragment(
                    it.childFragmentManager,
                    classLoader
                ).isBottomSheetShown

                if (isFaceFilterBottomSheetShown) {
                    beautificationUiBridge.eventBus.emit(BeautificationUiBridge.Event.BeautificationBottomSheetDismissed)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            beautificationUiBridge.eventBus.subscribe().collect { event ->
                when (event) {
                    is BeautificationUiBridge.Event.BeautificationBottomSheetShown -> {
                        val fullPageHeight = findViewById<ViewGroup>(android.R.id.content).rootView.height
                        val bottomSheetHeight = event.bottomSheetHeight

                        broadcasterFrameScalingManager.scaleDown(aspectFrameLayout, bottomSheetHeight, fullPageHeight)
                    }
                    is BeautificationUiBridge.Event.BeautificationBottomSheetDismissed -> {
                        broadcasterFrameScalingManager.scaleUp(aspectFrameLayout)
                    }
                }
            }
        }
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
            when (result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    if (!isRecreated) {
                        handleChannelConfiguration(result.data)
                    } else if (result.data.channelStatus == ChannelStatus.Pause) showDialogContinueLiveStreaming()
                    stopPageMonitoring()

                    if (!PlayBroadcasterIdlingResource.idlingResource.isIdleNow) {
                        PlayBroadcasterIdlingResource.decrement()
                    }

                    if (GlobalConfig.DEBUG) {
                        debugView?.logChannelId(result.data.channelId)
                    }
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
                else -> {
                    // no-op
                }
            }
        }
    }
    //endregion

    private fun handleChannelConfiguration(config: ConfigurationUiModel) {
        this.channelType = config.channelStatus
        if (isRequiredPermissionGranted()) {
            configureChannelType(channelType)
        } else {
            requestPermission()
        }
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
            else -> {
                // no-op
            }
        }
    }

    private fun requestPermission() {
        permissionHelper.requestMultiPermissionsFullFlow(
            permissions = permissions,
            requestCode = REQUEST_PERMISSION_CODE,
            permissionResultListener = object : PermissionResultListener {
                override fun onRequestPermissionResult(): PermissionStatusHandler {
                    return {
                        if (isAllGranted()) {
                            doWhenResume { configureChannelType(channelType) }
                        } else {
                            doWhenResume { showPermissionPage() }
                        }
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
        if (isRequiredPermissionGranted()) {
            configureChannelType(channelType)
        } else {
            showPermissionPage()
        }
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
        if (dialogUnSupportedDevice.isShowing) return
        dialogUnSupportedDevice.show()
    }

    private fun showToaster(
        err: Throwable,
        customErrMessage: String? = null,
        duration: Int = Toaster.LENGTH_INDEFINITE,
        actionLabel: String = "",
        actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            toasterBottomMargin = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
        }

        findViewById<View>(android.R.id.content)?.showErrorToaster(
            err = err,
            customErrMessage = customErrMessage,
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
        if (isLoading) {
            if (!isLoadingDialogVisible()) {
                getLoadingFragment().show(supportFragmentManager)
            }
        } else if (getLoadingFragment().isAdded) {
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

    private fun createBroadcaster(broadcastingConfigUiModel: BroadcastingConfigUiModel) {
        if (isRequiredPermissionGranted()) {
            val holder = surfaceHolder ?: return
            val surfaceSize = Broadcaster.Size(surfaceView.width, surfaceView.height)
            initBroadcasterWithDelay(holder, surfaceSize, broadcastingConfigUiModel)
        } else {
            showPermissionPage()
        }
    }

    private fun initBroadcasterWithDelay(
        holder: SurfaceHolder,
        surfaceSize: Broadcaster.Size,
        broadcastingConfigUiModel: BroadcastingConfigUiModel
    ) {
        lifecycleScope.launch(dispatcher.main) {
            broadcaster.setupThread(viewModel.isBeautificationEnabled)
            delay(INIT_BROADCASTER_DELAY)
            broadcaster.setConfig(
                audioRate = broadcastingConfigUiModel.audioRate,
                videoRate = broadcastingConfigUiModel.videoBitrate,
                videoFps = broadcastingConfigUiModel.fps
            )
            broadcaster.create(holder, surfaceSize, viewModel.isBeautificationEnabled)
            rebindEffect(isFirstTimeOpenPage = true)
        }
    }

    private fun releaseBroadcaster() {
        if (!::broadcaster.isInitialized) return
        broadcaster.release()
    }

    private fun destroyBroadcaster() {
        if (!::broadcaster.isInitialized) return
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
        when (state) {
            is BroadcastInitState.Error -> {
                showDialogWhenUnSupportedDevices()
            }
            is BroadcastInitState.ByteplusInitializationError -> {
                viewModel.submitAction(PlayBroadcastAction.RemoveBeautificationMenu)
            }
            else -> {}
        }

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

        private const val INIT_BROADCASTER_DELAY = 500L
    }
}
