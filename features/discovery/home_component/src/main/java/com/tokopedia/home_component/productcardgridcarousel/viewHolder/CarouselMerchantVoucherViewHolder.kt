package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemMerchantVoucherBinding
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherImpressed
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CarouselMerchantVoucherViewHolder (
    view: View
): AbstractViewHolder<CarouselMerchantVoucherDataModel>(view) {

    private var binding: HomeBannerItemMerchantVoucherBinding? by viewBinding()
    companion object{
        val LAYOUT = R.layout.home_banner_item_merchant_voucher
    }

    override fun bind(element: CarouselMerchantVoucherDataModel) {
        binding?.imageBackground
        setLayout(element)
    }

    private fun setLayout(element: CarouselMerchantVoucherDataModel){
        if (itemView.context.isDarkMode()) {
            binding?.imageBackgroundVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
            binding?.imageDividerVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N150))
        }
        binding?.shopName?.text = element.shopName
        binding?.titleBenefit?.text = element.benefit
        binding?.benefitPrice?.text = element.benefitPrice
        binding?.totalOtherCoupon?.text = element.totalOtherCoupon
        binding?.imageBadge?.loadImageNoRounded(element.iconBadge, com.tokopedia.home_component.R.drawable.placeholder_grey)
        binding?.imageProduct?.loadImageNoRounded(element.imageProduct, com.tokopedia.home_component.R.drawable.placeholder_grey)
        binding?.containerShop?.setOnClickListener {
            val horizontalPosition = "${adapterPosition + 1}"
            element.merchantVoucherComponentListener.onShopClicked(
                MerchantVoucherShopClicked(
                    shopAppLink = element.shopAppLink,
                    shopId = element.shopId,
                    shopName = element.shopName,
                    horizontalCardPosition = horizontalPosition,
                    bannerId = element.bannerId,
                    positionWidget = element.positionWidget,
                    headerName = element.headerName,
                    userId = element.userId
                )
            )
        }
        binding?.containerProduct?.setOnClickListener {
            val horizontalPosition = "${adapterPosition + 1}"
            element.merchantVoucherComponentListener.onVoucherDetailClicked(
                MerchantVoucherDetailClicked(
                    productAppLink = element.productAppLink,
                    shopId = element.shopId,
                    shopName = element.shopName,
                    horizontalCardPosition = horizontalPosition,
                    bannerId = element.bannerId,
                    positionWidget = element.positionWidget,
                    headerName = element.headerName,
                    userId = element.userId,
                    couponCode = element.couponCode,
                    couponType = element.couponType,
                    creativeName = element.creativeName
                )
            )
        }
        binding?.containerImageProduct?.setOnClickListener {
            val horizontalPosition = "${adapterPosition + 1}"
            element.merchantVoucherComponentListener.onProductClicked(
                MerchantVoucherProductClicked(
                    productAppLink = element.productAppLink,
                    shopId = element.shopId,
                    shopName = element.shopName,
                    horizontalCardPosition = horizontalPosition,
                    bannerId = element.bannerId,
                    positionWidget = element.positionWidget,
                    headerName = element.headerName,
                    userId = element.userId,
                    productId = element.productId,
                    productName = element.productName,
                    productVariant = element.productVariant,
                    productPrice = element.productPrice,
                    buType = element.buType,
                    topAds = element.topAds,
                    carousel = element.carousel,
                    recommendationType = element.recommendationType,
                    recomPageName = element.recomPageName,
                    productBrand = element.productBrand,
                    catNameLevel1 = element.catNameLevel1,
                    catNameLevel2 = element.catNameLevel2,
                    catNameLevel3 = element.catNameLevel3
                )
            )
        }
        itemView.addOnImpressionListener(element.impressHolder) {
            val horizontalPosition = "${adapterPosition + 1}"
            element.merchantVoucherComponentListener.onMerchantImpressed(
                MerchantVoucherImpressed(
                    couponCode = element.couponCode,
                    couponType = element.couponType,
                    creativeName = element.creativeName,
                    cardPositionHorizontal = horizontalPosition,
                    bannerId = element.bannerId,
                    shopId = element.shopId,
                    positionWidget = element.positionWidget,
                    headerName = element.headerName,
                    userId = element.userId
                )
            )
        }
    }
}