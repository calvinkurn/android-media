package com.tokopedia.appdownloadmanager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.dialog.AppUpdateVersionDialog
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class AppDownloadManagerHelper(
    activityRef: WeakReference<Activity>
) : BaseDownloadManagerHelper(activityRef), DownloadManagerSuccessListener, CoroutineScope {

    private var updateAppVersionDialog: AppUpdateVersionDialog? = null

    override fun showAppDownloadManagerBottomSheet() {
        launch {
            if (isEnableShowBottomSheet()) {
                if (updateAppVersionDialog == null) {
                     updateAppVersionDialog = AppUpdateVersionDialog(activityRef)
                }
                appVersionBetaInfoModel?.let {
                    updateAppVersionDialog?.showDialog(it, ::onSuccessDownloaded, ::onFailedDownload)
                }
            }
        }
    }

    override fun onSuccessDownloaded(fileName: String) {
        installApk(fileName)
    }

    private fun installApk(fileName: String) {
        activityRef.get()?.let {
            val file = File(fileName)
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    it,
                    it.applicationContext.packageName + ".provider",
                    file
                )
            } else {
                Uri.fromFile(file)
            }
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, APK_MIME_TYPE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }

            try {
                it.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun onFailedDownload(reason: String, statusColumn: Int) {
        showErrorToaster(reason)
    }

    private fun showErrorToaster(reason: String) {
        val view = activityRef.get()?.window?.decorView?.rootView

        view?.let {
            Toaster.build(
                it,
                reason,
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
}
