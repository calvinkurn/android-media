package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

class CarouselProductCardViewHolder (
    view: View,
    private val channels: ChannelModel,
    val listener: CommonProductCardCarouselListener? = null,
): AbstractViewHolder<CarouselProductCardDataModel>(view) {

    companion object{
        val LAYOUT = R.layout.home_banner_item_flashsale_carousel
        private const val className = "com.tokopedia.home_component.productcardgridcarousel.viewHolder.CarouselProductCardViewHolder"
    }

    private val productCardView: ProductCardGridView? by lazy { view.findViewById<ProductCardGridView>(R.id.productCardView) }
    override fun bind(element: CarouselProductCardDataModel) {
        setLayout(itemView.context, element)
    }

    private fun setLayout(context: Context, element: CarouselProductCardDataModel){
        productCardView?.run{
            applyCarousel()
            setProductModel(element.productModel)
            addOnImpressionListener(element.impressHolder) {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitImpressionUrl(context, element.grid.impression,
                            element.grid.id,
                            element.grid.name,
                            element.grid.imageUrl,
                            element.componentName)
                }
                listener?.onProductCardImpressed(position = adapterPosition, channel = channels, channelGrid = element.grid)
            }
            setOnClickListener {
                if(element.grid.isTopads){
                    TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
                            element.grid.id,
                            element.grid.name,
                            element.grid.imageUrl,
                            element.componentName)
                }
                listener?.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
            }
        }
    }
}
