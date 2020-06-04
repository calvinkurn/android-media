package com.tokopedia.product.addedit.common

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.addedit.common.di.AddEditProductComponent
import com.tokopedia.product.addedit.common.di.DaggerAddEditProductComponent

class AddEditProductComponentBuilder {

    companion object {
        private var addEditProductComponent: AddEditProductComponent? = null

        fun getComponent(application: Application): AddEditProductComponent {
            return addEditProductComponent?.run { addEditProductComponent }
                    ?: DaggerAddEditProductComponent.builder().baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
        }
    }
}