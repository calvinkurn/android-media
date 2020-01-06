package com.tokopedia.travelhomepage.destination.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import com.tokopedia.travelhomepage.homepage.presentation.fragment.TravelHomepageFragment.Companion.TYPE_ORDER_LIST
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.travel_homepage_travel_section_list_item.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelDestinationSectionAdapter(private var list: List<TravelDestinationSectionViewModel.Item>,
                                      private var type: Int,
                                      private var categoryType: String,
                                      private var onClickListener: OnClickListener) :
        RecyclerView.Adapter<TravelDestinationSectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(itemViewType, parent, false), onClickListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position, type, categoryType)
    }

    fun updateList(newList: List<TravelDestinationSectionViewModel.Item>) {
        this.list = newList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = list[position]
        return if (type == TYPE_ORDER_LIST) {
            if (item.subtitle.isBlank()) ViewHolder.ORDER_LAYOUT_WITHOUT_SUBTITLE else ViewHolder.ORDER_LAYOUT
        } else {
            if (item.subtitle.isBlank()) ViewHolder.LAYOUT_WITHOUT_SUBTITLE else ViewHolder.LAYOUT
        }
    }

    class ViewHolder(itemView: View, private val onClickListener: OnClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelDestinationSectionViewModel.Item, position: Int, type: Int, categoryType: String) {
            with(itemView) {
                image.loadImage(item.imageUrl)
                title.text = item.title
                if (item.subtitle.isNotBlank()) subtitle.text = item.subtitle
                setOnClickListener { onClickListener.clickAndRedirect(item.appUrl) }

                prefix.text = when (item.prefixStyling) {
                    TravelDestinationSectionViewModel.PREFIX_STYLE_STRIKETHROUGH -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_strikethrough, item.prefix, item.value))
                    TravelDestinationSectionViewModel.PREFIX_STYLE_NORMAL -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_normal, item.prefix, item.value))
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