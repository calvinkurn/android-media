package com.tokopedia.catalog_library.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.catalog_library.ui.fragment.*
import dagger.Component

@CatalogLibraryScope
@Component(modules = [CatalogLibraryModule::class,
    CatalogLibraryViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogLibraryComponent {
    fun inject(catalogHomepageFragment: CatalogHomepageFragment)
    fun inject(catalogLihatSemuaPageFragment: CatalogLihatSemuaPageFragment)
    fun inject(catalogLandingPageFragment: CatalogLandingPageFragment)
    fun inject(catalogPopularBrandsFragment: CatalogPopularBrandsFragment)
    fun inject(catalogBrandLandingPageFragment: CatalogBrandLandingPageFragment)
}
