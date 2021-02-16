package com.tokopedia.catalog.listener

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
     * CatalogSpecificationViewHolder
     */


    /**
     * CatalogSpecificationsContainerViewHolder
     */
    fun onViewMoreSpecificationsClick()

    /**
     * CatalogVideoViewHolder
     */


    /**
     * CatalogVideoContainerViewHolder
     */


}