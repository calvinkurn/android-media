package com.tokopedia.troubleshooter.notification.util

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.troubleshooter.notification.R

object ClearCacheUtil {

    fun showClearCache(context: Context?) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(context.getString(R.string.notif_clear_cache_warning))
            dialog.setDescription(context.getString(R.string.notif_clear_cache_message))
            dialog.setPrimaryCTAText(context.getString(R.string.notif_label_clear_cache_ok))
            dialog.setPrimaryCTAClickListener {
                clearCache(context)
            }
            dialog.setSecondaryCTAText(context.getString(R.string.notif_label_clear_cache_cancel))
            dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun clearCache(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE)
            if (activityManager is ActivityManager) {
                activityManager.clearApplicationUserData()
            }
        } else {
            try {
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear ${context?.packageName}")
            } catch (e: Exception) {}
        }
    }

}