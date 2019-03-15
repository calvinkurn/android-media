package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.detail.ProductDetailRouter
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.util.ProductDetailTracking
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
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
    private var isFromDeeplink = false
    private var isFromAffiliate = false
    private var shopDomain: String? = null
    private var productKey: String? = null
    private var productId: String? = null
    private var trackerAttribution: String? = null
    private var trackerListName: String? = null
    lateinit var remoteConfig: RemoteConfig

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_DOMAIN = "shop_domain"
        private const val PARAM_PRODUCT_KEY = "product_key"
        private const val PARAM_IS_FROM_DEEPLINK = "is_from_deeplink"
        private const val IS_FROM_EXPLORE_AFFILIATE = "is_from_explore_affiliate"
        private const val PARAM_TRACKER_ATTRIBUTION = "tracker_attribution"
        private const val PARAM_TRACKER_LIST_NAME = "tracker_list_name"

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

    object DeeplinkIntents {
        @DeepLink(ApplinkConst.PRODUCT_INFO)
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            return RouteManager.getIntent(context,
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                uri.lastPathSegment)) ?: Intent()
        }

        @DeepLink(ApplinkConst.AFFILIATE_PRODUCT)
        @JvmStatic
        fun getAffiliateIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)) ?: return Intent()
            val intent = RouteManager.getIntent(context,
                UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL,
                    uri.lastPathSegment)) ?: Intent()
            intent.putExtra(IS_FROM_EXPLORE_AFFILIATE, true)
            return intent
        }
    }

    override fun getScreenName(): String {
        return ProductDetailTracking.PRODUCT_DETAIL_SCREEN_NAME
    }

    override fun getNewFragment(): Fragment = ProductDetailFragment
        .newInstance(productId, shopDomain, productKey, isFromDeeplink, isFromAffiliate, trackerAttribution, trackerListName)

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
        .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        isFromDeeplink = intent.getBooleanExtra(PARAM_IS_FROM_DEEPLINK, false)
        val uri = intent.data
        val bundle = intent.extras
        if (uri != null) {
            if (uri.scheme == ApplinkConstInternal.INTERNAL_SCHEME) {
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
        }
        bundle?.let {
            if (productId.isNullOrEmpty()) {
                productId = it.getString(PARAM_PRODUCT_ID)
            }
            if (shopDomain.isNullOrEmpty()) {
                shopDomain = it.getString(PARAM_SHOP_DOMAIN)
            }
            if (productKey.isNullOrEmpty()) {
                productKey = it.getString(PARAM_PRODUCT_KEY)
            }
            if (trackerAttribution.isNullOrEmpty()) {
                trackerAttribution = it.getString(PARAM_TRACKER_ATTRIBUTION)
            }
            if (trackerListName.isNullOrEmpty()) {
                trackerListName = it.getString(PARAM_TRACKER_LIST_NAME)
            }
        }
        if (uri != null && uri.host == AFFILIATE_HOST) {
            isFromAffiliate = true
        } else {
            isFromAffiliate = intent.getBooleanExtra(IS_FROM_EXPLORE_AFFILIATE, false)
        }
        super.onCreate(savedInstanceState)

        remoteConfig = FirebaseRemoteConfigImpl(this)
        if (remoteConfig.getBoolean(RemoteConfigKey.MAIN_APP_DISABLE_NEW_PRODUCT_DETAIL)) {
            if (application is ProductDetailRouter) {
                (application as ProductDetailRouter).goToOldProductDetailPage(this, productId, shopDomain, productKey, trackerAttribution, trackerListName)
            }
        }
    }
}