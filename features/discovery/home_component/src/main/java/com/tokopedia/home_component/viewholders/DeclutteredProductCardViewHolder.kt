package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.databinding.LayoutDeclutteredProductCardItemBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.DeclutteredProductCardDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R

/**
 * Created by frenzel
 */
class DeclutteredProductCardViewHolder(
    v: View,
    private val channels: ChannelModel
): RecyclerView.ViewHolder(v) {
    companion object {
        val LAYOUT = R.layout.layout_decluttered_product_card_item
        private const val className = "com.tokopedia.home_component.visitable.DeclutteredProductCardViewHolder"
    }

    private val binding: LayoutDeclutteredProductCardItemBinding? by viewBinding()

    private val productCardView: ProductCardGridView? by lazy { binding?.declutteredProductCardView }

    fun bind(element: DeclutteredProductCardDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: DeclutteredProductCardDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element) {
//                if(element.grid.isTopads){
//                    TopAdsUrlHitter(className).hitImpressionUrl(context, element.grid.impression,
//                        element.grid.id,
//                        element.grid.name,
//                        element.grid.imageUrl,
//                        element.componentName)
//                }
                element.listener.onProductCardImpressed(position = adapterPosition, channel = channels, channelGrid = element.grid)
            }
            setOnClickListener {
//                if(element.grid.isTopads){
//                    TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
//                        element.grid.id,
//                        element.grid.name,
//                        element.grid.imageUrl,
//                        element.componentName)
//                }
                element.listener.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
            }
        }
    }
}
