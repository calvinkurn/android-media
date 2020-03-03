package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.DisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-24.
 */

class ShopHomeItemSliderSquareViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val ivSliderSquare: AppCompatImageView = itemView.findViewById(R.id.ivSliderSquare)

    fun bind(element: DisplayWidgetUiModel.DisplayWidgetItem) {
        ImageHandler.LoadImage(ivSliderSquare, element.imageUrl)
    }
}