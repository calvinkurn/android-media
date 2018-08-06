package com.tokopedia.product.edit.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.view.fragment.ProductDraftAddFragment
import com.tokopedia.product.edit.view.fragment.ProductDraftEditFragment.Companion.DRAFT_PRODUCT_ID

open class ProductDraftAddActivity : BaseProductAddEditActivity() {

    override fun getNewFragment(): Fragment {
        val draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, java.lang.Long.MIN_VALUE)
        return ProductDraftAddFragment.createInstance(draftProductId)
    }

    override fun setupFragment(savedInstance: Bundle?) {
        val draftProductId = intent.getLongExtra(DRAFT_PRODUCT_ID, java.lang.Long.MIN_VALUE)
        if (draftProductId < 0) {
            Toast.makeText(this, getString(R.string.product_draft_error_cannot_load_draft), Toast.LENGTH_LONG).show()
            finish()
            return
        }
        if (savedInstance == null) {
            inflateFragment()
        }
    }

    override fun getCancelMessageRes(): Int {
        return R.string.product_draft_dialog_cancel_message
    }

    override fun needDeleteCacheOnBack(): Boolean {
        return false
    }

    companion object {

        fun createInstance(context: Context, draftProductId: Long): Intent {
            val intent = Intent(context, ProductDraftAddActivity::class.java)
            intent.putExtra(DRAFT_PRODUCT_ID, draftProductId)
            return intent
        }
    }
}