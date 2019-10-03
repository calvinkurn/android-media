package com.tokopedia.common.travel.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import kotlinx.android.synthetic.main.item_travel_cross_selling.view.*

/**
 * @author by jessica on 2019-10-02
 */

class TravelCrossSellAdapter: RecyclerView.Adapter<TravelCrossSellAdapter.CrossSellingVH>() {

    var crossSellingProducts: MutableList<TravelCrossSelling.CrossSellingItem> = arrayListOf()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellingVH {
        return CrossSellingVH(LayoutInflater.from(parent.context).inflate(R.layout.item_travel_cross_selling, parent, false))
    }

    override fun getItemCount(): Int = crossSellingProducts.size

    override fun onBindViewHolder(holder: CrossSellingVH, position: Int) {
        holder.bind(crossSellingProducts.get(position))
        holder.itemView.setOnClickListener { listener?.onItemClickListener(crossSellingProducts.get(position)) }
    }

    fun setItem(products: List<TravelCrossSelling.CrossSellingItem>) {
        crossSellingProducts.clear()
        crossSellingProducts.addAll(products)
        notifyDataSetChanged()
    }

    class CrossSellingVH(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(travelCrossSelling: TravelCrossSelling.CrossSellingItem) {
            with(itemView) {
                ImageHandler.loadImage(context, item_image, travelCrossSelling.imageUrl, null)
                item_title.text = travelCrossSelling.title
                item_subtitle.text = travelCrossSelling.content
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(item: TravelCrossSelling.CrossSellingItem)
    }

}