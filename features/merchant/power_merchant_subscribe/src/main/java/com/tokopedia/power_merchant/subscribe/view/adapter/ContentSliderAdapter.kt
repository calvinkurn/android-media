package com.tokopedia.power_merchant.subscribe.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.view.model.ContentSliderUiModel
import kotlinx.android.synthetic.main.item_pm_content_slider.view.*

/**
 * Created By @ilhamsuaib on 01/03/21
 */

class ContentSliderAdapter : RecyclerView.Adapter<ContentSliderAdapter.ContentSliderViewHolder>() {

    var items: List<ContentSliderUiModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentSliderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_pm_content_slider, parent, false)
        return ContentSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentSliderViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ContentSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ContentSliderUiModel) {
            with(itemView) {
                tvPmItemContentSliderTitle.text = item.title
                tvPmItemContentSliderDescription.text = item.description
                imgPmItemContentSliderImage.loadImageWithoutPlaceholder(item.imgUrl)
            }
        }
    }
}