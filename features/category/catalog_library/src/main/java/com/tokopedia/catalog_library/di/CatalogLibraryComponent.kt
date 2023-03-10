package com.tokopedia.catalog_library.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.catalog_library.ui.fragment.CatalogHomepageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogLandingPageFragment
import com.tokopedia.catalog_library.ui.fragment.CatalogLihatSemuaPageFragment
import com.tokopedia.catalog_library.ui.fragment.PopularBrandsFragment
import dagger.Component

@CatalogLibraryScope
@Component(modules = [CatalogLibraryUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogLibraryComponent {
    fun inject(catalogHomepageFragment: CatalogHomepageFragment)
    fun inject(catalogLihatSemuaPageFragment: CatalogLihatSemuaPageFragment)
    fun inject(catalogLandingPageFragment: CatalogLandingPageFragment)
    fun inject(catalogPopularBrandsFragment: PopularBrandsFragment)
}
