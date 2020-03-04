package com.tokopedia.product.manage.feature.list.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment
import com.tokopedia.product.manage.oldlist.view.activity.ProductManageActivity
import com.tokopedia.remoteconfig.RemoteConfigKey.ENABLE_OLD_PRODUCT_MANAGE
import com.tokopedia.sellerhomedrawer.presentation.view.BaseSellerReceiverDrawerActivity

class ProductManageActivity : BaseSellerReceiverDrawerActivity(), HasComponent<ProductManageListComponent> {

    companion object {
        private const val MANAGE_PRODUCT = 8
        private const val SCREEN_NAME = "Store - Manage product"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        goToOldProductManageIfEnabled()
        super.onCreate(savedInstanceState)
        if (!GlobalConfig.isSellerApp()) {
            setupLayout(savedInstanceState)
        }
    }

    override fun getNewFragment(): Fragment? {
        return ProductManageSellerFragment()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    public override fun onStart() {
        super.onStart()
        checkLogin()
    }

    override fun onBackPressed() {
        goToSellerAppDashboard()
        super.onBackPressed()
    }

    private fun checkLogin() {
        if (!userSession.isLoggedIn) {
            RouteManager.route(this, ApplinkConst.LOGIN)
            finish()
        } else if (!userSession.hasShop()) {
            RouteManager.route(this, ApplinkConst.HOME)
            finish()
        }
    }

    override fun setDrawerPosition(): Int {
        return MANAGE_PRODUCT
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun getComponent(): ProductManageListComponent {
        return ProductManageListInstance.getComponent(application)
    }

    private fun goToSellerAppDashboard() {
        if (GlobalConfig.isSellerApp()) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.SELLER_APP_DASHBOARD)
        }
    }

    private fun goToOldProductManageIfEnabled() {
        if(remoteConfig.getBoolean(ENABLE_OLD_PRODUCT_MANAGE)) {
            val intent = Intent(this, ProductManageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}