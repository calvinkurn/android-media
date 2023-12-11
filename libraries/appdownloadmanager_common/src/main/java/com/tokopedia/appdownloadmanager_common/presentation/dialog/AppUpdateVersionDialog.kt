package com.tokopedia.appdownloadmanager_common.presentation.dialog

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.appdownloadmanager_common.di.component.DaggerDownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.di.component.DownloadManagerComponent
import com.tokopedia.appdownloadmanager_common.domain.model.AppVersionBetaInfoModel
import com.tokopedia.appdownloadmanager_common.domain.service.DownloadManagerService
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadingProgressUiModel
import com.tokopedia.appdownloadmanager_common.presentation.util.AppDownloadManagerPermission
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper.Companion.APK_URL
import com.tokopedia.dialog.DialogUnify
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.tokopedia.appdownloadmanager_common.R as appdownloadmanager_commonR

class AppUpdateVersionDialog(
    val activityRef: WeakReference<Activity>,
    val downloadManagerUpdateModel: DownloadManagerUpdateModel? = null,
    val appVersionBetaInfoModel: AppVersionBetaInfoModel? = null,
    val onSuccessDownload: (fileNamePath: String) -> Unit,
    val onFailDownload: (reason: String, statusColumn: Int) -> Unit,
    val setCacheExpire: () -> Unit
) : HasComponent<DownloadManagerComponent> {

    @Inject
    lateinit var downloadManagerService: DownloadManagerService

    init {
        component.inject(this)
    }

    private var dialog: DialogUnify? = null

    val downloadManagerListener = object : DownloadManagerService.DownloadManagerListener {
        override suspend fun onFailedDownload(reason: String, statusColumn: Int) {
            onFailDownload(reason, statusColumn)
        }

        override suspend fun onDownloading(downloadingProgressUiModel: DownloadingProgressUiModel) {
            // no op
        }

        override suspend fun onSuccessDownload(
            downloadingProgressUiModel: DownloadingProgressUiModel,
            fileNamePath: String
        ) {
            onSuccessDownload(fileNamePath)
        }
    }

    override fun getComponent(): DownloadManagerComponent {
        return DaggerDownloadManagerComponent.builder()
            .baseAppComponent((activityRef.get()?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    fun showDialog() {
        val apkName = appVersionBetaInfoModel?.versionName

        val context = activityRef.get() as? FragmentActivity

        context?.let {
            dialog = DialogUnify(
                it,
                actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE
            )
            dialog?.run {
                setCancelable(false)
                setCanceledOnTouchOutside(false)

                val title = downloadManagerUpdateModel?.dialogTitle ?: it.getString(
                    appdownloadmanager_commonR.string.dialog_update_app_version_title
                )

                val description = downloadManagerUpdateModel?.dialogText?.format(apkName) ?: it.getString(
                    appdownloadmanager_commonR.string.dialog_update_app_version_desc,
                    apkName
                )

                val btnPrimaryText =
                    downloadManagerUpdateModel?.dialogButtonPositive ?: it.getString(
                        appdownloadmanager_commonR.string.dialog_update_app_version_btn_primary
                    )

                val btnSecondaryText =
                    downloadManagerUpdateModel?.dialogButtonNegative
                        ?: it.getString(appdownloadmanager_commonR.string.dialog_update_app_version_btn_secondary)

                setTitle(title)
                setDescription(
                    MethodChecker.fromHtml(
                        description
                    )
                )

                setPrimaryCTAText(btnPrimaryText)
                setSecondaryCTAText(btnSecondaryText)

                setPrimaryCTAClickListener {
                    AppDownloadManagerPermission.checkAndRequestPermission(it) { hasGrantPermission ->
                        if (hasGrantPermission) {
                            appVersionBetaInfoModel?.let { appVersionBetaInfo ->
                                startDownloadApk(appVersionBetaInfo)
                            }
                        }
                    }
                    setCacheExpire()
                    dismiss()
                }

                setSecondaryCTAClickListener {
                    dismiss()
                }

                if (dialog?.isShowing == false) {
                    dialog?.show()
                }
            }
        }
    }

    private fun startDownloadApk(
        appVersionBetaInfoModel: AppVersionBetaInfoModel
    ) {
        val apkUrl =
            APK_URL.format(appVersionBetaInfoModel.versionName, appVersionBetaInfoModel.versionCode)

        downloadManagerService.startDownload(
            apkUrl,
            downloadManagerListener
        )
    }
}
