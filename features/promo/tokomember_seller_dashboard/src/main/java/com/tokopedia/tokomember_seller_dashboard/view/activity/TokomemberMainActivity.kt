package com.tokopedia.tokomember_seller_dashboard.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberMainFragment
import com.tokopedia.user.session.UserSession

class TokomemberMainActivity : BaseSimpleActivity() {

    private val REQUEST_CODE_LOGIN_TOKOMEMBER = 123

    override fun getNewFragment(): Fragment? {

        val userSession = UserSession(this)
        return if (userSession.isLoggedIn) {
            TokomemberMainFragment.newInstance()
        } else {
            val intent = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivityForResult(intent, REQUEST_CODE_LOGIN_TOKOMEMBER)
            null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_LOGIN_TOKOMEMBER){
            RouteManager.route(this, ApplinkConst.SellerApp.TOKOMEMBER)
            finish()
        }
    }
}