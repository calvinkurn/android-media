package com.tokopedia.contactus.switcheractivity.ticket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.R
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity as InboxTicketDetail
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity as InboxDetail
import com.tokopedia.contactus.switcheractivity.RemoteConfigSetting
import com.tokopedia.kotlin.extensions.orFalse

class TicketSwitcherActivity : BaseSimpleActivity() {

    companion object {
        const val BUNDLE_ID_TICKET = "id_ticket"
        const val IS_OFFICIAL_STORE = "is_official_store"

        @JvmStatic
        fun start(context: Context, idTicket: String, isOfficialStore: Boolean): Intent {
            val intent = Intent(context, InboxDetail::class.java)
            val bundle = Bundle()
            bundle.putString(BUNDLE_ID_TICKET, idTicket)
            bundle.putBoolean(IS_OFFICIAL_STORE, isOfficialStore)
            intent.putExtras(bundle)
            return intent
        }
    }

    private val ticketId by lazy {
        intent?.extras?.getString(BUNDLE_ID_TICKET).orEmpty()
    }

    private val isOfficialStore by lazy {
        intent?.extras?.getBoolean(IS_OFFICIAL_STORE, false).orFalse()
    }

    override fun getNewFragment(): Fragment? = null
    override fun getLayoutRes() = R.layout.contact_us_activity_inbox_detail
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (RemoteConfigSetting.isRemoteConfigGoesToMVVM(this)) {
            startActivity(
                InboxTicketDetail.getIntent(
                    this,
                    ticketId,
                    isOfficialStore
                )
            )
            finish()
        } else {
            startActivity(InboxDetail.getIntent(this, ticketId, isOfficialStore))
            finish()
        }
    }
}
