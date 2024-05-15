package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.LegoProductCardDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.R
import com.tokopedia.home_component.analytics.sendEventRealtimeClickAdsByteIo
import com.tokopedia.home_component.analytics.sendEventShowAdsByteIo
import com.tokopedia.home_component.analytics.sendEventShowOverAdsByteIo
import com.tokopedia.home_component.databinding.LayoutLegoProductCardItemBinding
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
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

    private var channelGrid: ChannelGrid? = null

    fun bind(element: LegoProductCardDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: LegoProductCardDataModel){
        this.channelGrid = element.grid
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
            setOnClickListener(object: ProductCardClickListener {
                override fun onClick(v: View) {
                    if(element.grid.isTopads){
                        TopAdsUrlHitter(className).hitClickUrl(context, element.grid.productClickUrl,
                            element.grid.id,
                            element.grid.name,
                            element.grid.imageUrl,
                            element.componentName)
                    }
                    element.listener.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
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

        productCardView?.setVisibilityPercentListener(
            isTopAds = element.grid.isTopads,
            eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.grid.sendEventShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.grid.sendEventShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            }
        )
    }
}
