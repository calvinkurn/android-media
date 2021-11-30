package com.tokopedia.shop.search.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment

class ShopSearchProductActivity : BaseSimpleActivity(), HasComponent<ShopComponent> {

    companion object {
        const val KEY_SHOP_ATTRIBUTION = "keyShopAttribution"
        const val KEY_KEYWORD = "keyKeyword"
        private const val KEY_SHOP_ID = "SHOP_ID"
        private const val KEY_SHOP_NAME = "SHOP_NAME"
        private const val KEY_IS_OFFICIAL = "IS_OFFICIAL"
        private const val KEY_IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT"
        const val KEY_SORT_ID = "keySortId"
        const val KEY_SHOP_REF = "shopRef"

        @JvmStatic
        fun createIntent(
                context: Context,
                shopId: String,
                shopName: String,
                isOfficial: Boolean,
                isGoldMerchant: Boolean,
                keyword: String,
                shopAttribution: String?,
                shopRef: String
        ): Intent {
            val intent = Intent(context, ShopSearchProductActivity::class.java)
            intent.putExtra(KEY_SHOP_ATTRIBUTION, shopAttribution)
            intent.putExtra(KEY_SHOP_ID, shopId)
            intent.putExtra(KEY_SHOP_NAME, shopName)
            intent.putExtra(KEY_IS_OFFICIAL, isOfficial)
            intent.putExtra(KEY_IS_GOLD_MERCHANT, isGoldMerchant)
            intent.putExtra(KEY_KEYWORD, keyword)
            intent.putExtra(KEY_SHOP_REF, shopRef)
            return intent
        }

        @JvmStatic
        fun createIntent(
                context: Context,
                shopId: String,
                shopName: String,
                isOfficial: Boolean,
                isGoldMerchant: Boolean,
                keyword: String,
                shopAttribution: String?,
                sortId: String,
                shopRef: String
        ): Intent {
            val intent = createIntent(context, shopId, shopName, isOfficial, isGoldMerchant, keyword, shopAttribution, shopRef)
            intent.putExtra(KEY_SORT_ID, sortId)
            return intent
        }
    }

    private var shopId: String = ""
    private var shopName: String = ""
    private var isOfficial: Boolean = false
    private var isGold: Boolean = false
    private var shopAttribution: String = ""
    private var keyword: String = ""

    private var shopRef: String = ""
    private var component: ShopComponent? = null

    private fun getIntentData() {
        intent?.run {
            shopId = getStringExtra(KEY_SHOP_ID).orEmpty()
            shopName = getStringExtra(KEY_SHOP_NAME).orEmpty()
            isOfficial = getBooleanExtra(KEY_IS_OFFICIAL, false)
            isGold = getBooleanExtra(KEY_IS_GOLD_MERCHANT, false)
            shopAttribution = getStringExtra(KEY_SHOP_ATTRIBUTION).orEmpty()
            keyword = getStringExtra(KEY_KEYWORD).orEmpty()
            shopRef = getStringExtra(KEY_SHOP_REF).orEmpty()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun getNewFragment() = ShopSearchProductFragment.createInstance(
            shopId,
            shopName,
            isOfficial,
            isGold,
            keyword,
            shopAttribution,
            shopRef
    )

    override fun getComponent(): ShopComponent = component
            ?: ShopComponentHelper().getComponent(application, this)

    override fun getLayoutRes() = R.layout.activity_shop_search_product

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }
}
