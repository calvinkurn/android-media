package com.tokopedia.product.manage.item.main.draft.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditActivity
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftAddFragment
import com.tokopedia.product.manage.item.main.draft.view.fragment.ProductDraftEditFragment.Companion.DRAFT_PRODUCT_ID

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

    override fun getCancelMessageRes() = R.string.product_draft_dialog_cancel_message

    override fun needDeleteCacheOnBack() = false

    companion object {

        fun createInstance(context: Context, draftProductId: Long) = Intent(context, ProductDraftAddActivity::class.java).apply {
                putExtra(DRAFT_PRODUCT_ID, draftProductId)
            }
    }
}