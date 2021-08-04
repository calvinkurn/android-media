package com.tokopedia.tokopoints.view.recommwidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.tokopointhome.RecommendationWrapper
import com.tokopedia.tokopoints.view.tokopointhome.RewardsRecomListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class RewardsRecommAdapter(val list: ArrayList<RecommendationWrapper> , val listener : RewardsRecomListener) :
    RecyclerView.Adapter<RewardsRecommAdapter.ProductCardViewHolder>() {

    inner class ProductCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val productView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)

        fun bind(model: RecommendationWrapper) {
            productView.setProductModel(model.recomData)
            val impressItem = model.recomendationItem
            productView.setImageProductViewHintListener(
                model.recomendationItem,object : ViewHintListener {
                    override fun onViewHint() {
                        if (impressItem.isTopAds) {
                            TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                                className,
                                impressItem.trackerImageUrl,
                                impressItem.productId.toString(),
                                impressItem.name,
                                impressItem.imageUrl,"")
                        }
                        listener.onProductImpression(impressItem,position)
                    }
                }
            )

            productView.setOnClickListener {
                if (impressItem.isTopAds) {
                        TopAdsUrlHitter(itemView.context).hitClickUrl(
                            className,
                            impressItem.clickUrl,
                            impressItem.productId.toString(),
                            impressItem.name,
                            impressItem.imageUrl,"")
                }
                listener.onProductClick(impressItem,"",position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tp_layout_recomm_item, parent, false)
        return ProductCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(list[position])
    }

    companion object{
        const val className = "com.tokopedia.tokopoints.view.recommwidget.RewardsRecommAdapter"
    }
}
