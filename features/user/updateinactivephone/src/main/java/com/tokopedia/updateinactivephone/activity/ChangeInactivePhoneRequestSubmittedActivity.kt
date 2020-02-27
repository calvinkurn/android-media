package com.tokopedia.updateinactivephone.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.core.analytics.ScreenTracking
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.IS_DUPLICATE_REQUEST
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_EMAIL
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneConstants.Constants.Companion.USER_PHONE
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventConstants
import com.tokopedia.updateinactivephone.common.analytics.UpdateInactivePhoneEventTracking

class ChangeInactivePhoneRequestSubmittedActivity : BaseSimpleActivity() {

    private var isDuplicateRequest: Boolean = false
    private var email: String? = null
    private var phone: String? = null

    private val successConfirmationScreenName: String
        get() = UpdateInactivePhoneEventConstants.Screen.SUBMIT_SUCCESS_REQUEST_PAGE

    private val waitingConfirmationScreenName: String
        get() = UpdateInactivePhoneEventConstants.Screen.WAITING_CONFIRMATION_PAGE

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun setupLayout(savedInstanceState: Bundle) {
        super.setupLayout(savedInstanceState)
        setupToolbar()
        initView()
    }

    override fun getLayoutRes(): Int {
        return R.layout.change_inactive_phone_request_submitted_layout
    }

    private fun initView() {

        if (intent.extras != null) {
            val bundle = intent.extras
            isDuplicateRequest = bundle?.getBoolean(IS_DUPLICATE_REQUEST, false) == true
            email = bundle.getString(USER_EMAIL)
            phone = bundle.getString(USER_PHONE)
        }

        val newRequestDetailLayout = findViewById<LinearLayout>(R.id.first_request_submission_details)
        val returnToHome = findViewById<Button>(R.id.button_return_to_home)
        val emailTV = findViewById<TextView>(R.id.value_email)
        val phoneTV = findViewById<TextView>(R.id.value_phone)
        val duplicateRequestTV = findViewById<TextView>(R.id.duplicate_request_view)

        if (isDuplicateRequest) {
            ScreenTracking.screen(this, waitingConfirmationScreenName)
            UpdateInactivePhoneEventTracking.eventViewWaitingForConfirmationPage(this)
            newRequestDetailLayout.visibility = View.GONE
            duplicateRequestTV.visibility = View.VISIBLE
        } else {
            ScreenTracking.screen(this, successConfirmationScreenName)
            UpdateInactivePhoneEventTracking.eventViewSubmitSuccessPage(this)
            newRequestDetailLayout.visibility = View.VISIBLE
            duplicateRequestTV.visibility = View.GONE
            emailTV.text = email
            phoneTV.text = phone
        }

        returnToHome.setOnClickListener {
            RouteManager.route(this, ApplinkConst.HOME)
            finish()
        }
    }

    private fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.navigation_cancel)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    companion object {

        fun createNewIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, ChangeInactivePhoneRequestSubmittedActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }
}
