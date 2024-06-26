package com.tokopedia.product.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.detail.di.DaggerProductDetailComponent
import com.tokopedia.product.detail.di.ProductDetailComponent
import com.tokopedia.product.detail.view.fragment.ProductDetailFragment

/**
 * Created by Yehezkiel on 10/01/21
 * Activity mock for testing, use this if you want to adding new function on activity
 */
class ProductDetailActivityCommonTest : BaseSimpleActivity(), HasComponent<ProductDetailComponent> {

    var productId: String = ""
    private var productDetailComponent: ProductDetailComponent? = null

    companion object {
        private const val PARAM_PRODUCT_ID = "product_id"
        private const val PRODUCT_DETAIL_TAG = "productDetailTag"

        @JvmStatic
        fun createIntent(context: Context, productId: String) = Intent(context, ProductDetailActivityCommonTest::class.java).apply {
            putExtra(PARAM_PRODUCT_ID, productId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        bundle?.let {
            productId = it.getString(PARAM_PRODUCT_ID) ?: ""
        }

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int = R.layout.activity_product_detail

    override fun getParentViewResourceID(): Int {
        return R.id.product_detail_parent_view
    }

    override fun getTagFragment(): String {
        return PRODUCT_DETAIL_TAG
    }

    override fun getNewFragment(): Fragment? = ProductDetailFragment.newInstance(
        productId, "", "",
        "", false,
        trackerAttribution = "",
        trackerListName = "", affiliateString = "", deeplinkUrl = "", layoutId = ""
    )

    fun getPositionViewHolderByName(name: String): Int {
        val fragment =
            supportFragmentManager.findFragmentByTag(PRODUCT_DETAIL_TAG) as ProductDetailFragment
        return fragment.productAdapter?.currentList?.indexOfFirst {
            it.name() == name
        } ?: 0
    }

    fun getAdapterTotalSize(): Int {
        val fragment =
            supportFragmentManager.findFragmentByTag(PRODUCT_DETAIL_TAG) as ProductDetailFragment
        return fragment.productAdapter?.currentList?.size ?: 0
    }

    override fun getComponent(): ProductDetailComponent {
        return productDetailComponent ?: initializeComponent()
    }

    private fun initializeComponent(): ProductDetailComponent {
        val baseComponent = (applicationContext as BaseMainApplication).baseAppComponent
        return DaggerProductDetailComponent.builder()
            .baseAppComponent(baseComponent)
            .build()
    }

}
