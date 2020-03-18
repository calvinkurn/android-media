package com.tokopedia.travelhomepage.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.TravelHomepageSectionModel
import com.tokopedia.travelhomepage.homepage.data.TravelLayoutSubhomepage
import com.tokopedia.travelhomepage.homepage.presentation.listener.TravelHomepageActionListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelHomepageSectionAdapter(private var list: List<TravelHomepageSectionModel.Item>,
                                   var layoutData: TravelLayoutSubhomepage.Data,
                                   var listener: TravelHomepageActionListener) :
        RecyclerView.Adapter<TravelHomepageSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemViewType, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position, listener, layoutData)
    }

    fun updateList(newList: List<TravelHomepageSectionModel.Item>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (item.subtitle.isBlank()) ViewHolder.LAYOUT_WITHOUT_SUBTITLE else ViewHolder.LAYOUT
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelHomepageSectionModel.Item, position: Int, listener: TravelHomepageActionListener, layoutData: TravelLayoutSubhomepage.Data) {
            with(itemView) {
                image.loadImage(item.imageUrl)
                title.text = item.title
                if (item.subtitle.isNotBlank()) subtitle.text = item.subtitle

                prefix.text = when (item.prefixStyling) {
                    TravelHomepageSectionModel.PREFIX_STYLE_STRIKETHROUGH -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_strikethrough, item.prefix, item.value))
                    TravelHomepageSectionModel.PREFIX_STYLE_NORMAL -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_normal, item.prefix, item.value))
                    else -> resources.getString(R.string.travel_prefix_nostyle, item.prefix, item.value)
                }
            }
            if (listener != null) itemView.setOnClickListener {
                listener.onClickProductSliderItem(item, position, layoutData.position, layoutData.title)
                listener.onItemClick(item.appUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.travel_homepage_section_list_item
            val LAYOUT_WITHOUT_SUBTITLE = R.layout.travel_homepage_section_list_item_without_subtitle
        }
    }
}