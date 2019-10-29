package com.tokopedia.salam.umrah.common.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleDetailModel
import kotlinx.android.synthetic.main.item_umrah_simple_detail_view.view.*

/**
 * @author by furqan on 14/10/2019
 */
class UmrahSimpleDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: UmrahSimpleDetailModel) {
        with(itemView) {
            tg_umrah_title.text = item.title
            tg_umrah_subtitle.text = item.subtitle
            iv_umrah_detail_icon.loadImage(item.icon)
        }
    }
}