package com.tokopedia.product.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.product.detail.view.activity.ProductDetailActivity
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet
import dagger.Component

@ProductDetailScope
@Component(modules = [
    ProductDetailModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class,
    ProductDetailUserModule::class,
    ProductDetailFragmentModule::class,
    ProductDetailDevModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailComponent {

    fun inject(activity: ProductDetailActivity)
    fun inject(fragment: DynamicProductDetailFragment)
    fun inject(bottomSheetFragment: AddToCartDoneBottomSheet)
    fun inject(productDetailBottomSheet: ProductDetailInfoBottomSheet)
}
