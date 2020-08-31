package com.tokopedia.product.manage.feature.cashback.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.cashback.presentation.fragment.ProductManageSetCashbackFragment
import dagger.Component

@ProductManageSetCashbackScope
@Component(dependencies = [ProductManageComponent::class])
interface ProductManageSetCashbackComponent {
    fun inject(view: ProductManageSetCashbackFragment)
}