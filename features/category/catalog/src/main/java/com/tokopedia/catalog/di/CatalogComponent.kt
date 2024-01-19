package com.tokopedia.catalog.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.catalog.ui.fragment.CatalogSwitchingComparisonFragment
import com.tokopedia.catalog.ui.fragment.CatalogComparisonDetailFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogAllReviewFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailPageFragment as OldCatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogLandingPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductListFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductListImprovementFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogProductComparisonFragment
import com.tokopedia.oldcatalog.viewholder.products.CatalogForYouContainerViewHolder
import dagger.Component

@CatalogScope
@Component(modules = [CatalogUseCaseModule::class,CatalogModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogComponent {

    fun inject(catalogDetailPageFragment: OldCatalogDetailPageFragment)
    fun inject(catalogDetailProductListingFragment: CatalogDetailProductListingFragment)
    fun inject(catalogAllReviewFragment: CatalogAllReviewFragment)
    fun inject(catalogProductComparisonFragment: CatalogProductComparisonFragment)
    fun inject(catalogForYouContainerViewHolder : CatalogForYouContainerViewHolder)

    // layout version 4
    fun inject(catalogDetailPageFragment: CatalogDetailPageFragment)
    fun inject(catalogProductListFragment: CatalogProductListFragment)
    fun inject(catalogLandingPageFragment: CatalogLandingPageFragment)
    fun inject(catalogComparisonDetailFragment: CatalogComparisonDetailFragment)
    fun inject(catalogProductListImprovementFragment: CatalogProductListImprovementFragment)

    fun inject(catalogSwitchingComparisonFragment: CatalogSwitchingComparisonFragment)


}
