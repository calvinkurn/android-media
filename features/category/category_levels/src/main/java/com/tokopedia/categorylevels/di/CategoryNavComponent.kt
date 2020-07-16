package com.tokopedia.categorylevels.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.categorylevels.view.activity.CategoryNavActivity
import com.tokopedia.categorylevels.view.fragment.CatalogNavFragment
import com.tokopedia.categorylevels.view.fragment.ProductNavFragment
import com.tokopedia.topads.sdk.di.TopAdsUrlHitterModule
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component


@CategoryNavScope
@Component(modules = [CategoryNavUseCaseModule::class,
    CategoryViewModelModule::class,
    TopAdsWishlistModule::class,
    TopAdsUrlHitterModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavComponent {
    fun inject(productNavFragment: ProductNavFragment)
    fun inject(catalogNavFragment: CatalogNavFragment)
    fun inject(categoryNavActivity: CategoryNavActivity)
}