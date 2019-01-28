package com.tokopedia.product.detail.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductParams
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment

class ProductDetailActivity: BaseSimpleActivity(), HasComponent<ProductDetailComponent> {
    private var uriData: Uri? = null
    private var bundleData: Bundle = Bundle()
    private var isFromDeeplink = false

    companion object {
        private const val PARAM_URL = "url"
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PARAM_SHOP_DOMAIN = "shop_domain"
        private const val PARAM_PRODUCT_KEY = "product_key"
        private const val PARAM_PRODUCT_PRICE = "product_price"
        private const val PARAM_IS_FROM_DEEPLINK = "is_from_deeplink"

        @JvmStatic
        fun createIntent(context: Context, productUrl: String) = Intent(context, ProductDetailActivity::class.java).apply {
            val bundle = Bundle()
            val uri = Uri.parse(productUrl)
            val segmentUri = uri.pathSegments
            bundle.putString(PARAM_URL, productUrl)
            if (segmentUri.size > 1){
                bundle.putString(PARAM_SHOP_DOMAIN, segmentUri[0])
                bundle.putString(PARAM_PRODUCT_KEY, segmentUri[1])
            }
            putExtras(bundle)
            data = uri
        }
    }

    override fun getNewFragment(): Fragment = ProductDetailFragment
            .newInstance(bundleData.getString(PARAM_PRODUCT_ID),
                    bundleData.getString(PARAM_PRODUCT_KEY),
                    bundleData.getString(PARAM_SHOP_DOMAIN), isFromDeeplink)

    private fun createProductParams(uriData: Uri?, bundleData: Bundle?): ProductParams {
        return if (bundleData != null) {
            ProductParams(
                    bundleData.getString(PARAM_PRODUCT_ID),
                    bundleData.getString(PARAM_SHOP_DOMAIN),
                    bundleData.getString(PARAM_PRODUCT_KEY),
                    bundleData.getString(PARAM_PRODUCT_PRICE)
            )
        } else {
            val uriSegments = uriData?.pathSegments
            ProductParams().apply {
                uriSegments?.let {
                    if (it.size >= 2){
                        productName = uriSegments[1]
                        shopDomain = uriSegments[0]
                    }
                }
            }
        }
    }

    override fun getComponent(): ProductDetailComponent = DaggerProductDetailComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        bundleData = intent.extras
        isFromDeeplink = intent.getBooleanExtra(PARAM_IS_FROM_DEEPLINK, false)
        if(intent.data != null)
            uriData = intent.data

        super.onCreate(savedInstanceState)
    }
}