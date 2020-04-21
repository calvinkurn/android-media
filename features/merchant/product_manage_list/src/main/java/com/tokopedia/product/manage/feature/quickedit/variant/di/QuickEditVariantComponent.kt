package com.tokopedia.product.manage.feature.quickedit.variant.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantPriceBottomSheet
import com.tokopedia.product.manage.feature.quickedit.variant.presentation.ui.QuickEditVariantStockBottomSheet
import dagger.Component

@QuickEditVariantScope
@Component(
    dependencies = [ProductManageComponent::class],
    modules = [QuickEditVariantViewModelModule::class]
)
interface QuickEditVariantComponent {
    fun inject(view: QuickEditVariantPriceBottomSheet)
    fun inject(view: QuickEditVariantStockBottomSheet)
}