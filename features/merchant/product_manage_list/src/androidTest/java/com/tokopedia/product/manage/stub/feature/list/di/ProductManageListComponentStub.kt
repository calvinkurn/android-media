package com.tokopedia.product.manage.stub.feature.list.di

import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.di.ProductManageListModule
import com.tokopedia.product.manage.feature.list.di.ProductManageListScope
import com.tokopedia.product.manage.stub.common.di.component.ProductManageComponentStub
import dagger.Component

@Component(
    dependencies = [ProductManageComponentStub::class],
    modules = [ProductManageListModule::class]
)
@ProductManageListScope
interface ProductManageListComponentStub: ProductManageListComponent