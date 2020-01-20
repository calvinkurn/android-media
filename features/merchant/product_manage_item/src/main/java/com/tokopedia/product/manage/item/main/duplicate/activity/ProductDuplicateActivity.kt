package com.tokopedia.product.manage.item.main.duplicate.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import com.tokopedia.product.manage.item.R
import com.tokopedia.product.manage.item.common.util.AddEditPageType
import com.tokopedia.product.manage.item.main.base.view.activity.BaseProductAddEditActivity
import com.tokopedia.product.manage.item.main.duplicate.fragment.ProductDuplicateFragment
import com.tokopedia.product.manage.item.main.edit.view.fragment.ProductEditFragment.Companion.EDIT_PRODUCT_ID

class ProductDuplicateActivity : BaseProductAddEditActivity() {

    override var addEditPageType: AddEditPageType = AddEditPageType.DUPLICATE
    override fun getCancelMessageRes() = R.string.product_draft_dialog_cancel_message

    override fun needDeleteCacheOnBack() = true

    override fun getNewFragment(): Fragment {
        val productId = intent.getStringExtra(EDIT_PRODUCT_ID)
        return ProductDuplicateFragment.createInstance(productId)
    }

    override fun setupFragment(savedInstance: Bundle?) {
        val productId = intent.getStringExtra(EDIT_PRODUCT_ID)
        if (TextUtils.isEmpty(productId)) {
            throw RuntimeException("Product id is not selected")
        }
        if (savedInstance == null) {
            inflateFragment()
        }

    }

    companion object {

        fun createInstance(context: Context, productId: String) = Intent(context, ProductDuplicateActivity::class.java).apply {
                putExtra(EDIT_PRODUCT_ID, productId)
            }
    }
}