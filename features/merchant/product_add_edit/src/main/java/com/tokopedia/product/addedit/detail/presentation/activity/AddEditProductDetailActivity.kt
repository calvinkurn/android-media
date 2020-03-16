package com.tokopedia.product.addedit.detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment

class AddEditProductDetailActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val initialSelectedImagePathList = intent?.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
        return AddEditProductDetailFragment.createInstance(initialSelectedImagePathList)
    }
}