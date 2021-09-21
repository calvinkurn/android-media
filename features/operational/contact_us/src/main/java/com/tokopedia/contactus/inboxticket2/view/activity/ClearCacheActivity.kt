package com.tokopedia.contactus.inboxticket2.view.activity

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.R
import com.tokopedia.dialog.DialogUnify

class ClearCacheActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showDialogClearCache()
    }

    private fun showDialogClearCache() {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.contact_us_clear_cache_warning))
        dialog.setDescription(getString(R.string.contact_us_clear_cache_message))
        dialog.setPrimaryCTAText(getString(R.string.contact_us_label_clear_cache_ok))
        dialog.setPrimaryCTAClickListener {
            clearCache()
        }
        dialog.setSecondaryCTAText(getString(R.string.contact_us_label_clear_cache_cancel))
        dialog.setSecondaryCTAClickListener {
            finish()
        }
        dialog.show()
    }

    private fun clearCache() {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE)
        if (activityManager is ActivityManager) {
            activityManager.clearApplicationUserData()
        }
    }
}
