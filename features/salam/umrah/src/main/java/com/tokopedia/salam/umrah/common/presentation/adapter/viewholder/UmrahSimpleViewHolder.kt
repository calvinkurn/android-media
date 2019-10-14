package com.tokopedia.salam.umrah.common.presentation.adapter.viewholder

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel
import kotlinx.android.synthetic.main.item_umrah_simple_view.view.*

/**
 * @author by furqan on 08/10/2019
 */
class UmrahSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: UmrahSimpleModel) {
        with(itemView) {
            tg_umrah_title.text = item.title
            tg_umrah_desc.text = item.description
            if (item.textColor.isNotEmpty()) {
                tg_umrah_desc.setTextColor(Color.parseColor(item.textColor))
            }
        }
    }

}