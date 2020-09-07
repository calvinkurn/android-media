package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.updateinactivephone.R
import kotlinx.android.synthetic.main.activity_inactive_phone_succcess_page.*

class InactivePhoneSuccessPageActivity : BaseSimpleActivity() {

    override fun getLayoutRes(): Int = R.layout.activity_inactive_phone_succcess_page

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getToolbarResourceID(): Int = R.id.toolbarInactivePhone

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle("")
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_inactive_phone)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            elevation = 0f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnGotoHome?.setOnClickListener {
            gotoHome()
        }

        textDescription?.text = MethodChecker.fromHtml(getString(R.string.text_success_description_single_account))
    }

    override fun onBackPressed() {
        gotoHome()
    }

    private fun gotoHome() {
        val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
        startActivity(intent)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, InactivePhoneSuccessPageActivity::class.java)
        }
    }
}
