package com.tokopedia.catalog.listener

import androidx.fragment.app.FragmentManager
import com.tokopedia.catalog.model.datamodel.CatalogForYouModel
import com.tokopedia.catalog.model.raw.*

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
    fun onViewMoreSpecificationsClick(topModel : TopSpecificationsComponentData?){}

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

    fun openComparisonNewBottomSheet(comparisonNewModel : ComparisonNewModel?) {}

    fun comparisonNewCatalogClicked(comparisonCatalogId : String) {}

    fun changeComparison(comparedCatalogId: String){}

    fun accordionDropUp(tabName : String?) {}

    fun accordionDropDown(tabName : String?) {}
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

    fun sendWidgetImpressionEvent(widgetImpressionActionName : String, widgetImpressionItemName: String, adapterPosition: Int) {}

    fun sendWidgetTrackEvent(actionName: String) { }

    fun setLastDetachedItemPosition(adapterPosition: Int) {}

    fun setLastAttachItemPosition(adapterPosition: Int) {}

}
