package com.tokopedia.shop.pageheader.presentation.activity

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
import com.tokopedia.shop.common.config.ShopPageConfig
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.info.view.activity.ShopInfoActivity
import com.tokopedia.shop.pageheader.presentation.fragment.ShopPageFragment

class ShopPageActivity : BaseSimpleActivity(), HasComponent<ShopComponent> {

    companion object {
        const val SHOP_ID = "EXTRA_SHOP_ID"
        const val SHOP_REF = "EXTRA_SHOP_REF"
        const val SHOP_DOMAIN = "domain"
        const val SHOP_ATTRIBUTION = "EXTRA_SHOP_ATTRIBUTION"
        const val APP_LINK_EXTRA_SHOP_ID = "shop_id"
        const val APP_LINK_EXTRA_SHOP_REF = "shop_ref"
        const val APP_LINK_EXTRA_SHOP_ATTRIBUTION = "tracker_attribution"
        const val EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION"
        const val TAB_POSITION_OS_HOME = -1
        const val TAB_POSITION_HOME = 0
        const val TAB_POSITION_FEED = 1
        const val TAB_POSITION_INFO = 2
        const val SHOP_STATUS_FAVOURITE = "SHOP_STATUS_FAVOURITE"
        const val SHOP_TRACE = "mp_shop"
        const val SHOP_NAME_PLACEHOLDER = "{{shop_name}}"
        const val SHOP_LOCATION_PLACEHOLDER = "{{shop_location}}"
        private const val REQUEST_CODER_USER_LOGIN = 100
        private const val REQUEST_CODE_FOLLOW = 101
        private const val REQUEST_CODE_USER_LOGIN_CART = 102
        private const val VIEW_CONTENT = 1
        private const val VIEW_LOADING = 2
        private const val VIEW_ERROR = 3

        private const val PAGE_LIMIT = 2

        private const val SOURCE_SHOP = "shop"

        private const val STICKY_SHOW_DELAY: Long = 3 * 60 * 1000

        private const val CART_LOCAL_CACHE_NAME = "CART"
        private const val TOTAL_CART_CACHE_KEY = "CACHE_TOTAL_CART"

        @JvmStatic
        fun createIntent(context: Context, shopId: String, shopRef: String) = Intent(context, ShopPageActivity::class.java)
                .apply {
                    putExtra(SHOP_ID, shopId)
                    putExtra(SHOP_REF, shopRef)
                }

        private fun isNewShopPageEnabled(context: Context): Boolean {
            val shopPageConfig = ShopPageConfig(context)
            return shopPageConfig.isNewShopPageEnabled()
        }

        private fun getShopInfoIntent(context: Context): Intent {
            return if (isNewShopPageEnabled(context)) {
                Intent(context, ShopInfoActivity::class.java)
            } else {
                Intent(context, com.tokopedia.shop.oldpage.view.activity.ShopPageActivity::class.java)
            }
        }

        private fun getShopPageIntent(context: Context): Intent {
            return if (isNewShopPageEnabled(context)) {
                Intent(context, ShopPageActivity::class.java)
            } else {
                Intent(context, com.tokopedia.shop.oldpage.view.activity.ShopPageActivity::class.java)
            }
        }

        private fun openNewShopPageIfEnabled(context: Context, uri: Uri.Builder, extras: Bundle) {
            if (isNewShopPageEnabled(context)) {
                val intent = Intent(context, ShopPageActivity::class.java)
                intent.setData(uri.build())
                        .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                        .putExtra(SHOP_REF, extras.getString(APP_LINK_EXTRA_SHOP_REF))
                        .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                        .putExtras(extras)
                context.startActivity(intent)
            }
        }
    }

    object DeeplinkIntents {
        @DeepLink(ApplinkConst.SHOP)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return getShopPageIntent(context)
                    .setData(Uri.parse(extras.getString(DeepLink.URI)).buildUpon().build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_REF, extras.getString(APP_LINK_EXTRA_SHOP_REF))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                    .putExtras(extras)
        }

        @DeepLink(ApplinkConst.SHOP_HOME)
        @JvmStatic
        fun getCallingIntentHomeSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return getShopPageIntent(context)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_REF, extras.getString(APP_LINK_EXTRA_SHOP_REF))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_OS_HOME)
        }

        @JvmStatic
        @Suppress("unused")
        @DeepLink(ApplinkConst.SHOP_INFO)
        fun getCallingIntentInfoSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            openNewShopPageIfEnabled(context, uri, extras)

            return getShopInfoIntent(context)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_REF, extras.getString(APP_LINK_EXTRA_SHOP_REF))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
                    .putExtras(extras)
        }

        @JvmStatic
        @Suppress("unused")
        @DeepLink(ApplinkConst.SHOP_NOTE)
        fun getCallingIntentNoteSelected(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            openNewShopPageIfEnabled(context, uri, extras)

            return getShopInfoIntent(context)
                    .setData(uri.build())
                    .putExtra(SHOP_ID, extras.getString(APP_LINK_EXTRA_SHOP_ID))
                    .putExtra(SHOP_REF, extras.getString(APP_LINK_EXTRA_SHOP_REF))
                    .putExtra(SHOP_ATTRIBUTION, extras.getString(APP_LINK_EXTRA_SHOP_ATTRIBUTION, ""))
                    .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_INFO)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isNewShopPageEnabled(this)) {
            openOldShopPage()
            finish()
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_new_shop_page
    }

    override fun getNewFragment(): Fragment? {
        return if (isNewShopPageEnabled(this)) {
            ShopPageFragment.createInstance()
        } else {
            null
        }
    }

    override fun getComponent() = ShopComponentInstance.getComponent(application)

    override fun onBackPressed() {
        super.onBackPressed()
        (fragment as? ShopPageFragment)?.onBackPressed()
    }

    private fun openOldShopPage() {
        val oldShopPageIntent = Intent(intent)
        oldShopPageIntent.setClass(this, com.tokopedia.shop.oldpage.view.activity.ShopPageActivity::class.java)
        startActivity(oldShopPageIntent)
    }

}