package com.tokopedia.shop.pageheader.presentation.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.shop.R
import com.tokopedia.shop.common.config.ShopPageConfig
import com.tokopedia.shop.info.view.activity.ShopInfoActivity
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.APP_LINK_EXTRA_SHOP_ATTRIBUTION
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.APP_LINK_EXTRA_SHOP_ID
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.EXTRA_STATE_TAB_POSITION
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_ATTRIBUTION
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.SHOP_ID
import com.tokopedia.shop.oldpage.view.activity.ShopPageActivity.Companion.TAB_POSITION_INFO

class ShopPageActivity: BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLayout(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_new_shop_page

    override fun getNewFragment(): Fragment? = null

    object DeeplinkIntents {

        @JvmStatic
        @Suppress("unused")
        @DeepLink(ApplinkConst.SHOP_INFO)
        fun getCallingIntentInfoSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            openNewShopPageIfEnabled(context)

            return getShopInfoIntent(context)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
                    .putExtras(extras)
        }

        @JvmStatic
        @Suppress("unused")
        @DeepLink(ApplinkConst.SHOP_NOTE)
        fun getCallingIntentNoteSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            openNewShopPageIfEnabled(context)

            return getShopInfoIntent(context)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
        }

        private fun getShopInfoIntent(context: Context): Intent {
            return if (isNewShopPageEnabled(context)) {
                Intent(context, ShopInfoActivity::class.java)
            } else {
                Intent(context, com.tokopedia.shop.oldpage.view.activity.ShopPageActivity::class.java)
            }
        }

        private fun openNewShopPageIfEnabled(context: Context) {
            if (isNewShopPageEnabled(context)) {
                val intent = Intent(context, ShopPageActivity::class.java)
                context.startActivity(intent)
            }
        }

        private fun isNewShopPageEnabled(context: Context): Boolean {
            val shopPageConfig = ShopPageConfig(context)
            return shopPageConfig.isNewShopPageEnabled()
        }
    }
}