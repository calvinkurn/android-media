package com.tokopedia.catalog.listener

import com.tokopedia.catalog.model.raw.CatalogImage

interface CatalogDetailListener {

    /**
     * CatalogInfoViewHolder
     */


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


    /**
     * CatalogVideoViewHolder
     */


    /**
     * CatalogVideoContainerViewHolder
     */


}