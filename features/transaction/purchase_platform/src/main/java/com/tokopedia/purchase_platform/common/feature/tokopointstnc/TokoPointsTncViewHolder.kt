package com.tokopedia.purchase_platform.common.feature.tokopointstnc

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.purchase_platform.R
import kotlinx.android.synthetic.main.item_tokopoints_tnc.view.*

class TokoPointsTncViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    companion object {
        val LAYOUT = R.layout.item_tokopoints_tnc
    }

    fun bind(element: TokoPointsTncUiModel) {
        ImageHandler.loadImageCircle2(itemView.context, itemView.image_tnc_item, element.imageUrl)
        itemView.label_tnc_item.text = element.description
    }

}