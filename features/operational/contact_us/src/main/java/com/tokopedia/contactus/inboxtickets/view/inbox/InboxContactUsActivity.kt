package com.tokopedia.contactus.inboxtickets.view.inbox

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst.INBOX_TICKET
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.view.inbox.InboxContactUsFragment.Companion.FLAG_FROM_TOKOPEDIA_HELP
import com.tokopedia.kotlin.extensions.orFalse

class InboxContactUsActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, InboxContactUsActivity::class.java)
            intent.data = "$INBOX_TICKET?$FLAG_FROM_TOKOPEDIA_HELP=true".toUri()
            context.startActivity(intent)
        }
    }


    override fun getNewFragment() : Fragment {
        val isFromTokopediaInboxPage= intent.data?.getBooleanQueryParameter(FLAG_FROM_TOKOPEDIA_HELP, false).orFalse()
        return InboxContactUsFragment.newInstance(isFromTokopediaInboxPage)
    }
    override fun getLayoutRes() = R.layout.contact_us_activity_inbox
    override fun getParentViewResourceID() = R.id.container

    @Deprecated("Deprecated in Java")
    @SuppressLint("DeprecatedMethod")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
