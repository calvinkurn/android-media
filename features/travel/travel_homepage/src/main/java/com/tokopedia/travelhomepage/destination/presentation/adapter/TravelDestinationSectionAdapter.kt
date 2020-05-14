package com.tokopedia.travelhomepage.destination.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel
import com.tokopedia.travelhomepage.destination.presentation.viewmodel.TravelDestinationViewModel.Companion.ORDER_LIST_ORDER
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelDestinationSectionAdapter(private var list: List<TravelDestinationSectionModel.Item>,
                                      private var type: Int,
                                      private var categoryType: String,
                                      private var actionListener: ActionListener) :
        RecyclerView.Adapter<TravelDestinationSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemViewType, parent, false), actionListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position, type, categoryType)
    }

    fun updateList(newList: List<TravelDestinationSectionModel.Item>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (type == ORDER_LIST_ORDER) {
            if (item.subtitle.isBlank()) ViewHolder.ORDER_LAYOUT_WITHOUT_SUBTITLE else ViewHolder.ORDER_LAYOUT
        } else {
            if (item.subtitle.isBlank()) ViewHolder.LAYOUT_WITHOUT_SUBTITLE else ViewHolder.LAYOUT
        }
    }

    class ViewHolder(itemView: View, private val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelDestinationSectionModel.Item, position: Int, type: Int, categoryType: String) {
            with(itemView) {
                image.loadImage(item.imageUrl)
                title.text = item.title
                if (item.subtitle.isNotBlank()) subtitle.text = item.subtitle
                setOnClickListener {
                    when (type) {
                        TravelDestinationViewModel.CITY_EVENT_ORDER -> actionListener.onTrackEventItemClick(item, position)
                        TravelDestinationViewModel.CITY_DEALS_ORDER -> actionListener.onTrackDealsItemClick(item, position)
                        ORDER_LIST_ORDER -> actionListener.onTrackOrderClick(item, position)
                    }
                    actionListener.clickAndRedirect(item.appUrl)
                }

                prefix.text = when (item.prefixStyling) {
                    TravelDestinationSectionModel.PREFIX_STYLE_STRIKETHROUGH -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_strikethrough, item.prefix, item.value))
                    TravelDestinationSectionModel.PREFIX_STYLE_NORMAL -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_normal, item.prefix, item.value))
                    else -> resources.getString(R.string.travel_prefix_nostyle, item.prefix, item.value)
                }
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