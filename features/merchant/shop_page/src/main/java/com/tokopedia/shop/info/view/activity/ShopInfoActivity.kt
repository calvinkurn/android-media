package com.tokopedia.shop.info.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment

/**
 * Navigate to ShopInfoActivity
 * use [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_INFO]
 */
class ShopInfoActivity: BaseSimpleActivity(), HasComponent<ShopComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)
        toolbar?.setTitleTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        inflateFragment()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun getScreenName() = null

    override fun getLayoutRes() = R.layout.activity_shop_info

    override fun getComponent(): ShopComponent = ShopComponentHelper().getComponent(application, this)

    override fun getNewFragment(): Fragment? = createShopInfoFragment()

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopInfoFragment)?.onBackPressed()
    }

    private fun createShopInfoFragment(): ShopInfoFragment {
        val shopId = intent.getStringExtra(SHOP_ID) ?: intent.data?.lastPathSegment.orEmpty()
        val shopData = intent.getParcelableExtra<ShopInfoData?>(EXTRA_SHOP_INFO)

        return ShopInfoFragment.createInstance(shopId, shopData)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    companion object {
        fun createIntent(
                context: Context,
                shopId: String? = null,
                shopInfo: ShopInfoData? = null
        ): Intent {
            return Intent(context, ShopInfoActivity::class.java).apply {
                putExtra(SHOP_ID, shopId)
                putExtra(EXTRA_SHOP_INFO, shopInfo)
            }
        }

        const val EXTRA_SHOP_INFO = "extra_shop_info"
        const val SHOP_ID = "EXTRA_SHOP_ID"
    }
}