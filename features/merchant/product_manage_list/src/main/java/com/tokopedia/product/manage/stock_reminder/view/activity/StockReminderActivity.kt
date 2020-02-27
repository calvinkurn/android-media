package com.tokopedia.product.manage.stock_reminder.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.manage.stock_reminder.constant.AppScreen
import com.tokopedia.product.manage.stock_reminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.stock_reminder.view.fragment.StockReminderFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class StockReminderActivity : BaseSimpleActivity() {

    @Inject
    lateinit var userSession: UserSessionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)

        DaggerStockReminderComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun getNewFragment(): Fragment? = StockReminderFragment()

    override fun getScreenName(): String = AppScreen.SCREEN_STOCK_REMINDER

    override fun onStart() {
        super.onStart()
        checkLogin()
    }

    override fun onBackPressed() {
        //gotomanageproducthome
        super.onBackPressed()
    }

    private fun checkLogin() {
        userSession?.let {
            if(!it.isLoggedIn) {
                RouteManager.route(this, ApplinkConst.LOGIN)
                finish()
            }else if(!it.hasShop()) {
                RouteManager.route(this, ApplinkConst.HOME)
                finish()
            }
        }
    }

    private fun goToManageProductHome() {}


}
