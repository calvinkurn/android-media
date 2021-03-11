package com.tokopedia.catalog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment

class CatalogPagerAdapter(private val catalogProductsContainerDataModel: CatalogProductsContainerDataModel,
                          fragmentManager: FragmentManager  ) : FragmentStatePagerAdapter(fragmentManager) {

    private val pageCount = 1

    override fun getItem(position: Int): Fragment {
        return CatalogDetailProductListingFragment.newInstance(catalogProductsContainerDataModel.catalogId)
    }

    override fun getCount(): Int {
        return  pageCount
    }
}