package com.tokopedia.appdownloadmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import androidx.fragment.app.FragmentActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadingBottomSheet
import com.tokopedia.appdownloadmanager_common.presentation.dialog.AppFileManagerDialog
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper
import java.lang.ref.WeakReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.CoroutineContext

class AppDownloadManagerHelper(
    activityRef: WeakReference<Activity>
) : BaseDownloadManagerHelper(activityRef), DownloadManagerSuccessListener, CoroutineScope {
    override fun showAppDownloadManagerBottomSheet() {
        launch {
            if (isEnableShowBottomSheet()) {
                (activityRef.get() as? FragmentActivity)?.let {
                    val onBoardingBottomSheet = AppDownloadingBottomSheet.newInstance()
                    downloadManagerUpdateModel?.let { downloadManagerUpdate ->
                        onBoardingBottomSheet.setDownloadManagerUpdate(downloadManagerUpdate)
                    }
                    appVersionBetaInfoModel?.let { appVersionBetaInfoModel ->
                        onBoardingBottomSheet.setAppBetaVersionInfoModel(appVersionBetaInfoModel)
                    }
                    onBoardingBottomSheet.setAppDownloadListener(
                        startAppDownloading = {
                            setCacheExpire()
                        },
                        this@AppDownloadManagerHelper
                    )
                    onBoardingBottomSheet.showBottomSheet(it.supportFragmentManager)
                }
            }
        }
    }

    override fun onSuccessDownloaded(fileName: String) {
        activityRef.get()?.let {
            AppFileManagerDialog.showDialog(it, fileName,
                onSuccessDownload = {
                    openDownloadDir()
                }
            )
        }
    }

    private fun openDownloadDir() {
        launch(Dispatchers.Main) {
            activityRef.get()?.let {
                val intent = getIntentOpenDownloadDir()

                try {
                    it.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    private fun getIntentOpenDownloadDir(): Intent {
        val uri = Uri.parse(TKPD_DOWNLOAD_APK_DIR)

        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            }

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Default
}
