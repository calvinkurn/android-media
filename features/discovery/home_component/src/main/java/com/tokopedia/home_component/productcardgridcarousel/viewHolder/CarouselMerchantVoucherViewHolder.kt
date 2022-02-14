package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.ContentFeaturedShopBigContentBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselFeaturedShopCardDataModel
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CarouselMerchantVoucherViewHolder (
    view: View,
    private val channels: ChannelModel
): AbstractViewHolder<CarouselFeaturedShopCardDataModel>(view) {

    private var binding: ContentFeaturedShopBigContentBinding? by viewBinding()
    companion object{
        val LAYOUT = R.layout.home_banner_item_merchant_voucher
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
        binding?.featuredShopProductImage?.show()
        binding?.featuredShopProductImage?.loadImageNoRounded(imageUrl, R.drawable.placeholder_grey)
    }

    private fun setTopAds(isTopAds: Boolean){
        binding?.featuredShopProductShopTopads?.shouldShowWithAction(isTopAds){}
    }

    private fun setShopLogo(imageUrl: String){
        binding?.featuredShopProductLogoShop?.let {
            it.shouldShowWithAction(imageUrl.isNotBlank()) {
                Glide.with(itemView)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder_rounded_grey)
                    .into(it)
            }
        }
    }

    private fun setShopBadge(imageUrl: String){
        binding?.featuredShopProductShopBadge?.shouldShowWithAction(imageUrl.isNotBlank()){
            binding?.featuredShopProductShopBadge?.loadImage(imageUrl, R.drawable.placeholder_rounded_grey)
        }
    }

    private fun setShopName(shopName: String){
        binding?.featuredShopProductShopName?.shouldShowWithAction(shopName.isNotBlank()){
            binding?.featuredShopProductShopName?.text = shopName
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
            binding?.featuredShopProductReviews?.show()
            binding?.featuredShopProductTotalCount?.show()
            binding?.featuredShopProductLocationIcon?.hide()
            binding?.featuredShopProductLocationName?.hide()
            binding?.featuredShopProductTotalCount?.text = "($reviewCount)"
            setImageRating(rating)
        } else {
            binding?.featuredShopProductReviews?.visibility = View.INVISIBLE
            binding?.featuredShopProductTotalCount?.visibility = View.INVISIBLE
        }
    }

    private fun setImageRating(rating: Int){
        binding?.featuredShopProductReviews1?.setImageResource(getRatingDrawable(rating >= 1))
        binding?.featuredShopProductReviews2?.setImageResource(getRatingDrawable(rating >= 2))
        binding?.featuredShopProductReviews3?.setImageResource(getRatingDrawable(rating >= 3))
        binding?.featuredShopProductReviews4?.setImageResource(getRatingDrawable(rating >= 4))
        binding?.featuredShopProductReviews5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    private fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) com.tokopedia.productcard.R.drawable.product_card_ic_rating_active
        else com.tokopedia.productcard.R.drawable.product_card_ic_rating_default
    }

    private fun setLocation(location: String){
        binding?.featuredShopProductReviews?.hide()
        binding?.featuredShopProductTotalCount?.hide()
        binding?.featuredShopProductLocationName?.shouldShowWithAction(location.isNotBlank()){
            binding?.featuredShopProductLocationIcon?.show()
            binding?.featuredShopProductLocationName?.text = location
        }
    }
}