package com.tokopedia.product.detail.view.viewholder.gwp.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.detail.view.viewholder.gwp.model.GWPWidgetUiModel

/**
 * Created by yovi.putra on 28/11/23"
 * Project name: android-tokopedia-core
 **/

abstract class GWPCardViewHolder<in DATA : GWPWidgetUiModel.Card>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: DATA)
}
