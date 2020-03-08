package com.tokopedia.shop.home.view.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeItemSliderSquareViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    companion object{
        const val ITEM_WIDTH_RATIO_DIVIDER = 1.5
    }

    private val ivSliderSquare: AppCompatImageView = itemView.findViewById(R.id.ivSliderSquare)

    fun bind(element: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ImageHandler.LoadImage(ivSliderSquare, element.imageUrl)
        val deviceWidth = Resources.getSystem().displayMetrics.widthPixels;
        itemView.layoutParams.width = (deviceWidth / ITEM_WIDTH_RATIO_DIVIDER).toInt()
    }
}