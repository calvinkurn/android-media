package com.tokopedia.product.detail.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.common_sdk_affiliate_toko.di.AffiliateCommonSdkModule
import com.tokopedia.product.detail.view.fragment.DynamicProductDetailFragment
import com.tokopedia.product.detail.view.widget.AddToCartDoneBottomSheet
import com.tokopedia.product.info.view.bottomsheet.ProductDetailInfoBottomSheet
import dagger.Component

@ProductDetailScope
@Component(modules = [
    ProductDetailModule::class,
    ViewModelModule::class,
    GqlRawQueryModule::class,
    ProductWishlistModule::class,
    ProductDetailUserModule::class,
    ProductDetailDevModule::class,
    AffiliateCommonSdkModule::class],
        dependencies = [BaseAppComponent::class])
interface ProductDetailComponent {
    fun inject(fragment: DynamicProductDetailFragment)
    fun inject(bottomSheetFragment: AddToCartDoneBottomSheet)
    fun inject(productDetailBottomSheet: ProductDetailInfoBottomSheet)
}