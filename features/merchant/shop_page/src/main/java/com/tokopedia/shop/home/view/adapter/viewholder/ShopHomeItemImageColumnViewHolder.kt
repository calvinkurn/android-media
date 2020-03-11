package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.setImage

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
        ivMultipleColumn.setImage(data.imageUrl, 0F)

        ivMultipleColumn.setOnClickListener {
            listener.onItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivMultipleColumn.addOnImpressionListener(data) {
            listener.onItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}