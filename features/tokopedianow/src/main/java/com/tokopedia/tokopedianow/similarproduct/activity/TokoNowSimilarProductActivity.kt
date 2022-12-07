package com.tokopedia.tokopedianow.similarproduct.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.similarproduct.listener.SimilarProductListener
import com.tokopedia.tokopedianow.similarproduct.fragment.TokoNowSimilarProductFragment

class TokoNowSimilarProductActivity: BaseTokoNowActivity() {

    companion object {
        const val EXTRA_SIMILAR_PRODUCT_ID = "extra_similar_product_id"
        const val EXTRA_SIMILAR_PRODUCT_LISTENER = "extra_similar_product_listener"

        fun createNewIntent(context: Context, products: String, listener: SimilarProductListener?): Intent {
            return Intent(context, TokoNowSimilarProductActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_PRODUCT_ID, products)
                putExtra(EXTRA_SIMILAR_PRODUCT_LISTENER, listener)
            }
        }
    }

    override fun getFragment(): Fragment {
        val products = intent?.getStringExtra(EXTRA_SIMILAR_PRODUCT_ID)
        val listener = intent?.getSerializableExtra(EXTRA_SIMILAR_PRODUCT_LISTENER) as? SimilarProductListener
        return TokoNowSimilarProductFragment.newInstance(products).apply {
            setListener(listener)
        }
    }
}
