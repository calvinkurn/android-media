package com.tokopedia.catalog.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.oldcatalog.ui.fragment.CatalogAllReviewFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailPageFragment as OldCatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.oldcatalog.ui.fragment.CatalogProductComparisonFragment
import com.tokopedia.oldcatalog.viewholder.products.CatalogForYouContainerViewHolder
import dagger.Component

@CatalogScope
@Component(modules = [CatalogUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogComponent {
    // old
    fun inject(catalogDetailPageFragment: OldCatalogDetailPageFragment)
    fun inject(catalogDetailProductListingFragment: CatalogDetailProductListingFragment)
    fun inject(catalogAllReviewFragment: CatalogAllReviewFragment)
    fun inject(catalogProductComparisonFragment: CatalogProductComparisonFragment)
    fun inject(catalogForYouContainerViewHolder : CatalogForYouContainerViewHolder)

    // new
    fun inject(catalogDetailPageFragment: CatalogDetailPageFragment)
}
