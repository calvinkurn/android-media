package com.tokopedia.chatbot.attachinvoice.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.activity.AttachInvoiceActivity.Companion.TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY
import com.tokopedia.chatbot.attachinvoice.view.fragment.AttachInvoiceFragment

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoiceActivity : BaseSimpleActivity(), AttachInvoiceContract.Activity, HasComponent<*> {

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

    override fun setupLayout(savedInstanceState: Bundle) {
        //        setTitle(getString(R.string.string_attach_invoice_activity_title));
        super.setupLayout(savedInstanceState)
        //        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable
        //                .bg_white_toolbar_drop_shadow));
        //        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable
        //                .ic_close_default));
        //        toolbar.setSubtitleTextAppearance(this, R.style.AttachProductToolbarSubTitle_SansSerif);
        //        toolbar.setTitleTextAppearance(this, R.style.AttachProductToolbarTitle_SansSerif);
    }

    override fun getComponent(): BaseAppComponent? {
        //TODO NISIE
        //        DaggerTalkComponent.builder().baseAppComponent(
        //                (application as BaseMainApplication).baseAppComponent).build()
        return null
    }

    companion object {
        var TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY = "ATTACH_INVOICE_USER_ID"
        var TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY = "ATTACH_INVOICE_MSG_ID"
        val TOKOPEDIA_ATTACH_INVOICE_REQ_CODE = 114
        val TOKOPEDIA_ATTACH_INVOICE_RESULT_CODE_OK = 325
        var TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY = "SELECTED_INVOICE"

        fun createInstance(context: Context, userId: String, messageId: Int): Intent {
            val intent = Intent(context, AttachInvoiceActivity::class.java)
            intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_USER_ID_KEY, userId)
            intent.putExtra(TOKOPEDIA_ATTACH_INVOICE_MSG_ID_KEY, messageId)
            return intent
        }
    }
}
