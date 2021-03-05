package com.tokopedia.catalog.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.model.raw.CatalogImage

interface CatalogDetailListener {

    /**
     * CatalogInfoViewHolder
     */
    fun onViewMoreDescriptionClick()

    /**
     * CatalogImageViewHolder
     */
    fun onProductImageClick(catalogImage : CatalogImage, position: Int)

    /**
     * CatalogSpecificationsContainerViewHolder
     */
    fun onViewMoreSpecificationsClick()

    /**
     * CatalogProductsContainerViewHolder
     */
    fun hideFloatingLayout()

    fun showFloatingLayout()

    val childsFragmentManager: FragmentManager?

}