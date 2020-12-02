package com.tokopedia.product.manage.common.feature.quickedit.stock.di

import com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import dagger.Component

@ProductManageQuickEditStockScope
@Component(dependencies = [com.tokopedia.product.manage.common.di.ProductManageCommonComponent::class])
interface ProductManageQuickEditStockComponent {
    fun inject(view: ProductManageQuickEditStockFragment)
}