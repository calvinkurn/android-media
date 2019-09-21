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

    companion object{
        const val SHOP_INFO_CACHE_MANAGER_ID_KEY = "shop_info_cache_manager_id_key"
        fun  createIntent(context: Context, cacheManagerId: String): Intent {
            val intent = Intent(context, ShopSearchProductActivity::class.java)
            intent.putExtra(SHOP_INFO_CACHE_MANAGER_ID_KEY,cacheManagerId)
            return intent
        }
    }

    private var shopInfoCacheManagerId : String = ""

    private var component: ShopComponent? = null

    private fun getIntentData() {
        intent?.run {
            shopInfoCacheManagerId = getStringExtra(SHOP_INFO_CACHE_MANAGER_ID_KEY)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = ShopSearchProductFragment.createInstance(shopInfoCacheManagerId)

    override fun getComponent(): ShopComponent = component
            ?: ShopComponentInstance.getComponent(application)

    override fun getLayoutRes() = R.layout.activity_shop_search_product
}
