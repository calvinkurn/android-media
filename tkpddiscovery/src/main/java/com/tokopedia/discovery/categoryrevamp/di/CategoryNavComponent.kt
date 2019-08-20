package com.tokopedia.discovery.categoryrevamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.categoryrevamp.view.fragments.activity.CatalogNavFragment
import com.tokopedia.discovery.categoryrevamp.view.fragments.activity.ProductNavFragment
import dagger.Component


@CategoryNavScope
@Component(modules = [CategoryNavUseCaseModule::class, CategoryViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CategoryNavComponent {
    fun inject(productNavFragment: ProductNavFragment)
    fun inject(catalogNavFragment: CatalogNavFragment)

}