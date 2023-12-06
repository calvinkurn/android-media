package com.tokopedia.appdownloadmanager_common.presentation.dialog

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.appdownloadmanager_common.R as appdownloadmanager_commonR
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.domain.model.AppVersionBetaInfoModel
import com.tokopedia.appdownloadmanager_common.domain.service.DownloadManagerService
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.APK_URL
import com.tokopedia.dialog.DialogUnify
import java.lang.ref.WeakReference
import javax.inject.Inject

class AppUpdateVersionDialog(
    val activityRef: WeakReference<Activity>
) : HasComponent<DownloadManagerComponent> {

    @Inject
    lateinit var downloadManagerService: DownloadManagerService

    init {
        component.inject(this)
    }

    private var dialog: DialogUnify? = null

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((activityRef.get()?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    fun showDialog(
        appVersionBetaInfoModel: AppVersionBetaInfoModel,
        onSuccessDownload: (fileNamePath: String) -> Unit,
        onFailedDownload: (reason: String, statusColumn: Int) -> Unit,
    ) {
        val apkName = appVersionBetaInfoModel.versionName

        val context = activityRef.get() as? FragmentActivity

        context?.let {
            if (dialog == null) {
                dialog = DialogUnify(
                    it,
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE
                )
                dialog?.run {
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)

                    setTitle(
                        it.getString(
                            appdownloadmanager_commonR.string.dialog_update_app_version_title
                        )
                    )
                    setDescription(
                        MethodChecker.fromHtml(
                            it.getString(
                                appdownloadmanager_commonR.string.dialog_update_app_version_desc,
                                apkName
                            )
                        )
                    )

                    setPrimaryCTAText(it.getString(appdownloadmanager_commonR.string.dialog_update_app_version_btn_primary))
                    setSecondaryCTAText(it.getString(appdownloadmanager_commonR.string.dialog_update_app_version_btn_secondary))

                    setPrimaryCTAClickListener {
                        startDownloadApk(
                            appVersionBetaInfoModel,
                            onSuccessDownload,
                            onFailedDownload,
                        )
                        dismiss()
                    }

                    setSecondaryCTAClickListener {
                        dismiss()
                    }
                }

                if (dialog?.isShowing == false) {
                    dialog?.show()
                }
            }
        }
    }

    private fun startDownloadApk(
        appVersionBetaInfoModel: AppVersionBetaInfoModel,
        onSuccessDownload: (fileNamePath: String) -> Unit,
        onFailedDownload: (reason: String, statusColumn: Int) -> Unit,
    ) {
        val apkUrl =
            APK_URL.format(appVersionBetaInfoModel.versionName, appVersionBetaInfoModel.versionCode)

        downloadManagerService.startDownload(
            apkUrl,
            object : DownloadManagerService.DownloadManagerListener {
                override suspend fun onFailedDownload(reason: String, statusColumn: Int) {
                    onFailedDownload(reason, statusColumn)
                }

                override suspend fun onDownloading(downloadingProgressUiModel: DownloadingProgressUiModel) {
                    //no op
                }

                override suspend fun onSuccessDownload(
                    downloadingProgressUiModel: DownloadingProgressUiModel,
                    fileNamePath: String
                ) {
                    onSuccessDownload.invoke(fileNamePath)
                }
            }
        )
    }

}
