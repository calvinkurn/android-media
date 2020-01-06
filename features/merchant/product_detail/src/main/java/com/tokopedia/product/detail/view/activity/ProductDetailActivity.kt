package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.constant.DeeplinkConstant
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey


/**
 * For navigating to this class
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL or
 * @see ApplinkConstInternalMarketplace.PRODUCT_DETAIL_DOMAIN
 */
class ProductDetailActivity : BaseSimpleActivity(), HasComponent<ProductDetailComponent> {

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_DOMAIN = "shop_domain"
        private const val PARAM_PRODUCT_KEY = "product_key"
        private const val PARAM_IS_FROM_DEEPLINK = "is_from_deeplink"
        private const val IS_FROM_EXPLORE_AFFILIATE = "is_from_explore_affiliate"
        private const val PARAM_TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val PARAM_TRACKER_LIST_NAME = "tracker_list_name"
        private const val PARAM_AFFILIATE_STRING = "aff"

        private const val AFFILIATE_HOST = "affiliate"

        @JvmStatic
        fun createIntent(context: Context, productUrl: String) =
                Intent(context, ProductDetailActivity::class.java).apply {
                    data = Uri.parse(productUrl)
                }

        @JvmStatic
        fun createIntent(context: Context, shopDomain: String, productKey: String) = Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(PARAM_SHOP_DOMAIN, shopDomain)
            putExtra(PARAM_PRODUCT_KEY, productKey)
        }

        @JvmStatic
        fun createIntent(context: Context, productId: Int) = Intent(context, ProductDetailActivity::class.java).apply {
            putExtra(PARAM_PRODUCT_ID, productId.toString())
        }
    }

    private var isFromDeeplink = false
    private var isFromAffiliate = false
    private var shopDomain: String? = null
    private var productKey: String? = null
    private var productId: String? = null
    private var warehouseId: String? = null
    private var trackerAttribution: String? = null
    private var trackerListName: String? = null
    private var affiliateString: String? = null
    private var deeplinkUrl: String? = null

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(applicationContext)
    }

    object DeeplinkIntents {
        @DeepLink(ApplinkConst.PRODUCT_INFO)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            return RouteManager.getIntent(context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    uri.lastPathSegment) ?: Intent()
        }

        @DeepLink(ApplinkConst.AFFILIATE_PRODUCT)
        @JvmStatic
        fun getAffiliateIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            val intent = RouteManager.getIntent(context,
                    ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    uri.lastPathSegment) ?: Intent()
            intent.putExtra(IS_FROM_EXPLORE_AFFILIATE, true)
            return intent
        }
    }

    fun goToHomePageClicked() {
        if (isTaskRoot) {
            RouteManager.route(this, ApplinkConst.HOME)
        } else {
            onBackPressed()
        }
        finish()
    }

    override fun getScreenName(): String {
        return "" // need only on success load data? (it needs custom dimension)
    }

    override fun getNewFragment(): Fragment =
            if (remoteConfig.getBoolean(RemoteConfigKey.ANDROID_MAIN_APP_ENABLED_OLD_PDP, false)) {
                ProductDetailFragment.newInstance(productId, warehouseId, shopDomain,
                        productKey, isFromDeeplink,
                        isFromAffiliate, trackerAttribution,
                        trackerListName, affiliateString, deeplinkUrl)
            } else {
                DynamicProductDetailFragment.newInstance(productId, warehouseId, shopDomain,
                        productKey, isFromDeeplink,
                        isFromAffiliate, trackerAttribution,
                        trackerListName, affiliateString, deeplinkUrl)
            }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        isFromDeeplink = intent.getBooleanExtra(PARAM_IS_FROM_DEEPLINK, false)
        val uri = intent.data
        val bundle = intent.extras
        bundle?.let {
            warehouseId = it.getString("warehouse_id")
        }
        if (uri != null) {
            deeplinkUrl = generateApplink(uri.toString())
            if (uri.scheme == DeeplinkConstant.SCHEME_INTERNAL) {
                val segmentUri = uri.pathSegments
                if (segmentUri.size == 2) {
                    productId = uri.lastPathSegment
                } else {
                    shopDomain = segmentUri[segmentUri.size - 2]
                    productKey = segmentUri[segmentUri.size - 1]
                }
            } else if (uri.pathSegments.size >= 2 && // might be tokopedia.com/
                    uri.host != AFFILIATE_HOST) {
                val segmentUri = uri.pathSegments
                if (segmentUri.size > 1) {
                    shopDomain = segmentUri[segmentUri.size - 2]
                    productKey = segmentUri[segmentUri.size - 1]
                }
            } else { // affiliate
                productId = uri.lastPathSegment
            }
            trackerAttribution = uri.getQueryParameter(PARAM_TRACKER_ATTRIBUTION)
            trackerListName = uri.getQueryParameter(PARAM_TRACKER_LIST_NAME)
            affiliateString = uri.getQueryParameter(PARAM_AFFILIATE_STRING)
        }
        bundle?.let {
            if (productId.isNullOrBlank()) {
                productId = it.getString(PARAM_PRODUCT_ID)
            }
            if (shopDomain.isNullOrBlank()) {
                shopDomain = it.getString(PARAM_SHOP_DOMAIN)
            }
            if (productKey.isNullOrBlank()) {
                productKey = it.getString(PARAM_PRODUCT_KEY)
            }
            if (trackerAttribution.isNullOrBlank()) {
                trackerAttribution = it.getString(PARAM_TRACKER_ATTRIBUTION)
            }
            if (trackerListName.isNullOrBlank()) {
                trackerListName = it.getString(PARAM_TRACKER_LIST_NAME)
            }
            if (affiliateString.isNullOrBlank()) {
                affiliateString = it.getString(PARAM_AFFILIATE_STRING)
            }
        }
        isFromAffiliate = if (uri != null && uri.host == AFFILIATE_HOST) {
            true
        } else {
            intent.getBooleanExtra(IS_FROM_EXPLORE_AFFILIATE, false)
        }

        super.onCreate(savedInstanceState)
    }

    private fun generateApplink(applink: String): String {
        return if (applink.contains(getString(R.string.internal_scheme))) {
            applink.replace(getString(R.string.internal_scheme), "tokopedia")
        } else {
            ""
        }
    }
}