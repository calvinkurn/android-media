package com.tokopedia.contactus.inboxtickets.view.inboxdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.contactus.R
import com.tokopedia.kotlin.extensions.orFalse

class InboxDetailActivity : BaseSimpleActivity() {

    companion object {
        const val BUNDLE_ID_TICKET = "id_ticket"
        const val IS_OFFICIAL_STORE = "is_official_store"

        @JvmStatic
        fun getIntent(context: Context, idTicket: String, isOfficialStore: Boolean): Intent {
            val intent = Intent(context, InboxDetailActivity::class.java)
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
        intent?.extras?.getBoolean(IS_OFFICIAL_STORE, false)
    }

    override fun getLayoutRes() = R.layout.contact_us_activity_inbox_detail
    override fun getParentViewResourceID() = R.id.container

    override fun getNewFragment(): Fragment {
        return TicketFragment.newInstance(ticketId, isOfficialStore.orFalse())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        uri?.let {
            val paths = UriUtil.destructureUri(ApplinkConst.TICKET_DETAIL, it)
            if (paths.isNotEmpty()) {
                val ticketId = paths[0]
                intent.putExtra(BUNDLE_ID_TICKET, ticketId)
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_us_activity_inbox_detail)
        initOnBackPressCallback()
    }

    private fun initOnBackPressCallback() {
        onBackPressedDispatcher.addCallback(this, getBackPressCallback())
    }
    private fun getBackPressCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragment = supportFragmentManager.findFragmentById(R.id.container)
                (fragment as BackTicketListener).onDeviceBackPress()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment !is BackTicketListener || !(fragment as BackTicketListener).onMenuBackPress()) {
                val fragment = supportFragmentManager.findFragmentById(R.id.container)
                (fragment as BackTicketListener).onDeviceBackPress()
            }
            true
        } else {
            false
        }
    }
}
