package com.tokopedia.shop.info.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.data.model.ShopInfoData
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.info.view.fragment.ShopInfoFragment
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.APP_LINK_EXTRA_SHOP_ATTRIBUTION
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.APP_LINK_EXTRA_SHOP_ID
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_ATTRIBUTION
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_ID

/**
 * Navigate to ShopInfoActivity
 * use [com.tokopedia.applink.internal.ApplinkConstInternalMarketplace.SHOP_INFO]
 */
class ShopInfoActivity: BaseSimpleActivity(), HasComponent<ShopComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)
        inflateFragment()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        inflateFragment()
    }

    override fun getScreenName() = null

    override fun getLayoutRes() = R.layout.activity_shop_info

    override fun getComponent(): ShopComponent = ShopComponentInstance.getComponent(application)

    override fun getNewFragment(): Fragment? = createShopInfoFragment()

    private fun createShopInfoFragment(): ShopInfoFragment {
        val shopId = intent.getStringExtra(SHOP_ID) ?: intent.data?.lastPathSegment.orEmpty()
        val shopData = intent.getParcelableExtra<ShopInfoData?>(EXTRA_SHOP_INFO)

        return ShopInfoFragment.createInstance(shopId, shopData)
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
    }

    @Suppress("unused")
    object DeeplinkIntents {

        @JvmStatic
        @DeepLink(ApplinkConst.SHOP_INFO)
        fun getCallingIntentInfo(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ShopInfoActivity::class.java)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtras(extras)
        }

        @JvmStatic
        @DeepLink(ApplinkConst.SHOP_NOTE)
        fun getCallingIntentNote(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, ShopInfoActivity::class.java)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
        }
    }
}