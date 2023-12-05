package com.tokopedia.appdownloadmanager_common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.nakamaupdate.DownloadManagerUpdateModel
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission
import com.tokopedia.appdownloadmanager_common.presentation.viewmodel.DownloadManagerViewModel
import com.tokopedia.appdownloadmanager_common.screen.DownloadManagerOnboardingScreen
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AppDownloadOnboardingBottomSheet : BottomSheetUnify(),
    HasComponent<DownloadManagerComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DownloadManagerViewModel::class.java)
    }

    private var downloadManagerUpdateModel: DownloadManagerUpdateModel? = null

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
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                DownloadManagerOnboardingScreen(
                    downloadManagerUpdateModel = downloadManagerUpdateModel,
                    onDownloadClick = {
                        activity?.let { mActivity ->
                            AppDownloadManagerPermission.checkAndRequestPermission(mActivity) {
                                if (it) {
                                    showStartDownloading()
                                }
                            }
                        }
                    }
                )
            }
        }

        showKnob = true

        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
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
                showStartDownloading()
            }
        }
    }

    override fun onDestroy() {
        startAppDownloading = null
        downloadManagerSuccessListener = null
        super.onDestroy()
    }

    private fun startDownloadingAndShowBottomSheet() {
        viewModel.startDownload(APK_URL)
        startAppDownloading?.invoke()
        showStartDownloading()

        dismiss()
    }

    private fun showStartDownloading() {
        val appDownloadingBottomSheet = AppDownloadingBottomSheet.newInstance()
        appDownloadingBottomSheet.onSuccessDownloaded {
            downloadManagerSuccessListener?.onSuccessDownloaded(
                it
            )
        }
        appDownloadingBottomSheet.showBottomSheet(childFragmentManager)
    }

    fun setDownloadManagerUpdate(downloadManagerUpdateModel: DownloadManagerUpdateModel) {
        this.downloadManagerUpdateModel = downloadManagerUpdateModel
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

    companion object {
        val TAG = AppDownloadOnboardingBottomSheet::class.java.simpleName

        const val APK_URL =
            "https://docs-android.tokopedia.net/downloadApk?packagename=com.tokopedia.tkpd&versionname=3.246"

        fun newInstance(): AppDownloadOnboardingBottomSheet {
            return AppDownloadOnboardingBottomSheet()
        }
    }
}
