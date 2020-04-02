package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment.Companion.EXTRA_PRODUCT_ID


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    private var productId = ""

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance(productId)
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_preview

    override fun onCreate(savedInstanceState: Bundle?) {
        intent.getStringExtra(EXTRA_PRODUCT_ID)?.run {
            productId = this
        }
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductPreviewActivity::class.java)
    }
}
