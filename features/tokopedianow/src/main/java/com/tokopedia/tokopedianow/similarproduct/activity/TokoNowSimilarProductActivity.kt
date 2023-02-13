package com.tokopedia.tokopedianow.similarproduct.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokopedianow.common.base.activity.BaseTokoNowActivity
import com.tokopedia.tokopedianow.similarproduct.listener.TokoNowSimilarProductTrackerListener
import com.tokopedia.tokopedianow.similarproduct.fragment.TokoNowSimilarProductFragment

class TokoNowSimilarProductActivity: BaseTokoNowActivity() {

    companion object {
        const val EXTRA_SIMILAR_PRODUCT_ID = "extra_similar_product_id"
        const val EXTRA_SIMILAR_PRODUCT_LISTENER = "extra_similar_product_listener"

        fun createNewIntent(context: Context, productId: String, listener: TokoNowSimilarProductTrackerListener?): Intent {
            return Intent(context, TokoNowSimilarProductActivity::class.java).apply {
                putExtra(EXTRA_SIMILAR_PRODUCT_ID, productId)
                putExtra(EXTRA_SIMILAR_PRODUCT_LISTENER, listener)
            }
        }
    }

    override fun getFragment(): Fragment {
        val productId = intent?.getStringExtra(EXTRA_SIMILAR_PRODUCT_ID)
        val listener = intent?.getSerializableExtra(EXTRA_SIMILAR_PRODUCT_LISTENER) as? TokoNowSimilarProductTrackerListener
        return TokoNowSimilarProductFragment.newInstance(productId).apply {
            setListener(listener)
        }
    }
}
