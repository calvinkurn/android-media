package com.tokopedia.shop.search.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment

class ShopSearchProductActivity : BaseSimpleActivity(), HasComponent<ShopComponent> {

    companion object {
        const val KEY_SHOP_INFO_CACHE_MANAGER_ID = "keyShopInfoCacheManagerId"
        const val KEY_SHOP_ATTRIBUTION = "keyShopAttribution"
        fun createIntent(context: Context, cacheManagerId: String, shopAttribution: String?): Intent {
            val intent = Intent(context, ShopSearchProductActivity::class.java)
            intent.putExtra(KEY_SHOP_INFO_CACHE_MANAGER_ID, cacheManagerId)
            intent.putExtra(KEY_SHOP_ATTRIBUTION, shopAttribution)
            return intent
        }
    }

    private var shopInfoCacheManagerId: String = ""

    private var shopAttribution: String = ""

    private var component: ShopComponent? = null

    private fun getIntentData() {
        intent?.run {
            shopInfoCacheManagerId = getStringExtra(KEY_SHOP_INFO_CACHE_MANAGER_ID).orEmpty()
            shopAttribution = getStringExtra(KEY_SHOP_ATTRIBUTION).orEmpty()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = ShopSearchProductFragment.createInstance(
            shopInfoCacheManagerId,
            shopAttribution
    )

    override fun getComponent(): ShopComponent = component
            ?: ShopComponentInstance.getComponent(application)

    override fun getLayoutRes() = R.layout.activity_shop_search_product
}
