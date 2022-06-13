package com.tokopedia.product.manage.stub.common.di.component

import com.tokopedia.product.manage.common.di.ProductManageComponent
import com.tokopedia.product.manage.common.di.ProductManageScope
import com.tokopedia.product.manage.stub.common.di.module.ProductManageModuleStub
import dagger.Component

@Component(
    modules = [ProductManageModuleStub::class],
    dependencies = [BaseAppComponentStub::class])
@ProductManageScope
interface ProductManageComponentStub: ProductManageComponent