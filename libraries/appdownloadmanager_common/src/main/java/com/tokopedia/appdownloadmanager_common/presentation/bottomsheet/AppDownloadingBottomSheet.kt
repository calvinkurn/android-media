package com.tokopedia.appdownloadmanager_common.presentation.bottomsheet

import android.app.DownloadManager
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.domain.model.AppVersionBetaInfoModel
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.model.AppDownloadingUiEvent
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingUiState
import com.tokopedia.appdownloadmanager_common.presentation.screen.AppDownloadInsufficientSpaceScreen
import com.tokopedia.appdownloadmanager_common.presentation.screen.AppDownloadingState
import com.tokopedia.appdownloadmanager_common.presentation.screen.DownloadManagerOnboardingScreen
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission.isAllPermissionNotGranted
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.APK_URL
import com.tokopedia.appdownloadmanager_common.presentation.viewmodel.DownloadManagerViewModel
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.appdownloadmanager_common.R as appdownloadmanager_commonR
import com.tokopedia.unifycomponents.R as unifycomponentsR

class AppDownloadingBottomSheet :
    BottomSheetUnify(),
    HasComponent<DownloadManagerComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DownloadManagerViewModel::class.java)
    }

    private var downloadManagerUpdateModel: DownloadManagerUpdateModel? = null

    private var appBetaVersionInfoModel: AppVersionBetaInfoModel? = null

    private var startAppDownloading: (() -> Unit)? = null

    private var showToasterError: ((errorMessage: String) -> Unit)? = null

    private var downloadManagerSuccessListener: DownloadManagerSuccessListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.let {
            val composeView = ComposeView(it).apply {
                setContent {
                    NestTheme {
                        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

                        val downloadingUiState by viewModel.downloadingUiState.collectAsStateWithLifecycle()

                        when (downloadingUiState) {
                            is DownloadingUiState.Onboarding -> {
                                DownloadManagerOnboardingScreen(
                                    downloadManagerUpdateModel = downloadManagerUpdateModel,
                                    onDownloadClick = {
                                        activity?.let { mActivity ->
                                            if (isAllPermissionNotGranted(mActivity)) {
                                                requestPermissions(
                                                    AppDownloadManagerPermission.requiredPermissions,
                                                    AppDownloadManagerPermission.PERMISSIONS_REQUEST_EXTERNAL_STORAGE
                                                )
                                            } else {
                                                startDownloadingAndChangeState()
                                            }
                                        }
                                    }
                                )
                            }

                            is DownloadingUiState.Downloading -> {
                                val apkUrl = APK_URL.format(
                                    appBetaVersionInfoModel?.versionName,
                                    appBetaVersionInfoModel?.versionCode
                                )
                                viewModel.startDownload(apkUrl)

                                AppDownloadingState(
                                    viewModel = viewModel,
                                    appDownloadingUiEvent = ::onDownloadingUiEvent
                                )
                            }

                            is DownloadingUiState.InSufficientSpace -> {
                                AppDownloadInsufficientSpaceScreen(onTryAgainClicked = {
                                    viewModel.updateDownloadingState()
                                }, onGoToStorageClicked = {
                                        goToStorageSettings()
                                    })
                            }
                        }
                    }
                }
            }

            showKnob = true
            showCloseIcon = false
            setChild(composeView)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKnobDownloadingUiState(view)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        AppDownloadManagerPermission.checkRequestPermissionResult(
            grantResults,
            requestCode
        ) { hasGrantPermission ->
            if (hasGrantPermission) {
                startDownloadingAndChangeState()
            }
        }
    }

    override fun onDestroy() {
        downloadManagerUpdateModel = null
        startAppDownloading = null
        downloadManagerSuccessListener = null
        showToasterError = null
        super.onDestroy()
    }

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    private fun goToStorageSettings() {
        startActivity(
            Intent(android.provider.Settings.ACTION_INTERNAL_STORAGE_SETTINGS).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private fun hideKnobDownloadingUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.downloadingUiState.collectLatest {
                    when (it) {
                        is DownloadingUiState.Downloading -> {
                            val viewTarget: LinearLayout =
                                view.findViewById(unifycomponentsR.id.bottom_sheet_wrapper)

                            isCancelable = false
                            dialog?.setCanceledOnTouchOutside(false)
                            BottomSheetUnify.bottomSheetBehaviorKnob(viewTarget, false)
                        }
                    }
                }
            }
        }
    }

    private fun onDownloadingUiEvent(uiEvent: AppDownloadingUiEvent) {
        when (uiEvent) {
            is AppDownloadingUiEvent.OnDownloadSuccess -> {
                onDownloadSuccess(uiEvent.fileNamePath)
            }

            is AppDownloadingUiEvent.OnDownloadFailed -> {
                if (uiEvent.statusColumn == DownloadManager.ERROR_INSUFFICIENT_SPACE) {
                    viewModel.updateInsufficientSpaceState()
                } else {
                    val errorMessage = try {
                        getString(appdownloadmanager_commonR.string.app_download_error_network_message)
                    } catch (e: Resources.NotFoundException) {
                        FirebaseCrashlytics.getInstance().recordException(e)
                        uiEvent.reason
                    }
                    onDownloadFailed(errorMessage)
                }
            }

            is AppDownloadingUiEvent.OnCancelClick -> {
                onDownloadCancelled()
            }
        }
    }

    private fun startDownloadingAndChangeState() {
        startAppDownloading?.invoke()
        viewModel.updateDownloadingState()
    }

    private fun onDownloadFailed(reason: String) {
        dismiss()
        showToasterError?.invoke(reason)
    }

    private fun onDownloadSuccess(fileNamePath: String) {
        downloadManagerSuccessListener?.onSuccessDownloaded(fileNamePath)
        dismiss()
    }

    private fun onDownloadCancelled() {
        viewModel.cancelDownload()
        dismiss()
    }

    fun setDownloadManagerUpdate(downloadManagerUpdateModel: DownloadManagerUpdateModel) {
        this.downloadManagerUpdateModel = downloadManagerUpdateModel
    }

    fun setAppBetaVersionInfoModel(appVersionBetaInfoModel: AppVersionBetaInfoModel) {
        this.appBetaVersionInfoModel = appVersionBetaInfoModel
    }

    fun setAppDownloadListener(
        startAppDownloading: () -> Unit,
        downloadManagerSuccessListener: DownloadManagerSuccessListener,
        showToasterError: (errorMessage: String) -> Unit
    ) {
        this.startAppDownloading = startAppDownloading
        this.downloadManagerSuccessListener = downloadManagerSuccessListener
        this.showToasterError = showToasterError
    }

    fun showBottomSheet(fragmentManager: FragmentManager) {
        fragmentManager.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    companion object {
        private val TAG = AppDownloadingBottomSheet::class.java.simpleName

        fun newInstance(): AppDownloadingBottomSheet {
            return AppDownloadingBottomSheet()
        }
    }
}
