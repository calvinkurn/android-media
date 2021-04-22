package com.tokopedia.catalog.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.user.session.UserSession

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

    val windowHeight: Int

}