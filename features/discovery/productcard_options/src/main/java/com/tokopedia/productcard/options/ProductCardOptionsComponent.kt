package com.tokopedia.productcard.options

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.productcard.options.di.OnBoardingModule
import com.tokopedia.productcard.options.di.ProductCardOptionsContextModule
import com.tokopedia.productcard.options.di.ProductCardOptionsFragmentModule
import com.tokopedia.productcard.options.di.ProductCardOptionsScope
import com.tokopedia.productcard.options.di.RemoteConfigModule
import com.tokopedia.productcard.options.di.SimilarSearchLocalCacheModule
import dagger.Component

@ProductCardOptionsScope
@Component(modules = [
    ProductCardOptionsContextModule::class,
    ProductCardOptionsViewModelFactoryModule::class,
    SimilarSearchLocalCacheModule::class,
    RemoteConfigModule::class,
    ProductCardOptionsFragmentModule::class,
    OnBoardingModule::class,
], dependencies = [BaseAppComponent::class])
internal interface ProductCardOptionsComponent {

    fun inject(productCardOptionsActivity: ProductCardOptionsActivity)
}
