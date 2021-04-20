package com.tokopedia.chatbot.attachinvoice.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.fragment.AttachInvoiceFragment

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoiceActivity : BaseSimpleActivity(), AttachInvoiceContract.Activity {

    override val userId: String
        get() = intent.getStringExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY)

    override val messageId: Int
        get() {
            var msgId = 0
            if (intent.hasExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY)) {
                msgId = intent.getIntExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY, 0)
            }
            return msgId
        }

    override fun getNewFragment(): Fragment {
        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(tagFragment)
        if (fragment != null) {
            return fragment
        } else {
            fragment = AttachInvoiceFragment.newInstance(this)
            return fragment
        }
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        title = getString(R.string.string_attach_invoice_activity_title);
        super.setupLayout(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar?.setBackgroundDrawable(resources.getDrawable(com.tokopedia.design.R.drawable
                    .bg_white_toolbar_drop_shadow))
            supportActionBar?.setHomeAsUpIndicator(resources.getDrawable(com.tokopedia.design.R.drawable
                    .ic_close_default));
            toolbar.setSubtitleTextAppearance(this, com.tokopedia.chat_common.R.style.AttachProductToolbarSubTitle_SansSerif);
            toolbar.setTitleTextAppearance(this, com.tokopedia.chat_common.R.style.AttachProductToolbarTitle_SansSerif);
        }
    }


    companion object {
        var TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY = "ATTACH_INVOICE_USER_ID"
        var TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY = "ATTACH_INVOICE_MSG_ID"

        fun createInstance(context: Context, userId: String, messageId: Int): Intent {
            val intent = Intent(context, AttachInvoiceActivity::class.java)
            intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY, userId)
            intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY, messageId)
            return intent
        }
    }
}
