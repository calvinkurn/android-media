package com.tokopedia.appdownloadmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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
        openDownloadDir()
    }

    private fun openDownloadDir() {
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

    private fun getIntentOpenDownloadDir(): Intent {
        val downloadsFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val uri = Uri.fromFile(downloadsFolder)

        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
            }

            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
