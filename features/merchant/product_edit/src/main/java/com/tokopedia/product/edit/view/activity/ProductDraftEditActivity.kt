package com.tokopedia.product.edit.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.product.edit.view.fragment.ProductDraftEditFragment
import com.tokopedia.product.edit.view.fragment.ProductDraftEditFragment.Companion.DRAFT_PRODUCT_ID

class ProductDraftEditActivity : ProductDraftAddActivity() {

    override fun getNewFragment(): Fragment {
        val productId = intent.getLongExtra(DRAFT_PRODUCT_ID, java.lang.Long.MIN_VALUE)
        return ProductDraftEditFragment.createInstance(productId)
    }

    companion object {

        fun createInstance(context: Context, draftProductId: Long): Intent {
            val intent = Intent(context, ProductDraftEditActivity::class.java)
            intent.putExtra(DRAFT_PRODUCT_ID, draftProductId)
            return intent
        }
    }

}