package com.tokopedia.attachinvoice.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.attachinvoice.R
import com.tokopedia.attachinvoice.di.AttachInvoiceComponent
import com.tokopedia.attachinvoice.di.DaggerAttachInvoiceComponent
import com.tokopedia.attachinvoice.view.fragment.AttachInvoiceFragment

class AttachInvoiceActivity : BaseSimpleActivity(), HasComponent<AttachInvoiceComponent>,
        AttachInvoiceFragment.Listener {

    override fun getNewFragment(): Fragment? {
        val messageId = intent.getStringExtra(ApplinkConst.AttachInvoice.PARAM_MESSAGE_ID) ?: ""
        val opponentName = intent.getStringExtra(ApplinkConst.AttachInvoice.PARAM_OPPONENT_NAME) ?: ""
        return AttachInvoiceFragment.createInstance(messageId, opponentName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLightNotificationBar()
        setupToolbar()
    }

    override fun getComponent(): AttachInvoiceComponent {
        return DaggerAttachInvoiceComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    private fun setupToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        supportActionBar?.setTitle(R.string.title_attachinvoice)
        toolbar.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }
    }

    override fun onClickAttachInvoice(intent: Intent) {
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
