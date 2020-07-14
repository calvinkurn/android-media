package com.tokopedia.play.broadcaster.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.alivc.live.pusher.SurfaceStatus
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.di.broadcast.DaggerPlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastComponent
import com.tokopedia.play.broadcaster.di.broadcast.PlayBroadcastModule
import com.tokopedia.play.broadcaster.di.provider.PlayBroadcastComponentProvider
import com.tokopedia.play.broadcaster.ui.model.ChannelType
import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel
import com.tokopedia.play.broadcaster.ui.model.result.NetworkResult
import com.tokopedia.play.broadcaster.util.DeviceInfoUtil
import com.tokopedia.play.broadcaster.util.channelNotFound
import com.tokopedia.play.broadcaster.util.getDialog
import com.tokopedia.play.broadcaster.util.permission.PermissionHelperImpl
import com.tokopedia.play.broadcaster.util.permission.PermissionResultListener
import com.tokopedia.play.broadcaster.util.permission.PermissionStatusHandler
import com.tokopedia.play.broadcaster.util.showToaster
import com.tokopedia.play.broadcaster.view.contract.PlayBroadcastCoordinator
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastPrepareFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayBroadcastUserInteractionFragment
import com.tokopedia.play.broadcaster.view.fragment.PlayPermissionFragment
import com.tokopedia.play.broadcaster.view.fragment.base.PlayBaseBroadcastFragment
import com.tokopedia.play.broadcaster.view.partial.ActionBarPartialView
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * Created by mzennis on 19/05/20.
 */
class PlayBroadcastActivity : BaseActivity(), PlayBroadcastCoordinator, PlayBroadcastComponentProvider {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var analytic: PlayBroadcastAnalytic

    private lateinit var viewModel: PlayBroadcastViewModel

    private lateinit var containerSetup: FrameLayout
    private lateinit var viewActionBar: ActionBarPartialView
    private lateinit var loaderView: LoaderUnify
    private lateinit var globalErrorView: GlobalError
    private lateinit var surfaceView: SurfaceView

    private var surfaceStatus = SurfaceStatus.UNINITED

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

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        initViewModel()
        setFragmentFactory()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_broadcast)
        isRecreated = (savedInstanceState != null)

        if (savedInstanceState != null) {
            populateSavedState(savedInstanceState)
            requestPermission()
        }

        initPushStream()
        setupContent()
        initView()
        setupView()
        setupInsets()

        if (!DeviceInfoUtil.isDeviceSupported()) {
            showDialogWhenUnSupportedDevices()
            return
        }

        getConfiguration()
        observeConfiguration()
    }

    override fun onStart() {
        super.onStart()
        viewActionBar.rootView.requestApplyInsetsWhenAttached()
    }

    override fun onResume() {
        super.onResume()
        setLayoutFullScreen()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.resumePushStream()
    }

    override fun onPause() {
        super.onPause()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        viewModel.pausePushStream()
    }

    override fun onStop() {
        viewModel.pausePushStream()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(CHANNEL_ID, viewModel.channelId)
        outState.putString(CHANNEL_TYPE, channelType.value)
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
            if (permissionHelper.isAllPermissionsGranted(permissions)) configureChannelType(channelType)
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

    override fun setupTitle(title: String) {
        viewActionBar.setTitle(title)
    }

    override fun setupCloseButton(actionTitle: String) {
        viewActionBar.setupCloseButton(actionTitle)
    }

    override fun showActionBar(shouldShow: Boolean) {
        if (!::viewActionBar.isInitialized) return
        if (shouldShow) viewActionBar.show() else viewActionBar.hide()
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

    private fun initPushStream() {
        viewModel.initPushStream()
    }

    private fun setFragmentFactory() {
        supportFragmentManager.fragmentFactory = fragmentFactory
    }

    private fun setupContent() {
    }

    private fun initView() {
        containerSetup = findViewById(R.id.fl_setup)
        loaderView = findViewById(R.id.loader_initial)
        globalErrorView = findViewById(R.id.error_channel)
        surfaceView = findViewById(R.id.surface_view)
    }

    private fun setupView() {
        viewActionBar = ActionBarPartialView(findViewById(android.R.id.content), object : ActionBarPartialView.Listener {
            override fun onCameraIconClicked() {
                viewModel.switchCamera()
            }

            override fun onCloseIconClicked() {
                onBackPressed()
            }
        })

        surfaceView.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
                surfaceStatus = SurfaceStatus.CHANGED
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder?) {
                surfaceStatus = SurfaceStatus.DESTROYED
            }

            override fun surfaceCreated(surfaceHolder: SurfaceHolder?) {
                if (surfaceStatus == SurfaceStatus.UNINITED) {
                    surfaceStatus = SurfaceStatus.CREATED
                } else if (surfaceStatus == SurfaceStatus.DESTROYED) {
                    surfaceStatus = SurfaceStatus.RECREATED
                }
                startPreview()
            }
        })
    }

    private fun setupInsets() {
        viewActionBar.rootView.doOnApplyWindowInsets { v, insets, _, _ ->
            v.updatePadding(top = insets.systemWindowInsetTop)
        }
    }

    private fun getConfiguration() {
        viewModel.getConfiguration()
    }

    private fun populateSavedState(savedInstanceState: Bundle) {
        val channelId = savedInstanceState.getString(CHANNEL_ID)
        val channelType = savedInstanceState.getString(CHANNEL_TYPE)
        viewModel.setChannelId(channelId)
        this.channelType = ChannelType.getByValue(channelType)
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
            when(result) {
                is NetworkResult.Loading -> loaderView.show()
                is NetworkResult.Success -> {
                    loaderView.hide()
                    if (!isRecreated) handleChannelConfiguration(result.data)
                }
                is NetworkResult.Fail -> {
                    loaderView.hide()
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
            } else {
                if (permissionHelper.isAllPermissionsGranted(permissions)) configureChannelType(channelType)
                else requestPermission()
            }
        } else {
            globalErrorView.channelNotFound { this.finish() }
            globalErrorView.show()
        }
    }

    private fun configureChannelType(channelType: ChannelType) {
        showActionBar(true)
        if (channelType == ChannelType.Pause) {
            showDialogContinueLiveStreaming()
            openBroadcastActivePage()
        } else  {
            openBroadcastSetupPage()
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
                            if (isAllGranted()) configureChannelType(channelType)
                            else showPermissionPage()
                        }
                    }

                    override fun onShouldShowRequestPermissionRationale(permissions: Array<String>, requestCode: Int): Boolean {
                        return false
                    }
                }
        )
    }

    private fun startPreview() {
        if (surfaceStatus != SurfaceStatus.UNINITED &&
                surfaceStatus != SurfaceStatus.DESTROYED) {
            viewModel.startPreview(surfaceView)
        }
    }

    private fun openBroadcastSetupPage() {
        analytic.openSetupScreen()
        navigateToFragment(PlayBroadcastPrepareFragment::class.java)
    }

    private fun openBroadcastActivePage() {
        analytic.openBroadcastScreen(channelId = viewModel.channelId)
        navigateToFragment(PlayBroadcastUserInteractionFragment::class.java)
    }

    private fun showPermissionPage() {
        showActionBar(false)
        analytic.openPermissionScreen()
        navigateToFragment(PlayPermissionFragment::class.java)
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

    private fun showDialogContinueLiveStreaming() {
        getDialog(
                actionType = DialogUnify.HORIZONTAL_ACTION,
                title = getString(R.string.play_dialog_continue_live_title),
                desc = getString(R.string.play_dialog_continue_live_desc),
                primaryCta = getString(R.string.play_next),
                primaryListener = { dialog ->
                    dialog.dismiss()
                    viewModel.startPushStream()
                },
                secondaryCta = getString(R.string.play_broadcast_end),
                secondaryListener = { dialog ->
                    dialog.dismiss()
                    viewModel.stopPushStream(shouldNavigate = true)
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

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_TYPE = "channel_type"
        private const val REQUEST_PERMISSION_CODE = 3298
        const val RESULT_PERMISSION_CODE = 3297
    }
}