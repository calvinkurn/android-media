package com.tokopedia.contactus.switcheractivity.inbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxticket2.view.activity.InboxListActivity.Companion.getCallingIntent
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsActivity
import com.tokopedia.contactus.switcheractivity.RemoteConfigSetting

class InboxSwitcherActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, InboxSwitcherActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getNewFragment(): Fragment? = null
    override fun getLayoutRes() = R.layout.contact_us_activity_inbox
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(RemoteConfigSetting.isRemoteConfigGoesToMVVM(this)){
            InboxContactUsActivity.start(this)
            finish()
        } else {
            startActivity(getCallingIntent(this))
            finish()
        }
    }
}
