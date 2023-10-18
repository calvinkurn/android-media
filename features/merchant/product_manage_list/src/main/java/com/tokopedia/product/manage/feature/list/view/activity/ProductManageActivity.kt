package com.tokopedia.product.manage.feature.list.view.activity

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.product.manage.common.util.ProductManageConfig
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment

open class ProductManageActivity : BaseSimpleActivity(), HasComponent<ProductManageListComponent> {

    companion object {
        private const val SCREEN_NAME = "Store - Manage product"
    }

    private val productManageSellerFragment by lazy {
        val uri = intent.data
        val filterId = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_FILTER).orEmpty()
        val searchKeyword = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_SEARCH).orEmpty()
        val tab = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_TAB).orEmpty()

        return@lazy when {
            filterId.isNotBlank() || searchKeyword.isNotBlank() || tab.isNotBlank() -> {
                if (filterId.isEmpty()){
                    ProductManageSellerFragment.newInstance(arrayListOf(), tab, searchKeyword)
                }else{
                    ProductManageSellerFragment.newInstance(arrayListOf(filterId), tab, searchKeyword)
                }
            }
            else -> {
                ProductManageSellerFragment()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()

        if (!ProductManageConfig.IS_SELLER_APP) {
            window.decorView.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment? {
        return productManageSellerFragment
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onBackPressed() {
        goToSellerAppDashboard()
        super.onBackPressed()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun getComponent(): ProductManageListComponent {
        return ProductManageListInstance.getComponent(this)
    }

    private fun goToSellerAppDashboard() {
        if (ProductManageConfig.IS_SELLER_APP) {
            RouteManager.route(this, ApplinkConstInternalMarketplace.SELLER_APP_DASHBOARD)
        }
    }
}
