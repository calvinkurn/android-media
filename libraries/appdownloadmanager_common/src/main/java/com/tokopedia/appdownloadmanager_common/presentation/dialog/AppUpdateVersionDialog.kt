package com.tokopedia.appdownloadmanager_common.presentation.dialog

import android.app.Activity
import android.app.DownloadManager
import androidx.fragment.app.FragmentActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
import com.tokopedia.unifycomponents.Toaster
import timber.log.Timber
import java.lang.RuntimeException
import java.lang.ref.WeakReference
import javax.inject.Inject
import com.tokopedia.appdownloadmanager_common.R as appdownloadmanager_commonR
import com.tokopedia.unifycomponents.R as unifycomponentsR

class AppUpdateVersionDialog(
    val activityRef: WeakReference<Activity>,
    val downloadManagerUpdateModel: DownloadManagerUpdateModel? = null,
    val appVersionBetaInfoModel: AppVersionBetaInfoModel? = null,
    val onSuccessDownload: (fileNamePath: String) -> Unit,
    val onFailDownload: (reason: String) -> Unit,
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
            val errorMessage = if (statusColumn == DownloadManager.ERROR_INSUFFICIENT_SPACE) {
                activityRef.get()?.getString(appdownloadmanager_commonR.string.app_download_error_insufficient_message)
            } else {
                activityRef.get()?.getString(appdownloadmanager_commonR.string.app_download_error_network_message)
            }.orEmpty()

            val apkUrl = APK_URL.format(appVersionBetaInfoModel?.versionName, appVersionBetaInfoModel?.versionCode)

            onFailDownload(errorMessage)

            FirebaseCrashlytics.getInstance().recordException(RuntimeException("status: $statusColumn | reason: $reason | apkUrl: $apkUrl"))
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

                val description =
                    downloadManagerUpdateModel?.dialogText?.format(apkName) ?: it.getString(
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

                setOnDismissListener {
                    setCacheExpire()
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

        showStartDownloadToaster()
    }

    private fun showStartDownloadToaster() {
        val view = activityRef.get()?.window?.decorView?.rootView
        val message = activityRef.get()?.getString(appdownloadmanager_commonR.string.update_app_version_toaster_install).orEmpty()

        view?.let {
            val toaster = Toaster

            try {
                activityRef.get()?.let { activityRef ->
                    toaster.toasterCustomBottomHeight =
                        activityRef.resources.getDimensionPixelSize(unifycomponentsR.dimen.layout_lvl6)
                }
            } catch (t: Throwable) {
                Timber.d(t)
            }

            toaster.build(
                it,
                message,
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL,
                actionText = activityRef.get()?.getString(appdownloadmanager_commonR.string.update_app_version_toaster_oke).orEmpty()
            ).show()
        }
    }
}
