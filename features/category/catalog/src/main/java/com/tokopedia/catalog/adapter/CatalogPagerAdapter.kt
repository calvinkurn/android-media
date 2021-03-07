package com.tokopedia.catalog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment

class CatalogPagerAdapter(val catalogDetailListener: CatalogDetailListener,
                          private val catalogProductsContainerDataModel: CatalogProductsContainerDataModel,
                          fragmentManager: FragmentManager  ) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return CatalogDetailProductListingFragment.newInstance(catalogProductsContainerDataModel.catalogId)
    }

    override fun getCount(): Int {
        return  1
    }

}