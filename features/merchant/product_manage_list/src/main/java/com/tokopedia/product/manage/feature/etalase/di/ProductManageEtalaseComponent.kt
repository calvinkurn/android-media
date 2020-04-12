package com.tokopedia.product.manage.feature.etalase.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.etalase.view.fragment.EtalasePickerFragment
import dagger.Component

@ProductManageEtalaseScope
@Component(
    modules = [ProductManageEtalaseModule::class],
    dependencies = [ProductManageComponent::class]
)
interface ProductManageEtalaseComponent {
    fun inject(view: EtalasePickerFragment)
}