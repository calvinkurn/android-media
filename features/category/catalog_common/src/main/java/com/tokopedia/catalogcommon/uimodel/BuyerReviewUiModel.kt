package com.tokopedia.catalogcommon.uimodel

import android.os.Parcelable
import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import kotlinx.parcelize.Parcelize

data class BuyerReviewUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val title: String,
    val items: List<ItemBuyerReviewUiModel>
) : BaseCatalogUiModel(idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor) {

    @Parcelize
    data class ItemBuyerReviewUiModel(
        val shopIcon: String,
        val shopName: String,
        val avatar: String,
        val reviewerName: String,
        val reviewerStatus: String? = null,
        val totalCompleteReview: Int? = null,
        val totalHelpedPeople: Int? = null,
        val description: String,
        val datetime: String? = null,
        val rating: Float,
        val variantName: String? = null,
        val images: List<ImgReview>,
        val catalogName: String = "",
        val reviewId: String = ""
    ) : Parcelable

    @Parcelize
    data class ImgReview(
        val id: String,
        val imgUrl: String,
        val fullsizeImgUrl: String = ""
    ) : Parcelable

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
