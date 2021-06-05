package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.pedro.rtplibrary.view.LightOpenGlView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.analytics.performance.util.PltPerformanceData
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.di.broadcast.DaggerPlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastModule
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.ui.model.ChannelType
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.util.deviceinfo.DeviceInfoUtil
import com.tokopedia.play.broadcaster.util.extension.channelNotFound
import com.tokopedia.play.broadcaster.util.extension.getDialog
import com.tokopedia.play.broadcaster.util.extension.showToaster
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.view.contract.PlayBaseCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayBeforeLiveFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPrepareFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPermissionFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.fragment.loading.LoadingDialogFragment
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.util.extension.awaitResume
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity : BaseActivity(), PlayBaseCoordinator, PlayBroadcastComponentProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    @Inject
    lateinit var dispatcher: CoroutineDispatchers

    private val job = SupervisorJob()
    private val scope = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = job + dispatcher.immediate
    }

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var globalErrorView: GlobalError
    private lateinit var lightOpenGlView: LightOpenGlView

    private lateinit var playBroadcastComponent: PlayBroadcastComponent

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

    private lateinit var loadingFragment: LoadingDialogFragment
    private lateinit var pauseLiveDialog: DialogUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()
        startPageMonitoring()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcast)
        isRecreated = (savedInstanceState != null)

        if (savedInstanceState != null) {
            populateSavedState(savedInstanceState)
            requestPermission()
        }

        if (!DeviceInfoUtil.isDeviceSupported()) {
            showDialogWhenUnSupportedDevices()
            return
        }

        setupContent()
        initView()
        setupView()

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
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancelChildren()
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
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fl_setup)
        if (currentFragment == null || currentFragment::class.java != fragmentClass) {
            destFragment.arguments = extras
            fragmentTransaction
                    .replace(R.id.fl_setup, destFragment, fragmentClass.name)
                    .commit()
        }
    }

    override fun getBroadcastComponent(): PlayBroadcastComponent {
        return playBroadcastComponent
    }

    override fun onBackPressed() {
        if (shouldClosePage()) return
        super.onBackPressed()
    }

    private fun inject() {
        playBroadcastComponent = DaggerPlayBroadcastComponent.builder()
                .playBroadcastModule(PlayBroadcastModule(this))
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()

        playBroadcastComponent.inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContent() {
    }

    private fun initView() {
        containerSetup = findViewById(R.id.fl_setup)
        globalErrorView = findViewById(R.id.global_error)
        lightOpenGlView = findViewById(R.id.surface_view)
    }

    private fun setupView() {
        lightOpenGlView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                startPreview()
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
                stopPreview()
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
            }
        })
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

    private fun getCurrentFragment() = supportFragmentManager.findFragmentById(R.id.fl_setup)

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
        viewModel.observableConfigInfo.observe(this, Observer { result ->
            startRenderMonitoring()
            when(result) {
                is NetworkResult.Loading -> showLoading(true)
                is NetworkResult.Success -> {
                    showLoading(false)
                    if (!isRecreated) handleChannelConfiguration(result.data)
                    stopPageMonitoring()
                }
                is NetworkResult.Fail -> {
                    invalidatePerformanceData()
                    showLoading(false)
                    showToaster(
                            message = result.error.localizedMessage,
                            actionLabel = getString(R.string.play_broadcast_try_again),
                            actionListener = View.OnClickListener { result.onRetry() }
                    )
                }
            }
        })
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
            ChannelType.CompleteDraft -> openBroadcastFinalSetupPage()
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
                            if (isGranted(Manifest.permission.CAMERA)) startPreview()
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

    fun startPreview() {
        if (permissionHelper.isPermissionGranted(Manifest.permission.CAMERA)) viewModel.startPreview(lightOpenGlView)
    }

    fun stopPreview() {
        viewModel.stopPreview()
    }

    fun checkAllPermission() {
        if (isRequiredPermissionGranted()) configureChannelType(channelType)
        else showPermissionPage()
    }

    private fun openBroadcastSetupPage() {
        navigateToFragment(PlayBroadcastPrepareFragment::class.java)
        analytic.openSetupScreen()
    }

    private fun openBroadcastFinalSetupPage() {
        navigateToFragment(PlayBeforeLiveFragment::class.java)
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
            message: String,
            type: Int = Toaster.TYPE_ERROR,
            duration: Int = Toaster.LENGTH_INDEFINITE,
            actionLabel: String = "",
            actionListener: View.OnClickListener = View.OnClickListener { }
    ) {
        if (toasterBottomMargin == 0) {
            toasterBottomMargin = resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5)
        }

        findViewById<View>(android.R.id.content)?.showToaster(
                message = message,
                duration = duration,
                type = type,
                actionLabel = actionLabel,
                actionListener = actionListener,
                bottomMargin = toasterBottomMargin
        )
    }

    private fun getLoadingFragment(): LoadingDialogFragment {
        if (!::loadingFragment.isInitialized) {
            val setupClass = LoadingDialogFragment::class.java
            val fragmentFactory = supportFragmentManager.fragmentFactory
            loadingFragment = fragmentFactory.instantiate(this.classLoader, setupClass.name) as LoadingDialogFragment
        }
        return loadingFragment
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading && !getLoadingFragment().isVisible) {
            getLoadingFragment().show(supportFragmentManager)
        } else if (getLoadingFragment().isVisible) {
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
                    viewModel.continueLiveStream()
                    analytic.clickDialogContinueBroadcastOnLivePage(viewModel.channelId, viewModel.title)
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    viewModel.stopLiveStream(shouldNavigate = true)
                }
            )
        }
        if (!pauseLiveDialog.isShowing) {
            pauseLiveDialog.show()
            analytic.viewDialogContinueBroadcastOnLivePage(viewModel.channelId, viewModel.title)
        }
    }

    private fun doWhenResume(block: () -> Unit) {
        scope.launch {
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
    fun getPltPerformanceResultData(): PltPerformanceData? {
        return pageMonitoring.getPltPerformanceData()
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_TYPE = "channel_type"
        private const val REQUEST_PERMISSION_CODE = 3298
        const val RESULT_PERMISSION_CODE = 3297
    }
}