package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeItemImageColumnViewHolder(
        itemView: View,
        val listener: ShopHomeDisplayWidgetListener
) : RecyclerView.ViewHolder(itemView) {

    private val ivMultipleColumn: AppCompatImageView = itemView.findViewById(R.id.ivMultipleColumn)
    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ImageHandler.LoadImage(ivMultipleColumn, data.imageUrl)
        ivMultipleColumn.setOnClickListener {
            listener.onDisplayItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivMultipleColumn.addOnImpressionListener(data) {
            listener.onDisplayItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}