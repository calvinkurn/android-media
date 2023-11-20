package com.tokopedia.oldcatalog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.oldcatalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.oldcatalog.ui.fragment.CatalogDetailProductListingFragment

class CatalogPagerAdapter(private val catalogProductsContainerDataModel: CatalogProductsContainerDataModel,
                          fragmentManager: FragmentManager  ) : FragmentStatePagerAdapter(fragmentManager) {

    private val pageCount = 1

    override fun getItem(position: Int): Fragment {
        return CatalogDetailProductListingFragment.newInstance(catalogProductsContainerDataModel.catalogId,
            catalogProductsContainerDataModel.catalogName,
            catalogProductsContainerDataModel.catalogUrl,
            catalogProductsContainerDataModel.categoryId,
            catalogProductsContainerDataModel.catalogBrand,
            catalogProductsContainerDataModel.productSortingStatus
            )
    }

    override fun getCount(): Int {
        return  pageCount
    }
}
