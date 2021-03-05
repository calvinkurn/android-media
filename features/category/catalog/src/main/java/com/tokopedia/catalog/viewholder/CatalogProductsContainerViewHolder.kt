package com.tokopedia.catalog.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogProductsContainerDataModel
import com.tokopedia.catalog.ui.fragment.CatalogDetailProductListingFragment

class CatalogProductsContainerViewHolder(private val view: View,
                                         private val catalogDetailListener: CatalogDetailListener) : AbstractViewHolder<CatalogProductsContainerDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_catalog_products_container
    }

    override fun bind(element: CatalogProductsContainerDataModel) {
        //attachFragmentToContainer(R.id.products_container_frame, element.catalogId)
    }

    private fun attachFragmentToContainer(containerId: Int, catalogId: String) {

        catalogDetailListener.childsFragmentManager?.beginTransaction()
                ?.add(containerId, CatalogDetailProductListingFragment.newInstance(catalogId, ""))
                ?.commit()

    }

//    private fun attachFragmentToContainer2(containerId: Int, catalogId : String) {
//        val fragment = if (catalogDetailListener.childsFragmentManager.fragments.firstOrNull { it is CatalogDetailProductListingFragment } == null)
//            CatalogDetailProductListingFragment.newInstance(catalogId, "", "", "")
//        else
//            null
//
//        if (fragment != null) {
//            catalogDetailListener.childsFragmentManager.beginTransaction()
//                    .add(containerId, fragment)
//                    .commitNowAllowingStateLoss()
//        }
//    }
}