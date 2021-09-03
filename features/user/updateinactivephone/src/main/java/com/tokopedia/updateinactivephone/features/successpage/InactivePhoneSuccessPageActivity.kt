package com.tokopedia.updateinactivephone.features.successpage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.InactivePhoneConstant
import com.tokopedia.updateinactivephone.common.utils.getValidEmail
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.features.InactivePhoneTracker
import kotlinx.android.synthetic.main.activity_inactive_phone_succcess_page.*

class InactivePhoneSuccessPageActivity : BaseSimpleActivity() {

    private var tracker = InactivePhoneTracker()
    private var inactivePhoneUserDataModel: InactivePhoneUserDataModel? = null

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

        intent?.extras?.let {
            inactivePhoneUserDataModel = it.getParcelable(InactivePhoneConstant.PARAM_USER_DATA)
        }

        btnGotoHome?.setOnClickListener {
            tracker.clickOnButtonGotoHome()
            gotoHome()
        }

        val desc = if (inactivePhoneUserDataModel?.email?.getValidEmail()?.isEmpty() == true) {
            String.format(getString(R.string.text_success_description_has_no_email), inactivePhoneUserDataModel?.newPhoneNumber)
        } else {
            String.format(getString(R.string.text_success_description_single_account), inactivePhoneUserDataModel?.email?.getValidEmail(), inactivePhoneUserDataModel?.newPhoneNumber)
        }

        textDescription?.text = MethodChecker.fromHtml(desc)
        showTicker()
    }

    private fun showTicker() {
        tickerInactivePhoneNumber?.apply {
            setHtmlDescription(getString(R.string.text_success_ticker))
        }?.show()
    }

    override fun onBackPressed() {
        gotoHome()
    }

    private fun gotoHome() {
        val intent = RouteManager.getIntent(this, ApplinkConst.HOME)
        startActivity(intent)
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, InactivePhoneSuccessPageActivity::class.java)
        }
    }
}
