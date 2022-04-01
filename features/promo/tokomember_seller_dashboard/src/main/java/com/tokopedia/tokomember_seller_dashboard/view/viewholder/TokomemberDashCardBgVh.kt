package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomember_seller_dashboard.model.CardTemplateImageListItem
import kotlinx.android.synthetic.main.tm_dash_colorbg_item.view.*

class TokomemberDashCardBgVh(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(colorTemplateListItem: CardTemplateImageListItem) {
        itemView.colorBg.loadImage(colorTemplateListItem.imageURL)
    }
}