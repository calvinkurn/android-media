package com.tokopedia.product.manage.feature.quickedit.stock.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import dagger.Component

@ProductManageQuickEditStockScope
@Component(dependencies = [ProductManageComponent::class])
interface ProductManageQuickEditStockComponent {
    fun inject(view: ProductManageQuickEditStockFragment)
}