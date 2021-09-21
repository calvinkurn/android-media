package com.tokopedia.catalog.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.catalog.model.raw.VideoComponentData

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

    /**
     * CatalogVideoViewHolder
     */
    fun playVideo(catalogVideo : VideoComponentData, position : Int)

    /**
     * CatalogComparisionContainerViewHolder
     */
    fun comparisionCatalogClicked(comparisionCatalogId : String)

    val childsFragmentManager: FragmentManager?

    val windowHeight: Int

}