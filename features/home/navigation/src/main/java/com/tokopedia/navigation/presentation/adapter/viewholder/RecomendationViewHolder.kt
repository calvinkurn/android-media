package com.tokopedia.navigation.presentation.adapter.viewholder

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.InboxGtmTracker
import com.tokopedia.navigation.domain.model.Recomendation
import com.tokopedia.navigation.presentation.adapter.InboxAdapterListener
import com.tokopedia.productcard.ProductCardView
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.utils.ImpresionTask

/**
 * Author errysuprayogi on 13,March,2019
 */
public class RecomendationViewHolder(itemView: View, private val listener: InboxAdapterListener) : AbstractViewHolder<Recomendation>(itemView) {
    private val productCardView: ProductCardView
    private val context: Context

    init {
        this.context = itemView.context
        productCardView = itemView.findViewById(R.id.productCardView)
    }

    override fun bind(element: Recomendation) {
        productCardView.setImageUrl(element.imageUrl)
        productCardView.setTitle(element.productName)
        productCardView.setPrice(element.price)
        productCardView.setTopAdsVisible(element.isTopAds)
        if (element.isTopAds) {
            productCardView.imageView.addOnImpressionListener(element, object: ViewHintListener{
                override fun onViewHint() {
                    if(element.isTopAds){
                        ImpresionTask().execute(element.trackerImageUrl)
                        val product = Product()
                        product.id = element.productId.toString()
                        product.name = element.productName
                        product.priceFormat = element.price
                        product.category = Category(element.departementId)
                        TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, adapterPosition)
                    } else{
                        InboxGtmTracker.getInstance().addInboxProductViewImpressions(element, adapterPosition)
                    }
                }
            })
        }
        productCardView.setOnClickListener {
            listener.onItemClickListener(element, adapterPosition)
            if (element.isTopAds) {
                ImpresionTask().execute(element.clickUrl)
                val product = Product()
                product.id = element.productId.toString()
                product.name = element.productName
                product.priceFormat = element.price
                product.category = Category(element.departementId)
                TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, adapterPosition)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recomendation
    }
}
