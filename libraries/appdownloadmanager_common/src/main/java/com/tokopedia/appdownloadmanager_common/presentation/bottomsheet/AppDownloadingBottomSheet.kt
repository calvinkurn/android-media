package com.tokopedia.appdownloadmanager_common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.domain.model.AppVersionBetaInfoModel
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.appdownloadmanager_common.presentation.dialog.AppFileManagerDialog
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.model.AppDownloadingUiEvent
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingUiState
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.APK_URL
import com.tokopedia.appdownloadmanager_common.presentation.viewmodel.DownloadManagerViewModel
import com.tokopedia.appdownloadmanager_common.screen.AppDownloadingState
import com.tokopedia.appdownloadmanager_common.screen.DownloadManagerOnboardingScreen
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
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
        val composeView = ComposeView(inflater.context).apply {
            setContent {
                val downloadingUiState by viewModel.downloadingUiState.collectAsStateWithLifecycle()

                when (downloadingUiState) {
                    is DownloadingUiState.Onboarding -> {
                        DownloadManagerOnboardingScreen(
                            downloadManagerUpdateModel = downloadManagerUpdateModel,
                            onDownloadClick = {
                                activity?.let { mActivity ->
                                    AppDownloadManagerPermission.checkAndRequestPermission(mActivity) {
                                        if (it) {
                                            startDownloadingAndChangeState()
                                        }
                                    }
                                }
                            }
                        )
                    }

                    is DownloadingUiState.Downloading -> {
                        val apkUrl = APK_URL.format(appBetaVersionInfoModel?.versionName, appBetaVersionInfoModel?.versionCode)
                        viewModel.startDownload(apkUrl)

                        AppDownloadingState(
                            viewModel = viewModel,
                            appDownloadingUiEvent = ::onDownloadingUiEvent
                        )
                    }
                }
            }
        }

        showKnob = true
        showCloseIcon = false
        setChild(composeView)
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
            requestCode,
            grantResults
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
        super.onDestroy()
    }

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    private fun hideKnobDownloadingUiState(view: View) {
        viewLifecycleOwner.lifecycleScope.launch {
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

    private fun onDownloadingUiEvent(uiEvent: AppDownloadingUiEvent) {
        when (uiEvent) {
            is AppDownloadingUiEvent.OnDownloadSuccess -> {
                onDownloadSuccess(uiEvent.fileNamePath)
            }
            is AppDownloadingUiEvent.OnDownloadFailed -> {
                onDownloadFailed(uiEvent.reason)
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
        showErrorToaster(reason)
        dismiss()
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
        downloadManagerSuccessListener: DownloadManagerSuccessListener
    ) {
        this.startAppDownloading = startAppDownloading
        this.downloadManagerSuccessListener = downloadManagerSuccessListener
    }

    fun showBottomSheet(fragmentManager: FragmentManager) {
        fragmentManager.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    private fun showErrorToaster(reason: String) {
        view?.let {
            Toaster.build(
                it,
                reason,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    companion object {
        private val TAG = AppDownloadingBottomSheet::class.java.simpleName

        fun newInstance(): AppDownloadingBottomSheet {
            return AppDownloadingBottomSheet()
        }
    }
}
