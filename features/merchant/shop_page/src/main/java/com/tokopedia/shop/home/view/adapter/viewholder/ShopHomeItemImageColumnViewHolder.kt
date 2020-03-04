package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

/**
 * Created by rizqiaryansa on 2020-02-21.
 */

class ShopHomeItemImageColumnViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val ivMultipleColumn: AppCompatImageView = itemView.findViewById(R.id.ivMultipleColumn)

    fun bind(data: ShopHomeDisplayWidgetUiModel.DisplayWidgetItem) {
        ImageHandler.LoadImage(ivMultipleColumn, data.imageUrl)
    }
}