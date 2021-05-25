package com.tokopedia.product.detail.ui.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.di.*
import com.tokopedia.product.detail.ui.base.BaseProductDetailUiTest
import com.tokopedia.product.warehouse.di.ProductWarehouseModule
import dagger.Component

/**
 * Created by Yehezkiel on 08/04/21
 */

@ProductDetailScope
@Component(modules = [
    ProductDetailModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class,
    ProductWarehouseModule::class,
    ProductWishlistModule::class,
    ProductUserSessionModuleMock::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailTestComponent : ProductDetailComponent {
    fun inject(testData: BaseProductDetailUiTest)
}