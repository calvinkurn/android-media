package com.tokopedia.categoryLevels.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.categoryLevels.view.activity.CategoryNavActivity
import com.tokopedia.categoryLevels.view.fragment.CatalogNavFragment
import com.tokopedia.categoryLevels.view.fragment.ProductNavFragment
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component


@CategoryNavScope
@Component(modules = [CategoryNavUseCaseModule::class,
    CategoryViewModelModule::class,
    TopAdsWishlistModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavComponent {
    fun inject(productNavFragment: ProductNavFragment)
    fun inject(catalogNavFragment: CatalogNavFragment)
    fun inject(categoryNavActivity: CategoryNavActivity)
}