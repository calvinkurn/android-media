package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
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
import com.tokopedia.broadcaster.widget.SurfaceAspectRatioView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.di.DaggerActivityRetainedComponent
import com.tokopedia.play.broadcaster.ui.model.ChannelType
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.TermsAndConditionUiModel
import com.tokopedia.play.broadcaster.util.delegate.retainedComponent
import com.tokopedia.play.broadcaster.util.extension.channelNotFound
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showErrorToaster
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.contract.PlayBaseCoordinator
import com.tokopedia.play.broadcaster.view.custom.PlayTermsAndConditionView
import com.tokopedia.play.broadcaster.view.fragment.*
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.awaitResume
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity : BaseActivity(), PlayBaseCoordinator {

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

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var globalErrorView: GlobalError
    private lateinit var surfaceView: SurfaceAspectRatioView

    private var isRecreated = false
    private var isResultAfterAskPermission = false
    private var channelType = ChannelType.Unknown

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

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        setFragmentFactory()
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        initViewModel()
        setContentView(R.layout.activity_play_broadcast)
        isRecreated = (savedInstanceState != null)

//        initStreamer()
        initView()

        if (savedInstanceState != null) {
            populateSavedState(savedInstanceState)
            requestPermission()
        }

        setupView()
        setupObserve()

        getConfiguration()
        observeConfiguration()
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.sendLogs()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        try {
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

//    private fun initStreamer() {
//        try {
//            viewModel.createStreamer(this, Handler(Looper.getMainLooper()))
//        } catch (exception: IllegalAccessException) {
//            showDialogWhenUnSupportedDevices()
//            return
//        }
//    }

    private fun initView() {
        containerSetup = findViewById(R.id.fl_container)
        globalErrorView = findViewById(R.id.global_error)
        surfaceView = findViewById(R.id.surface_aspect_ratio_view)
    }

    private fun setupView() {
        surfaceView.setCallback(object : SurfaceAspectRatioView.Callback{
            override fun onSurfaceCreated() {
//                startPreview()
            }

            override fun onSurfaceDestroyed() {
//                stopPreview()
            }
        })
    }

    private fun setupObserve() {
        lifecycleScope.launchWhenResumed {
            viewModel.uiState.collectLatest { state ->
                showTermsAndConditionBottomSheet(state.channel.canStream, state.channel.tnc)
            }
        }
    }

    private fun getConfiguration() {
        startNetworkMonitoring()
        viewModel.getConfiguration()
    }

    private fun populateSavedState(savedInstanceState: Bundle) {
        val channelId = savedInstanceState.getString(CHANNEL_ID)
        val channelType = savedInstanceState.getString(CHANNEL_TYPE)
        channelId?.let { viewModel.setChannelId(it) }
        channelType?.let {
            this.channelType = ChannelType.getByValue(it)
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
                    else if(result.data.channelType == ChannelType.Pause) showDialogContinueLiveStreaming()
                    stopPageMonitoring()
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
        if (config.streamAllowed) {
            this.channelType = config.channelType
            if (channelType == ChannelType.Active) {
                showDialogWhenActiveOnOtherDevices()
                analytic.viewDialogViolation(config.channelId)
            } else {
                if (isRequiredPermissionGranted()) configureChannelType(channelType)
                else requestPermission()
            }
        } else {
            globalErrorView.channelNotFound { this.finish() }
            globalErrorView.show()
        }
    }

    private fun configureChannelType(channelType: ChannelType) {
        if (isRecreated) return
        when (channelType) {
            ChannelType.Pause -> {
                openBroadcastActivePage()
                showDialogContinueLiveStreaming()
            }
            else -> openBroadcastSetupPage()
        }
    }

    private fun requestPermission() {
        permissionHelper.requestMultiPermissionsFullFlow(
                permissions = permissions,
                requestCode = REQUEST_PERMISSION_CODE,
                permissionResultListener = object: PermissionResultListener {
                    override fun onRequestPermissionResult(): PermissionStatusHandler {
                        return {
                            if (isGranted(Manifest.permission.CAMERA)) {
//                                startPreview()
                            }
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

//    fun startPreview() {
//        if (permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)) {
//            viewModel.startPreview(surfaceView)
//        }
//    }

//    fun stopPreview() {
//        viewModel.stopPreview()
//    }

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

    private fun showDialogWhenActiveOnOtherDevices() {
        getDialog(
                title = getString(R.string.play_dialog_error_active_other_devices_title),
                desc = getString(R.string.play_dialog_error_active_other_devices_desc),
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
        val loadingFragment = getAddedLoadingDialog()
        return if (loadingFragment == null) {
            val setupClass = LoadingDialogFragment::class.java
            val fragmentFactory = supportFragmentManager.fragmentFactory
            fragmentFactory.instantiate(this.classLoader, setupClass.name) as LoadingDialogFragment
        } else loadingFragment
    }

    private fun isLoadingDialogVisible(): Boolean {
        val loadingDialog = getAddedLoadingDialog()
        return loadingDialog != null && loadingDialog.isVisible
    }

    private fun getAddedLoadingDialog(): LoadingDialogFragment? {
        return LoadingDialogFragment.get(supportFragmentManager)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading && !isLoadingDialogVisible()) {
            getLoadingFragment().show(supportFragmentManager)
        } else if (isLoadingDialogVisible()) {
            getLoadingFragment().dismiss()
        }
    }

    fun showDialogContinueLiveStreaming() {
        if (!::pauseLiveDialog.isInitialized) {
            pauseLiveDialog = getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
//                    viewModel.continueLiveStream()
                    analytic.clickDialogContinueBroadcastOnLivePage(viewModel.channelId, viewModel.channelTitle)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
//                    viewModel.stopLiveStream(shouldNavigate = true)
                }
            )
        }
        if (!pauseLiveDialog.isShowing) {
            pauseLiveDialog.show()
            analytic.viewDialogContinueBroadcastOnLivePage(viewModel.channelId, viewModel.channelTitle)
        }
    }

    fun isDialogContinueLiveStreamOpen(): Boolean {
        return if(!::pauseLiveDialog.isInitialized) false
        else pauseLiveDialog.isShowing
    }

    private fun showTermsAndConditionBottomSheet(
        canStream: Boolean,
        tncList: List<TermsAndConditionUiModel>
    ) {
        val existingFragment = supportFragmentManager.findFragmentByTag(TERMS_AND_CONDITION_TAG)

        if (canStream) {
            if (existingFragment is BottomSheetUnify && existingFragment.isVisible) {
                existingFragment.setOnDismissListener {  }
                existingFragment.dismiss()
            }
            return
        }

        val (bottomSheet, view) = if (existingFragment is BottomSheetUnify) {
            existingFragment to existingFragment.requireView().findViewWithTag(
                TERMS_AND_CONDITION_TAG
            )
        } else {
            val bottomSheet = BottomSheetUnify().apply {
                clearContentPadding = true
                setTitle(this@PlayBroadcastActivity.getString(R.string.play_bro_tnc_title))
            }

            val view = PlayTermsAndConditionView(this@PlayBroadcastActivity)
                .apply {
                    tag = TERMS_AND_CONDITION_TAG
                    setListener(object : PlayTermsAndConditionView.Listener {
                        override fun onOkButtonClicked(view: PlayTermsAndConditionView) {
                            bottomSheet.dismiss()
                        }
                    })
                }

            bottomSheet.setChild(view)

            bottomSheet to view
        }
        if (!bottomSheet.isVisible) {
            view.setTermsAndConditions(tncList)
            bottomSheet.setOnDismissListener { finish() }
            bottomSheet.show(supportFragmentManager, TERMS_AND_CONDITION_TAG)
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

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_TYPE = "channel_type"
        private const val REQUEST_PERMISSION_CODE = 3298
        const val RESULT_PERMISSION_CODE = 3297

        private const val TERMS_AND_CONDITION_TAG = "TNC_BOTTOM_SHEET"
    }
}