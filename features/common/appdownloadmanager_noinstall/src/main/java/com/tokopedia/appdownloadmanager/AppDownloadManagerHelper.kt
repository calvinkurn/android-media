package com.tokopedia.appdownloadmanager

import android.app.Activity
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadingBottomSheet
import com.tokopedia.appdownloadmanager_common.presentation.dialog.AppFileManagerDialog
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper
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
                    onBoardingBottomSheet.setOnDismissListener {
                        setCacheExpire()
                    }
                    onBoardingBottomSheet.showBottomSheet(it.supportFragmentManager)
                }
            }
        }
    }

    override fun onSuccessDownloaded(fileName: String) {
        activityRef.get()?.let {
            AppFileManagerDialog.showDialog(
                it,
                fileName,
                onSuccessDownload = {
                    openDownloadDir()
                }
            )
        }
    }

    fun startDownloadApk() {
    }

    private fun openDownloadDir() {
        launch(Dispatchers.Main) {
            activityRef.get()?.let {
                val intent = getIntentOpenDownloadDir(it)

                try {
                    it.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    private fun getIntentOpenDownloadDir(context: Context): Intent {
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        val folder = File(downloadsFolder, TOKOPEDIA_APK_PATH)
        if (!folder.exists()) folder.mkdirs()

        val downloadUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider",
                folder
            )
        } else {
            Uri.fromFile(folder)
        }

        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        } else {
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(downloadUri, DocumentsContract.Document.MIME_TYPE_DIR)

                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main
}
