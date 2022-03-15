package com.tokopedia.catalog.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.model.datamodel.CatalogForYouModel
import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.catalog.model.raw.VideoComponentData

interface CatalogDetailListener {

    /**
     * CatalogInfoViewHolder
     */
    fun onViewMoreDescriptionClick(){}

    /**
     * CatalogImageViewHolder
     */
    fun onProductImageClick(catalogImage : CatalogImage, position: Int){}

    /**
     * CatalogSpecificationsContainerViewHolder
     */
    fun onViewMoreSpecificationsClick(){}

    /**
     * CatalogProductsContainerViewHolder
     */
    fun hideFloatingLayout(){}

    fun showFloatingLayout(){}

    /**
     * CatalogVideoViewHolder
     */
    fun playVideo(catalogVideo : VideoComponentData, position : Int){}

    /**
     * CatalogComparisionContainerViewHolder
     */
    fun comparisonCatalogClicked(comparisonCatalogId : String){}

    fun openComparisonBottomSheet(comparisonCatalog: ComparisionModel?){}

    fun changeComparison(comparedCatalogId: String){}

    /**
     * CatalogReviewContainerViewHolder
     */
    fun readMoreReviewsClicked(catalogId : String){}

    /**
     * CatalogReviewViewHolder
     */
    fun onReviewImageClicked(position: Int, items : ArrayList<CatalogImage>, reviewId : String,
                             isFromBottomSheet : Boolean = true){}

    fun onCatalogForYouClick(adapterPosition : Int , catalogComparison: CatalogComparisonProductsResponse.CatalogComparisonList.CatalogComparison) {}

    fun onCatalogForYouImpressed(model : CatalogForYouModel , adapterPosition: Int){}

    fun getChildsFragmentManager() : FragmentManager? {
        return null
    }

    fun getWindowHeight() : Int { return 0 }

}