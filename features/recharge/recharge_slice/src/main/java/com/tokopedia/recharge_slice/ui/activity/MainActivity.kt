package com.tokopedia.recharge_slice.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.recharge_slice.di.DaggerRechargeSliceComponent
import com.tokopedia.recharge_slice.util.SliceTracking
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class MainActivity : AppCompatActivity() {

    lateinit var sliceTracking : SliceTracking
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInject()
         userSession = UserSession(this)
         sliceTracking = SliceTracking(userSession)

         trackingLogin()
    }

    private fun trackingLogin(){
        sliceTracking.clickLoginPage()
        sliceTracking.openLoginPage()
        RouteManager.route(this, ApplinkConst.LOGIN)
    }

    private fun initInject() {
        DaggerRechargeSliceComponent.builder().build().inject(this)
    }
}
