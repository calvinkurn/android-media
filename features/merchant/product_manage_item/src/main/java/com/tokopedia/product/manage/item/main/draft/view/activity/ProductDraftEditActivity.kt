package com.tokopedia.product.manage.item.main.draft.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment.Companion.DRAFT_PRODUCT_ID

class ProductDraftEditActivity : ProductDraftAddActivity() {

    override var addEditPageType: AddEditPageType = AddEditPageType.DRAFT_EDIT
    override fun getNewFragment(): Fragment {
        val productId = intent.getLongExtra(DRAFT_PRODUCT_ID, java.lang.Long.MIN_VALUE)
        return ProductDraftEditFragment.createInstance(productId)
    }

    companion object {
        fun createInstance(context: Context, draftProductId: Long) = Intent(context, ProductDraftEditActivity::class.java).apply {
                putExtra(DRAFT_PRODUCT_ID, draftProductId)
            }
    }

}