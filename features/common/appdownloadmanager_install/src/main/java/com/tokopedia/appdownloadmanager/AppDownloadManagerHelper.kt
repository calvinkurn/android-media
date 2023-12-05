package com.tokopedia.appdownloadmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadOnboardingBottomSheet
import com.tokopedia.appdownloadmanager_common.presentation.listener.DownloadManagerSuccessListener
import com.tokopedia.appdownloadmanager_common.presentation.util.BaseDownloadManagerHelper
import java.io.File
import java.lang.ref.WeakReference

class AppDownloadManagerHelper(
    activityRef: WeakReference<Activity>,
) : BaseDownloadManagerHelper(activityRef), DownloadManagerSuccessListener {
    override fun showAppDownloadManagerBottomSheet() {
        if (isEnableShowBottomSheet()) {
            (activityRef.get() as? FragmentActivity)?.let {
                val onBoardingBottomSheet = AppDownloadOnboardingBottomSheet.newInstance()
                downloadManagerUpdateModel?.let { downloadManagerUpdate ->
                    onBoardingBottomSheet.setDownloadManagerUpdate(downloadManagerUpdate)
                }
                onBoardingBottomSheet.setAppDownloadListener(
                    startAppDownloading = {
                        setCacheExpire()
                    }, this
                )
                onBoardingBottomSheet.showBottomSheet(it.supportFragmentManager)
            }
        }
    }

    override fun onSuccessDownloaded(fileName: String) {
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
}
