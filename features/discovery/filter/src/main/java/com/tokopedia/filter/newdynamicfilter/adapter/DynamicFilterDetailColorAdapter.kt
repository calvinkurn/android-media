package com.tokopedia.filter.newdynamicfilter.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView

import com.tokopedia.design.color.ColorSampleView
import com.tokopedia.filter.R
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.adapter.viewholder.DynamicFilterDetailViewHolder
import com.tokopedia.filter.newdynamicfilter.view.DynamicFilterDetailView

/**
 * Created by henrypriyono on 8/16/17.
 */

class DynamicFilterDetailColorAdapter(filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailAdapter(filterDetailView) {

    override val layout: Int = R.layout.filter_detail_color

    override fun getViewHolder(view: View): DynamicFilterDetailViewHolder {
        return ColorItemViewHolder(view, filterDetailView)
    }

    private class ColorItemViewHolder(itemView: View, filterDetailView: DynamicFilterDetailView) : DynamicFilterDetailViewHolder(itemView, filterDetailView) {

        private val colorIcon: ColorSampleView = itemView.findViewById(R.id.color_icon)
        private val colorTitle: TextView = itemView.findViewById(R.id.color_title)

        override fun bind(option: Option) {
            super.bind(option)
            colorIcon.setColor(Color.parseColor(option.hexColor))
            colorTitle.text = option.name
        }
    }
}
