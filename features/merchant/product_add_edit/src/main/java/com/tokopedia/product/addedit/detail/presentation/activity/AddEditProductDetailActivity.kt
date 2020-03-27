package com.tokopedia.product.addedit.detail.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.di.AddEditProductDetailModule
import com.tokopedia.product.addedit.detail.di.DaggerAddEditProductDetailComponent
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment

class AddEditProductDetailActivity : BaseSimpleActivity(), HasComponent<AddEditProductDetailComponent> {

    override fun getNewFragment(): Fragment? {
        val initialSelectedImagePathList = intent?.getStringArrayListExtra(ImagePickerActivity.PICKER_RESULT_PATHS)
        return AddEditProductDetailFragment.createInstance(initialSelectedImagePathList)
    }

    override fun getComponent(): AddEditProductDetailComponent {
        return DaggerAddEditProductDetailComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductDetailModule(AddEditProductDetailModule())
                .build()
    }
}