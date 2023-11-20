package com.tokopedia.product.manage.feature.list.di

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageFragment
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment
import com.tokopedia.product.manage.feature.list.view.ui.bottomsheet.ProductArchivalBottomSheet
import dagger.Component

@Component(
    dependencies = [ProductManageComponent::class],
    modules = [ProductManageListModule::class]
)
@ProductManageListScope
interface ProductManageListComponent {

    fun inject(productManageFragment: ProductManageFragment)
    fun inject(productManageSellerFragment: ProductManageSellerFragment)
    fun inject(productManageSellerFragment: ProductManageActivity)
    fun inject(productArchivalBottomSheet: ProductArchivalBottomSheet)
}
