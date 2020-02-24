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
        const val KEY_KEYWORD = "keyKeyword"
        const val KEY_SORT_ID = "keySortId"
        const val KEY_SHOP_REF = "shopRef"

        @JvmStatic
        fun createIntent(context: Context, keyword: String, cacheManagerId: String, shopAttribution: String?, shopRef: String): Intent {
            val intent = Intent(context, ShopSearchProductActivity::class.java)
            intent.putExtra(KEY_SHOP_INFO_CACHE_MANAGER_ID, cacheManagerId)
            intent.putExtra(KEY_SHOP_ATTRIBUTION, shopAttribution)
            intent.putExtra(KEY_KEYWORD, keyword)
            intent.putExtra(KEY_SHOP_REF, shopRef)
            return intent
        }

        @JvmStatic
        fun createIntent(context: Context, keyword: String, cacheManagerId: String,
                         shopAttribution: String?, sortId: String, shopRef: String): Intent {
            val intent = createIntent(context, keyword, cacheManagerId, shopAttribution, shopRef)
            intent.putExtra(KEY_SORT_ID, sortId)
            return intent
        }
    }

    private var shopInfoCacheManagerId: String = ""

    private var shopAttribution: String = ""

    private var keyword: String = ""

    private var sortId: String = ""

    private var shopRef: String = ""

    private var component: ShopComponent? = null

    private fun getIntentData() {
        intent?.run {
            shopInfoCacheManagerId = getStringExtra(KEY_SHOP_INFO_CACHE_MANAGER_ID).orEmpty()
            shopAttribution = getStringExtra(KEY_SHOP_ATTRIBUTION).orEmpty()
            keyword = getStringExtra(KEY_KEYWORD).orEmpty()
            sortId = getStringExtra(KEY_SORT_ID).orEmpty()
            shopRef = getStringExtra(KEY_SHOP_REF).orEmpty()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment() = ShopSearchProductFragment.createInstance(
            keyword,
            shopInfoCacheManagerId,
            shopAttribution,
            sortId,
            shopRef
    )

    override fun getComponent(): ShopComponent = component
            ?: ShopComponentInstance.getComponent(application)

    override fun getLayoutRes() = R.layout.activity_shop_search_product
}
