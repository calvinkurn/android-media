package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.LegoProductCardDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.LayoutLegoProductCardItemBinding
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by frenzel
 */
class LegoProductCardViewHolder(
    v: View,
    private val channels: ChannelModel
): RecyclerView.ViewHolder(v) {
    companion object {
        val LAYOUT = R.layout.layout_lego_product_card_item
        private const val className = "com.tokopedia.home_component.visitable.DeclutteredProductCardViewHolder"
    }

    private val binding: LayoutLegoProductCardItemBinding? by viewBinding()

    private val productCardView: ProductCardGridView? by lazy { binding?.productCard }

    fun bind(element: LegoProductCardDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: LegoProductCardDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element) {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitImpressionUrl(context, element.grid.impression,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
                }
            }
            setOnClickListener {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
                }
                element.listener.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
            }
        }
    }
}
