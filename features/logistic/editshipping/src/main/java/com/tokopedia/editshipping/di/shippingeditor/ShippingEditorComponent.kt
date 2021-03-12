package com.tokopedia.editshipping.di.shippingeditor

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorActivity
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorFragment
import dagger.Component

@ShippingEditorScope
@Component(modules = [ShippingEditorModule::class, ShippingEditorViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ShippingEditorComponent {
    fun inject(shippingEditorFragment: ShippingEditorFragment)
}