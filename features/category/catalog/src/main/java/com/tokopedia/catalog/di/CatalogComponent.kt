package com.tokopedia.catalog.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.catalog.ui.fragment.CatalogAllReviewFragment
import com.tokopedia.catalog.ui.fragment.CatalogDetailPageFragment
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment
import com.tokopedia.catalog.ui.fragment.CatalogProductComparisonFragment
import com.tokopedia.catalog.viewholder.products.CatalogForYouContainerViewHolder
import dagger.Component

@CatalogScope
@Component(modules = [CatalogUseCaseModule::class,
    ViewModelModule::class],
        dependencies = [BaseAppComponent::class])
interface CatalogComponent {
    fun inject(catalogDetailPageFragment: CatalogDetailPageFragment)
    fun inject(catalogDetailProductListingFragment: CatalogDetailProductListingFragment)
    fun inject(catalogAllReviewFragment: CatalogAllReviewFragment)
    fun inject(catalogProductComparisonFragment: CatalogProductComparisonFragment)
    fun inject(catalogForYouContainerViewHolder : CatalogForYouContainerViewHolder)
}
