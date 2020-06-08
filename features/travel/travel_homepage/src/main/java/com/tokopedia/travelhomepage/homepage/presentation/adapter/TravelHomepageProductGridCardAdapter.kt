package com.tokopedia.travelhomepage.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.ProductGridCardItemModel
import com.tokopedia.travelhomepage.homepage.widget.TravelHomepageProductGridCardWidget
import kotlinx.android.synthetic.main.item_travel_homepage_product_widget_card.view.*

/**
 * @author by jessica on 2020-02-28
 */

class TravelHomepageProductGridCardAdapter(var list: List<ProductGridCardItemModel>, var listener: TravelHomepageProductGridCardWidget.ActionListener?) : RecyclerView.Adapter<TravelHomepageProductGridCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ViewHolder.LAYOUT, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener { listener?.onItemClickListener(list[position], position) }
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: ProductGridCardItemModel) {
            with(itemView) {
                travel_homepage_product_image.loadImage(item.imageUrl)
                travel_homepage_product_title.text = item.title

                if (item.subtitle.isNotEmpty()) {
                    travel_homepage_product_subtitle.text = item.subtitle
                    travel_homepage_product_subtitle.show()
                } else travel_homepage_product_subtitle.hide()

                if (item.prefix.isNotEmpty()) {
                    travel_homepage_product_prefix.show()
                    travel_homepage_product_prefix.text = when (item.prefixStyling) {
                        PREFIX_STYLE_STRIKETHROUGH -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_strikethrough, item.prefix, ""))
                        PREFIX_STYLE_NORMAL -> TextHtmlUtils.getTextFromHtml(resources.getString(R.string.travel_prefix_normal, item.prefix, ""))
                        else -> item.prefix
                    }
                } else travel_homepage_product_prefix.hide()

                if (item.value.isNotEmpty()) {
                    travel_homepage_product_value.show()
                    travel_homepage_product_value.text = item.value
                } else travel_homepage_product_value.hide()

                if (item.tag.isNotEmpty()) {
                    travel_homepage_product_discount_tag.show()
                    travel_homepage_product_discount_tag.text = item.tag
                } else travel_homepage_product_discount_tag.hide()
            }
        }

        companion object {
            val LAYOUT = R.layout.item_travel_homepage_product_widget_card
            val PREFIX_STYLE_STRIKETHROUGH = "strikethrough"
            val PREFIX_STYLE_NORMAL = "normal"
        }
    }


}