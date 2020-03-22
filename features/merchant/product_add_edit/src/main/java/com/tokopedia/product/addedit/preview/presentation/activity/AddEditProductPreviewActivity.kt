package com.tokopedia.product.addedit.preview.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent
import com.tokopedia.product.addedit.preview.presentation.fragment.AddEditProductPreviewFragment

class AddEditProductPreviewActivity : BaseSimpleActivity(), HasComponent<AddEditProductPreviewComponent> {

    override fun getNewFragment(): Fragment? {
        return AddEditProductPreviewFragment.createInstance()
    }

    override fun getComponent(): AddEditProductPreviewComponent {
        return DaggerAddEditProductPreviewComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductPreviewActivity::class.java)
    }
}
