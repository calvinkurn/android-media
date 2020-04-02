package com.tokopedia.product.manage.feature.list.view.activity

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment

class ProductManageActivity : BaseSimpleActivity(), HasComponent<ProductManageListComponent> {

    companion object {
        private const val SCREEN_NAME = "Store - Manage product"
    }

    private val productManageSellerFragment by lazy {
        val uri = intent.data
        val filterId = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_FILTER).orEmpty()
        return@lazy if (filterId.isNotBlank()) {
            ProductManageSellerFragment.newInstance(arrayListOf(filterId))
        } else {
            ProductManageSellerFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!GlobalConfig.isSellerApp()) {
            setupLayout(savedInstanceState)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }
    }

    override fun getNewFragment(): Fragment? {
        return productManageSellerFragment
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun getComponent(): ProductManageListComponent {
        return ProductManageListInstance.getComponent(application)
    }
}