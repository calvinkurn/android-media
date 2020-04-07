package com.tokopedia.shop.pageheader.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.info.view.activity.ShopInfoActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent> {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
        const val PATH_INFO = "info"

        @JvmStatic
        fun createIntent(context: Context, shopId: String, shopRef: String) = Intent(context, ShopPageActivity::class.java)
                .apply {
                    putExtra(SHOP_ID, shopId)
                    putExtra(SHOP_REF, shopRef)
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        checkIfAppLinkToShopInfo()
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_shop_page
    }

    override fun getNewFragment(): Fragment? {
        return ShopPageFragment.createInstance()
    }

    override fun getComponent(): ShopComponent = ShopComponentInstance.getComponent(application)

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopPageFragment)?.onBackPressed()
    }

    private fun checkIfAppLinkToShopInfo() {
        intent?.data?.let {
            if (it.lastPathSegment.orEmpty() == PATH_INFO) {
                val shopId = it.pathSegments.getOrNull(1) ?: ""
                val intent = getShopInfoIntent(this).putExtra(SHOP_ID, shopId)
                startActivity(intent)
            }
        }
    }

    private fun getShopInfoIntent(context: Context): Intent {
        return Intent(context, ShopInfoActivity::class.java)
    }
}