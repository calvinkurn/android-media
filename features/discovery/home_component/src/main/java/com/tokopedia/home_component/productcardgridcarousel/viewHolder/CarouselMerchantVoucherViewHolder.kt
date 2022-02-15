package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemMerchantVoucherBinding
import com.tokopedia.home_component.listener.MerchantVoucherComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CarouselMerchantVoucherViewHolder (
    view: View,
    private val merchantVoucherComponentListener: MerchantVoucherComponentListener
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
        binding?.shopName?.text = element.shopName
        binding?.titleBenefit?.text = element.benefit
        binding?.benefitPrice?.text = element.benefitPrice
        binding?.totalOtherCoupon?.text = element.totalOtherCoupon
        binding?.imageBadge?.loadImageNoRounded(element.iconBadge, com.tokopedia.home_component.R.drawable.placeholder_grey)
        binding?.imageProduct?.loadImageNoRounded(element.imageProduct, com.tokopedia.home_component.R.drawable.placeholder_grey)
        binding?.containerShop?.setOnClickListener {
            merchantVoucherComponentListener.onShopClicked(element.shopAppLink)
        }
        binding?.containerProduct?.setOnClickListener {
            merchantVoucherComponentListener.onProductClicked(element.productAppLink)
        }
    }
}