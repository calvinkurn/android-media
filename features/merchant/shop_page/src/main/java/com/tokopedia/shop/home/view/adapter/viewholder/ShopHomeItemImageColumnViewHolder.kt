package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.setImage

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeItemImageColumnViewHolder(
        itemView: View,
        val listener: ShopHomeDisplayWidgetListener,
        private val heightRatio: Float
) : RecyclerView.ViewHolder(itemView) {

    private val ivMultipleColumn: ImageUnify = itemView.findViewById(R.id.ivMultipleColumn)
    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ivMultipleColumn.setImageUrl(data.imageUrl, heightRatio = heightRatio)

        ivMultipleColumn.setOnClickListener {
            listener.onDisplayItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivMultipleColumn.addOnImpressionListener(data) {
            listener.onDisplayItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}