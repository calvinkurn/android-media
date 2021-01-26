package com.tokopedia.editshipping.ui.shippingeditor

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.editshipping.di.shippingeditor.DaggerShippingEditorComponent
import com.tokopedia.editshipping.di.shippingeditor.ShippingEditorComponent

class ShippingEditorActivity : BaseSimpleActivity(), HasComponent<ShippingEditorComponent> {

    override fun getNewFragment(): Fragment? {
        return ShippingEditorFragment()
    }

    override fun getComponent(): ShippingEditorComponent {
        return DaggerShippingEditorComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}