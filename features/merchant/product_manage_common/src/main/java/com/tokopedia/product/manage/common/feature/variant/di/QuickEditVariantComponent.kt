package com.tokopedia.product.manage.common.feature.variant.di

import com.tokopedia.product.manage.common.di.ProductManageCommonComponent
import com.tokopedia.product.manage.common.feature.variant.presentation.ui.QuickEditVariantBottomSheet
import dagger.Component

@QuickEditVariantScope
@Component(
    dependencies = [ProductManageCommonComponent::class],
    modules = [QuickEditVariantViewModelModule::class]
)
interface QuickEditVariantComponent {
    fun inject(view: QuickEditVariantBottomSheet)
}