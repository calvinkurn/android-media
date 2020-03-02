package com.tokopedia.product.manage.feature.stockreminder.view.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.product.manage.feature.stockreminder.constant.AppScreen
import com.tokopedia.product.manage.feature.stockreminder.di.DaggerStockReminderComponent
import com.tokopedia.product.manage.feature.stockreminder.view.fragment.StockReminderFragment
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

    override fun getNewFragment(): Fragment? {
        var productId = 0L
        val uri = intent.data
        if (uri != null) {
            val segments = uri.pathSegments
            productId = segments[segments.size - 1].toLong()
        }
        return StockReminderFragment.createInstance(productId)
    }

    override fun getScreenName(): String = AppScreen.SCREEN_STOCK_REMINDER

    override fun onStart() {
        super.onStart()
        checkLogin()
    }

    private fun checkLogin() {
        userSession.let {
            if(!it.isLoggedIn) {
                RouteManager.route(this, ApplinkConst.LOGIN)
                finish()
            }else if(!it.hasShop()) {
                RouteManager.route(this, ApplinkConst.HOME)
                finish()
            }
        }
    }

}
