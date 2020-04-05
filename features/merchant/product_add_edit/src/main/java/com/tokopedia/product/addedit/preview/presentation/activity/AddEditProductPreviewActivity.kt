package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment


class AddEditProductPreviewActivity : BaseSimpleActivity() {

    private var productId = ""

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance(productId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
//        productId = intent.getStringExtra(EXTRA_PRODUCT_ID)
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductPreviewActivity::class.java)
    }
}
