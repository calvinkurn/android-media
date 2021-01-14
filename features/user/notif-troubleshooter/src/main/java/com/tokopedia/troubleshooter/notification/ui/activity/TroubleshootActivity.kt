package com.tokopedia.troubleshooter.notification.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.fragment.TroubleshootFragment
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING as USER_NOTIFICATION_SETTING

class TroubleshootActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return TroubleshootFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_troubleshoot, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mnSettings) {
            RouteManager.route(this, USER_NOTIFICATION_SETTING)
        }
        return super.onOptionsItemSelected(item)
    }

}