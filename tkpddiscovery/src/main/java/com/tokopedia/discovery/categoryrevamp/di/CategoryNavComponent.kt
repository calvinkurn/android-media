package com.tokopedia.discovery.categoryrevamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.categoryrevamp.view.activity.CategoryNavActivity
import com.tokopedia.discovery.categoryrevamp.view.fragments.BaseBannedProductFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.CatalogNavFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.ProductNavFragment
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component


@CategoryNavScope
@Component(modules = [CategoryNavUseCaseModule::class,
    CategoryViewModelModule::class,
    TopAdsWishlistModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavComponent {
    fun inject(productNavFragment: ProductNavFragment)
    fun inject(bannedProductFragment: BaseBannedProductFragment)
    fun inject(catalogNavFragment: CatalogNavFragment)
    fun inject(categoryNavActivity: CategoryNavActivity)
}