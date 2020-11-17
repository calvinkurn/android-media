package com.tokopedia.product.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.view.activity.ProductInstallmentActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.info.view.ProductFullDescriptionActivity
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet
import com.tokopedia.product.warehouse.di.ProductWarehouseModule
import dagger.Component

@ProductDetailScope
@Component(modules = [
    ProductDetailModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class,
    ProductWarehouseModule::class,
    ProductWishlistModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailComponent {
    fun inject(fragment: DynamicProductDetailFragment)
    fun inject(activity: ProductInstallmentActivity)
    fun inject(bottomSheetFragment: AddToCartDoneBottomSheet)
    fun inject(activity: ProductFullDescriptionActivity)
    fun inject(productDetailBottomSheet: ProductDetailInfoBottomSheet)
}