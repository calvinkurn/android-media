package com.tokopedia.product.manage.item.main.draft.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment.Companion.CACHE_MANAGER_ID
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment.Companion.DRAFT_PRODUCT_ID

class ProductDraftEditActivity : ProductDraftAddActivity() {

    private var draftProductId = 0L
    private var cacheManagerId = ""
    override var addEditPageType: AddEditPageType = AddEditPageType.DRAFT_EDIT
    override fun getNewFragment(): Fragment {
        return ProductDraftEditFragment.createInstance(draftProductId, cacheManagerId)
    }

    override fun setupFragment(savedInstance: Bundle?) {
        draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, java.lang.Long.MIN_VALUE)
        cacheManagerId = intent.getStringExtra(CACHE_MANAGER_ID) ?: ""
        if (draftProductId < 0 && cacheManagerId.isEmpty()) {
            Toast.makeText(this, getString(R.string.product_draft_error_cannot_load_draft), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (savedInstance == null) {
            inflateFragment()
        }
    }

    companion object {
        fun createInstance(context: Context, draftProductId: Long) = Intent(context, ProductDraftEditActivity::class.java).apply {
            putExtra(DRAFT_PRODUCT_ID, draftProductId)
        }

        fun createInstance(context: Context, cacheManagerId: String) = Intent(context, ProductDraftEditActivity::class.java).apply {
            putExtra(CACHE_MANAGER_ID, cacheManagerId)
        }
    }

}