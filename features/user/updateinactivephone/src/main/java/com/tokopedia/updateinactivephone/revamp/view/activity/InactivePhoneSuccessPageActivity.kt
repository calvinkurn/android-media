package com.tokopedia.updateinactivephone.revamp.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.revamp.common.FragmentTransactionInterface
import com.tokopedia.updateinactivephone.revamp.common.IOnBackPressed
import com.tokopedia.updateinactivephone.revamp.common.replaceFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneOnboardingFragment
import com.tokopedia.updateinactivephone.revamp.view.fragment.InactivePhoneUploadDataFragment
import kotlinx.android.synthetic.main.activity_inactive_phone_succcess_page.*

class InactivePhoneSuccessPageActivity : BaseSimpleActivity() {

    override fun getLayoutRes(): Int = R.layout.activity_inactive_phone_succcess_page

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)
        updateTitle("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imgBack?.setOnClickListener {
            startActivity(InactivePhoneOnboardingActivity.getIntent(this))
        }

        btnGotoHome?.setOnClickListener {
            val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
            startActivity(intent)
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, InactivePhoneSuccessPageActivity::class.java)
        }
    }
}
