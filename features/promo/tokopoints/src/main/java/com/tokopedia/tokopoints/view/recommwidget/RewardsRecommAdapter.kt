package com.tokopedia.tokopoints.view.recommwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopoints.R

class RewardsRecommAdapter(val list: ArrayList<ProductCardModel>) : RecyclerView.Adapter<RewardsRecommAdapter.ProductCardViewHolder>() {

    inner class ProductCardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val productView  = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

        fun bind(position: Int){
            productView.setProductModel(list[position])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tp_layout_recomm_item,parent,false)
        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(position)
    }
}