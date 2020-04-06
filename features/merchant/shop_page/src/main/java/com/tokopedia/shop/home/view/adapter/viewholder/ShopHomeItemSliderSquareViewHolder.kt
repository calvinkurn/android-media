package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel
import com.tokopedia.unifycomponents.ImageUnify

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeItemSliderSquareViewHolder(
        itemView: View,
        private val listener: ShopHomeDisplayWidgetListener,
        private val heightRatio: Float
): RecyclerView.ViewHolder(itemView) {

    companion object{
        const val ITEM_WIDTH_RATIO_DIVIDER = 1.8
    }

    private val ivSliderSquare: ImageUnify = itemView.findViewById(R.id.ivSliderSquare)
    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ivSliderSquare.setImageUrl(data.imageUrl, heightRatio = heightRatio)

        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        itemView.layoutParams.width = (deviceWidth / ITEM_WIDTH_RATIO_DIVIDER).toInt()
        ivSliderSquare.setOnClickListener {
            listener.onDisplayItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivSliderSquare.addOnImpressionListener(data) {
            listener.onDisplayItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}