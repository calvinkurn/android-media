package com.tokopedia.salam.umrah.common.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
                tg_umrah_title.setTypeface(null, Typeface.BOLD)
                tg_umrah_title.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
            } else {
                tg_umrah_title.weightType = Typography.REGULAR
                tg_umrah_title.setTypeface(null, Typeface.NORMAL)
                tg_umrah_title.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_44))
            }

            if (item.description.isNotEmpty()) {
                tg_umrah_desc.text = item.description
                if (item.textColor.isNotEmpty()) {
                    tg_umrah_desc.setTextColor(Color.parseColor(item.textColor))
                } else {
                    tg_umrah_desc.setTextColor(resources.getColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
                }
            } else {
                tg_umrah_desc.visibility = View.GONE
            }
        }
    }

}