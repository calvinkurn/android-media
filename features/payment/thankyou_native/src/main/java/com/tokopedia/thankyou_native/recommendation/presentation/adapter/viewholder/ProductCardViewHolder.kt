package com.tokopedia.thankyou_native.recommendation.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.recommendation.data.ThankYouProductCardModel
import com.tokopedia.thankyou_native.recommendation.presentation.adapter.listener.ProductCardViewListener

class ProductCardViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val productCardView = itemView.findViewById<ProductCardGridView>(R.id.productCardView)


    private lateinit var thankYouProductCardModel: ThankYouProductCardModel

    fun bind(thankYouProductCardModel: ThankYouProductCardModel,
             listener: ProductCardViewListener?) {
        this.thankYouProductCardModel = thankYouProductCardModel
        thankYouProductCardModel.productCardModel.let { productCardModel ->
            itemView.tag = thankYouProductCardModel
            productCardView.run {
                applyCarousel()
                setProductModel(productCardModel)
                setImageProductViewHintListener(thankYouProductCardModel
                        .recommendationItem, object : ViewHintListener {
                    override fun onViewHint() {
                        listener?.onProductImpression(thankYouProductCardModel
                                .recommendationItem, adapterPosition)
                    }
                })

                setOnClickListener {
                    listener?.onProductClick(thankYouProductCardModel
                            .recommendationItem, null, adapterPosition)
                }

                setThreeDotsOnClickListener {
                    listener?.onThreeDotsAllProductClicked(thankYouProductCardModel)
                }
            }
        }

    }


    fun getThankYouRecommendationModel(): ThankYouProductCardModel? {
        if (::thankYouProductCardModel.isInitialized)
            return thankYouProductCardModel
        return null
    }


    companion object {
        val LAYOUT_ID = R.layout.thank_item_recommendation
    }
}
