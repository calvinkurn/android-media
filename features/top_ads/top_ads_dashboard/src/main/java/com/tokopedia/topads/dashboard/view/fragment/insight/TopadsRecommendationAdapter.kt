package com.tokopedia.topads.dashboard.view.fragment.insight

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.recommendation.data.ProductRecommendation
import kotlinx.android.synthetic.main.topads_dash_recon_product_item.view.*

class TopadsRecommendationAdapter : RecyclerView.Adapter<TopadsRecommendationAdapter.ViewHolder>() {
    var items:MutableList<ProductRecommendation> = mutableListOf()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.topads_dash_recon_product_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.productName.text = items[holder.adapterPosition].productName
        holder.view.img_folder.loadImage(items[holder.adapterPosition].imgUrl)
        holder.view.totalSearch.text = items[holder.adapterPosition].searchPercentage
       // holder.view.potential.text = items[holder.adapterPosition].recomBid
        holder.view.recommendedBid.text = items[holder.adapterPosition].recomBid.toString()
        holder.view.editBudget.textFiedlLabelText.text = items[holder.adapterPosition].recomBid.toString()
    }

}
