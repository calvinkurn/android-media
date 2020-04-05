package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_DRAFT_ID
import com.tokopedia.product.addedit.preview.presentation.constant.AddEditProductPreviewConstants.Companion.EXTRA_PRODUCT_ID
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment

class AddEditProductPreviewActivity : BaseSimpleActivity() {

    private var productId = ""
    private var draftId = ""

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance(productId, draftId)
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_preview

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getStringExtra(EXTRA_PRODUCT_ID)?.run { productId = this }
        intent.getStringExtra(EXTRA_DRAFT_ID)?.run { draftId = this }
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductPreviewActivity::class.java)
    }
}
