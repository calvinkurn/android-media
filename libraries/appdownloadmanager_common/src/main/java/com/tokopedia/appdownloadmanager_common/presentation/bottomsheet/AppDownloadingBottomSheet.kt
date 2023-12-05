package com.tokopedia.appdownloadmanager_common.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingState
import com.tokopedia.appdownloadmanager_common.presentation.viewmodel.DownloadManagerViewModel
import com.tokopedia.appdownloadmanager_common.screen.DownloadManagerDownloadingScreen
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject

class AppDownloadingBottomSheet : BottomSheetUnify(),
    HasComponent<DownloadManagerComponent> {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(DownloadManagerViewModel::class.java)
    }

    private var onSuccessDownloaded: ((filename: String) -> Unit)? = null

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
                val downloadingManagerState by viewModel.downloadingState.collectAsState(initial = null)

                downloadingManagerState?.let {
                    when (it) {
                        is DownloadingState.Downloading -> {
                            DownloadManagerDownloadingScreen(downloadingProgressUiModel = it.downloadingProgressUiModel, onCancelClick = {
                                viewModel.cancelDownload()
                            })
                        }

                        is DownloadingState.DownloadSuccess -> {
                            DownloadManagerDownloadingScreen(downloadingProgressUiModel = it.downloadingProgressUiModel, onCancelClick = {
                                viewModel.cancelDownload()
                            })

                            onSuccessDownloaded?.invoke(it.fileName)
                        }

                        is DownloadingState.DownloadFailed -> {
                            dismissAllowingStateLoss()
                        }

                    }
                }
            }
        }

        setChild(composeView)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((context?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    override fun onDestroy() {
        onSuccessDownloaded = null
        super.onDestroy()
    }

    fun showBottomSheet(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    fun onSuccessDownloaded(onSuccessDownloaded: (filename: String) -> Unit) {
        this.onSuccessDownloaded = onSuccessDownloaded
    }

    companion object {
        const val DOWNLOAD_MANAGER_EXPIRED_TIME = "expired_time"
        const val DOWNLOAD_MANAGER_TIMESTAMP = "timestamp"
        private val TAG = AppDownloadingBottomSheet::class.java.simpleName

        fun newInstance(): AppDownloadingBottomSheet {
            return AppDownloadingBottomSheet()
        }
    }
}
