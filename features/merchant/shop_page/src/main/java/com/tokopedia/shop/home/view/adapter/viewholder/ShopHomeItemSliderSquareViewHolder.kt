package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomeDisplayWidgetListener
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeItemSliderSquareViewHolder(
        itemView: View,
        private val listener: ShopHomeDisplayWidgetListener
): RecyclerView.ViewHolder(itemView) {

    companion object{
        const val ITEM_WIDTH_RATIO_DIVIDER = 1.5
    }

    private val ivSliderSquare: AppCompatImageView = itemView.findViewById(R.id.ivSliderSquare)
    var displayWidgetUiModel: ShopHomeDisplayWidgetUiModel? = null
    var parentPosition: Int = 0

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ImageHandler.LoadImage(ivSliderSquare, data.imageUrl)
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        itemView.layoutParams.width = (deviceWidth / ITEM_WIDTH_RATIO_DIVIDER).toInt()
        ivSliderSquare.setOnClickListener {
            listener.onItemClicked(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
        ivSliderSquare.addOnImpressionListener(data) {
            listener.onItemImpression(displayWidgetUiModel, data, parentPosition, adapterPosition)
        }
    }
}