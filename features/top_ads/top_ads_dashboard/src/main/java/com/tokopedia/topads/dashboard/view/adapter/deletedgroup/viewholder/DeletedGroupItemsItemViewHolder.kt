package com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.media.loader.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.view.adapter.deletedgroup.viewmodel.DeletedGroupItemsItemModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography


class DeletedGroupItemsItemViewHolder(
    val view: View,
) : DeletedGroupItemsViewHolder<DeletedGroupItemsItemModel>(view) {

    private val deletedProductImg: ImageUnify = view.findViewById(R.id.deletedProductImg)
    private val deletedProductName: Typography = view.findViewById(R.id.deletedProductName)
    private val date: Typography = view.findViewById(R.id.date)
    private val tampilCount: Typography = view.findViewById(R.id.tampilCount)
    private val pengeluaranCount: Typography = view.findViewById(R.id.pengeluaranCount)
    private val klikCount: Typography = view.findViewById(R.id.klikCount)
    private val pendapatanCount: Typography = view.findViewById(R.id.pendapatanCount)
    private val persentaseKlikCount: Typography = view.findViewById(R.id.persentaseKlikCount)
    private val produkTerjualCount: Typography = view.findViewById(R.id.produkTerjualCount)

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_dash_item_deleted_group_card
    }

    override fun bind(item: DeletedGroupItemsItemModel) {
        loadImage(item.topAdsDeletedAdsItem.productImageUri)
        deletedProductName.text = item.topAdsDeletedAdsItem.adTitle
        date.text = item.topAdsDeletedAdsItem.adDeletedTime
        tampilCount.text = item.topAdsDeletedAdsItem.statTotalImpression
        pengeluaranCount.text = item.topAdsDeletedAdsItem.statTotalSpent
        klikCount.text = item.topAdsDeletedAdsItem.statAvgClick
        pendapatanCount.text = item.topAdsDeletedAdsItem.statTotalGrossProfit
        persentaseKlikCount.text = item.topAdsDeletedAdsItem.statTotalCtr
        produkTerjualCount.text = item.topAdsDeletedAdsItem.statTotalConversion
    }

    private fun loadImage(productImageUri: String) {
        if (productImageUri.isNotEmpty()) {
            deletedProductImg.loadImage(productImageUri)
        } else {
            deletedProductImg.loadImage(R.drawable.product_image_empty)
        }
    }
}
