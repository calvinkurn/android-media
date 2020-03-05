package com.tokopedia.product.addedit.preview.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment

class AddEditProductPreviewActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance()
    }
}
