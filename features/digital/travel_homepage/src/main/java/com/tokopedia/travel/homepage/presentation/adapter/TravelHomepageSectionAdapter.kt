package com.tokopedia.travel.homepage.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travel.homepage.R
import com.tokopedia.travel.homepage.data.TravelHomepageSectionViewModel
import com.tokopedia.travel.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travel.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageSectionAdapter(private var list: List<TravelHomepageSectionViewModel.Item>,
                                   private var type: Int,
                                   var listener: OnItemClickListener):
        RecyclerView.Adapter<TravelHomepageSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemViewType, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position, listener, type)
    }

    override fun getItemViewType(position: Int): Int {
        val item = list.get(position)
        return if (type == TYPE_ORDER_LIST) {
            if (item.subtitle.isBlank()) ViewHolder.ORDER_LAYOUT_WITHOUT_SUBTITLE else ViewHolder.ORDER_LAYOUT
        } else {
            if (item.subtitle.isBlank()) ViewHolder.LAYOUT_WITHOUT_SUBTITLE else ViewHolder.LAYOUT
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelHomepageSectionViewModel.Item, position: Int, listener: OnItemClickListener, type: Int) {
            with(itemView) {
                image.loadImage(item.imageUrl)
                title.text = item.title
                if (item.subtitle.isNotBlank()) subtitle.text = item.subtitle

                prefix.text = when (item.prefixStyling) {
                    TravelHomepageSectionViewModel.PREFIX_STYLE_STRIKETHROUGH -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_strikethrough, item.prefix, item.value))
                    TravelHomepageSectionViewModel.PREFIX_STYLE_NORMAL -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_normal, item.prefix, item.value))
                    else -> resources.getString(R.string.travel_prefix_nostyle, item.prefix, item.value)
                }
            }
            if (listener != null) itemView.setOnClickListener {
                listener.onTrackEventClick(type, position, item.product)
                listener.onItemClick(item.appUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_section_list_item
            val LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_section_list_item_without_subtitle
            val ORDER_LAYOUT = R.layout.travel_homepage_order_section_list_item
            val ORDER_LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_order_section_list_without_subtitle_item
        }
    }
}