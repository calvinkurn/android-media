package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import kotlinx.android.synthetic.main.content_featured_shop_big_content.view.*

/**
 * Created by Lukas on 07/09/20.
 */

class CarouselFeaturedShopViewHolder (
        view: View,
        private val channels: ChannelModel
): AbstractViewHolder<CarouselFeaturedShopCardDataModel>(view) {

    companion object{
        val LAYOUT = R.layout.content_featured_shop_big_content
    }

    override fun bind(element: CarouselFeaturedShopCardDataModel) {
        setLayout(element)
        setListener(element)
    }

    private fun setLayout(element: CarouselFeaturedShopCardDataModel){
        setImageShop(element.grid.imageUrl)
        setTopAds(element.grid.isTopads)
        setShopLogo(element.grid.shopProfileUrl)
        setShopBadge(element.grid.shopBadgeUrl)
        setShopName(element.grid.shopName)
        setContextualInfo(channels.contextualInfo, element)
    }

    private fun setListener(element: CarouselFeaturedShopCardDataModel){
        itemView.addOnImpressionListener(element.impressHolder) {
            if(element.grid.isTopads){
                TopAdsUrlHitter(itemView.context).hitImpressionUrl(this::class.java.simpleName, element.grid.impression,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
            }
            element.listener.onProductCardImpressed(position = adapterPosition, channel = channels, channelGrid = element.grid)
        }
        itemView.setOnClickListener {
            if(element.grid.isTopads){
                TopAdsUrlHitter(itemView.context).hitImpressionUrl(this::class.java.simpleName, element.grid.productClickUrl,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
            }
            element.listener.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
        }
    }

    private fun setImageShop(imageUrl:String){
        itemView.featured_shop_product_image?.loadImageNoRounded(imageUrl, R.drawable.placeholder_grey)
    }

    private fun setTopAds(isTopAds: Boolean){
        itemView.featured_shop_product_shop_topads?.shouldShowWithAction(isTopAds){}
    }

    private fun setShopLogo(imageUrl: String){
        itemView.featured_shop_product_logo_shop?.loadImage(imageUrl, R.drawable.placeholder_grey)
    }

    private fun setShopBadge(imageUrl: String){
        itemView.featured_shop_product_shop_badge?.loadImage(imageUrl, R.drawable.placeholder_grey)
    }

    private fun setShopName(shopName: String){
        itemView.featured_shop_product_shop_name.text = shopName
    }

    private fun setContextualInfo(contextualInfo: Int, dataModel: CarouselFeaturedShopCardDataModel){
        when(contextualInfo){
            1 -> setRating(dataModel.grid.rating, dataModel.grid.countReview)
            2 -> setLocation(dataModel.grid.shopLocation)
        }
    }

    private fun setRating(rating: Int, reviewCount: Int){
        itemView.featured_shop_product_reviews?.show()
        itemView.featured_shop_product_total_count?.show()
        itemView.featured_shop_product_total_count?.text = "($reviewCount)"
    }

    private fun setLocation(location: String){
        itemView.featured_shop_product_location_name?.shouldShowWithAction(location.isNotBlank()){
            itemView.featured_shop_product_location_icon?.show()
            itemView.featured_shop_product_location_name?.text = location
        }
    }
}