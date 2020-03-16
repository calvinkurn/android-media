package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment

class AddEditProductPreviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance()
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductPreviewActivity::class.java)
    }
}
