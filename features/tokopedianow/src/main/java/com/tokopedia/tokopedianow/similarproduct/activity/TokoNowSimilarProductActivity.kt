package com.tokopedia.tokopedianow.similarproduct.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.similarproduct.fragment.TokoNowSimilarProductFragment

class TokoNowSimilarProductActivity: BaseTokoNowActivity() {

    companion object {
        private var listener: ProductItemListener? = null
        const val EXTRA_SIMILAR_PRODUCT_ID = "extra_similar_product_list"

        fun createNewIntent(context: Context, products: String, listener: ProductItemListener?): Intent {
            this.listener = listener
            return Intent(context, TokoNowSimilarProductActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_PRODUCT_ID, products)
            }
        }
    }

    override fun getFragment(): Fragment {
        val products = intent
            ?.getStringExtra(EXTRA_SIMILAR_PRODUCT_ID)
        return TokoNowSimilarProductFragment.newInstance(products).apply {
            setListener(listener)
        }
    }
}
