package com.tokopedia.discovery.catalogrevamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.catalogrevamp.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.discovery.catalogrevamp.ui.fragment.CatalogDetailProductListingFragment
import dagger.Component

@CatalogScope
@Component(modules = [CatalogUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogComponent {
    fun inject(catalogDetailPageFragment: CatalogDetailPageFragment)
    fun inject(catalogDetailProductListingFragment: CatalogDetailProductListingFragment)
}
