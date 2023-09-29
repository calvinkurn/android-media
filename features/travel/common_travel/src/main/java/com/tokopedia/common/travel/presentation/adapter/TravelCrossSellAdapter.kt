package com.tokopedia.common.travel.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.databinding.ItemTravelCrossSellingBinding
import com.tokopedia.kotlin.extensions.view.loadImage

/**
 * @author by jessica on 2019-10-02
 */

class TravelCrossSellAdapter : RecyclerView.Adapter<TravelCrossSellAdapter.CrossSellingVH>() {

    var products: MutableList<TravelCrossSelling.Item> = arrayListOf()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrossSellingVH {
        return CrossSellingVH(LayoutInflater.from(parent.context).inflate(R.layout.item_travel_cross_selling, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: CrossSellingVH, position: Int) {
        holder.bind(products.get(position))
        holder.itemView.setOnClickListener { listener?.onItemClickListener(products.get(position), position) }
    }

    fun setItem(products: List<TravelCrossSelling.Item>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }

    class CrossSellingVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemTravelCrossSellingBinding.bind(itemView)

        fun bind(item: TravelCrossSelling.Item) {
            with(binding) {
                itemImage.loadImage(item.imageUrl)
                itemTitle.text = item.title
                itemSubtitle.text = item.content
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClickListener(item: TravelCrossSelling.Item, position: Int)
    }
}
