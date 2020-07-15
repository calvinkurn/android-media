package com.tokopedia.travelhomepage.homepage.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.homepage.data.widgetmodel.LegoBannerItemModel
import com.tokopedia.travelhomepage.homepage.widget.TravelHomepageLegoBannerWidget
import kotlinx.android.synthetic.main.item_travel_homepage_lego_banner.view.*

/**
 * @author by jessica on 2020-02-28
 */

class TravelHomepageLegoBannerAdapter(var list: List<LegoBannerItemModel>, var listener: TravelHomepageLegoBannerWidget.ActionListener?) : RecyclerView.Adapter<TravelHomepageLegoBannerAdapter.ViewHolder>() {

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

        fun bind(item: LegoBannerItemModel) {
            with(itemView) {
                legoBannerItemImage.loadImage(item.imageUrl)
            }
        }

        companion object {
            val LAYOUT = R.layout.item_travel_homepage_lego_banner
        }
    }


}