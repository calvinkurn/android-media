package com.tokopedia.salam.umrah.common.presentation.adapter.viewholder

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.presentation.model.UmrahSimpleModel
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.item_umrah_simple_view.view.*

/**
 * @author by furqan on 08/10/2019
 */
class UmrahSimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: UmrahSimpleModel, isTitleBold: Boolean) {
        with(itemView) {
            tg_umrah_title.text = item.title

            if (isTitleBold) {
                tg_umrah_title.weightType = Typography.BOLD
                tg_umrah_title.setTextColor(resources.getColor(R.color.Neutral_N700_96))
            } else {
                tg_umrah_title.weightType = Typography.REGULAR
                tg_umrah_title.setTextColor(resources.getColor(R.color.Neutral_N700_20))
            }

            if (item.description.isNotEmpty()) {
                tg_umrah_desc.text = item.description
                if (item.textColor.isNotEmpty()) {
                    tg_umrah_desc.setTextColor(Color.parseColor(item.textColor))
                } else {
                    tg_umrah_desc.setTextColor(resources.getColor(R.color.Neutral_N700_96))
                }
            } else {
                tg_umrah_desc.visibility = View.GONE
            }
        }
    }

}