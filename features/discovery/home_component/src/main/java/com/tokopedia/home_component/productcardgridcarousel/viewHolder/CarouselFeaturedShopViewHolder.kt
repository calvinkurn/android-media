package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.kotlin.extensions.view.*
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
        setShopLogo(element.grid.shop.shopProfileUrl)
        setShopBadge(element.grid.shop.shopBadgeUrl)
        setShopName(element.grid.shop.shopName)
        setContextualInfo(channels.contextualInfo, element)
    }

    private fun setListener(element: CarouselFeaturedShopCardDataModel){
        itemView.addOnImpressionListener(element.impressHolder) {
            if(element.grid.impression.isNotBlank()){
                TopAdsUrlHitter(itemView.context).hitImpressionUrl(this::class.java.simpleName, element.grid.impression,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
            }
            element.listener.onProductCardImpressed(position = adapterPosition, channel = channels, channelGrid = element.grid)
        }
        itemView.setOnClickListener {
            if(element.grid.productClickUrl.isNotBlank()){
                TopAdsUrlHitter(itemView.context).hitClickUrl(this::class.java.simpleName, element.grid.productClickUrl,
                        element.grid.id,
                        element.grid.name,
                        element.grid.imageUrl,
                        element.componentName)
            }
            element.listener.onProductCardClicked(position = adapterPosition, channel = channels, channelGrid = element.grid, applink = element.applink)
        }
    }

    private fun setImageShop(imageUrl:String){
        itemView.featured_shop_product_image?.show()
        itemView.featured_shop_product_image?.loadImageNoRounded(imageUrl, R.drawable.placeholder_grey)
    }

    private fun setTopAds(isTopAds: Boolean){
        itemView.featured_shop_product_shop_topads?.shouldShowWithAction(isTopAds){}
    }

    private fun setShopLogo(imageUrl: String){
        itemView.featured_shop_product_logo_shop?.shouldShowWithAction(imageUrl.isNotBlank()){
            Glide.with(itemView)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder_rounded_grey)
                    .into(itemView.featured_shop_product_logo_shop)
        }
    }

    private fun setShopBadge(imageUrl: String){
        itemView.featured_shop_product_shop_badge?.shouldShowWithAction(imageUrl.isNotBlank()){
            itemView.featured_shop_product_shop_badge?.loadImage(imageUrl, R.drawable.placeholder_rounded_grey)
        }
    }

    private fun setShopName(shopName: String){
        itemView.featured_shop_product_shop_name.shouldShowWithAction(shopName.isNotBlank()){
            itemView.featured_shop_product_shop_name.text = shopName
        }
    }

    private fun setContextualInfo(contextualInfo: Int, dataModel: CarouselFeaturedShopCardDataModel){
        when(contextualInfo){
            1 -> setRating(dataModel.grid.rating, dataModel.grid.countReviewFormat)
            2 -> setLocation(dataModel.grid.shop.shopLocation)
        }
    }

    private fun setRating(rating: Int, reviewCount: String){
        if(rating > 0) {
            itemView.featured_shop_product_reviews?.show()
            itemView.featured_shop_product_total_count?.show()
            itemView.featured_shop_product_location_icon?.hide()
            itemView.featured_shop_product_location_name?.hide()
            itemView.featured_shop_product_total_count?.text = "($reviewCount)"
            setImageRating(rating)
        } else {
            itemView.featured_shop_product_reviews?.visibility = View.INVISIBLE
            itemView.featured_shop_product_total_count?.visibility = View.INVISIBLE
        }
    }

    private fun setImageRating(rating: Int){
        itemView.featured_shop_product_reviews_1?.setImageResource(getRatingDrawable(rating >= 1))
        itemView.featured_shop_product_reviews_2?.setImageResource(getRatingDrawable(rating >= 2))
        itemView.featured_shop_product_reviews_3?.setImageResource(getRatingDrawable(rating >= 3))
        itemView.featured_shop_product_reviews_4?.setImageResource(getRatingDrawable(rating >= 4))
        itemView.featured_shop_product_reviews_5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    private fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) com.tokopedia.productcard.R.drawable.product_card_ic_rating_active
        else com.tokopedia.productcard.R.drawable.product_card_ic_rating_default
    }

    private fun setLocation(location: String){
        itemView.featured_shop_product_reviews?.hide()
        itemView.featured_shop_product_total_count?.hide()
        itemView.featured_shop_product_location_name?.shouldShowWithAction(location.isNotBlank()){
            itemView.featured_shop_product_location_icon?.show()
            itemView.featured_shop_product_location_name?.text = location
        }
    }
}