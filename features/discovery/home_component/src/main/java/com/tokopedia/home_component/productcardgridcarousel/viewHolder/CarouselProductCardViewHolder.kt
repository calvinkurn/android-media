package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselProductCardDataModel
import com.tokopedia.home_component.R
import com.tokopedia.home_component.analytics.sendEventRealtimeClickAdsByteIo
import com.tokopedia.home_component.analytics.sendEventShowAdsByteIo
import com.tokopedia.home_component.analytics.sendEventShowOverAdsByteIo
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.listener.CommonProductCardCarouselListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.layout.ProductConstraintLayout
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
                element.listener?.onProductCardImpressed(position = adapterPosition, channel = channels, channelGrid = element.grid)
                    ?: listener?.onProductCardImpressed(position = element.grid.position, trackingAttributionModel = element.trackingAttributionModel, channelGrid = element.grid)
            }
            setVisibilityPercentListener(element.grid.isTopads, object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.grid.sendEventShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.grid.sendEventShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            })
            setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
                    if(element.grid.isTopads){
                        TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
                            element.grid.id,
                            element.grid.name,
                            element.grid.imageUrl,
                            element.componentName)
                    }
                    element.listener?.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
                        ?: listener?.onProductCardClicked(position = element.grid.position, trackingAttributionModel = element.trackingAttributionModel, channelGrid = element.grid, applink = element.applink)
                }

                override fun onAreaClicked(v: View) {
                    element.grid.sendEventRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    element.grid.sendEventRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    element.grid.sendEventRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }
            })
        }
    }
}
