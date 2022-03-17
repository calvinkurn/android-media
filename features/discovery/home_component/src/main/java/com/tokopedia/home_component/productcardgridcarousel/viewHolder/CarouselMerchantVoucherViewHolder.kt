package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeBannerItemMerchantVoucherBinding
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.home_component.util.ImageHandler
import com.tokopedia.home_component.util.loadImageNoRounded
import com.tokopedia.home_component.util.loadImageNormal
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
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
        private const val BACKGROUND_MVC_WHITE = "https://images.tokopedia.net/img/android/home/mvc_light_mode.png"
        private const val BACKGROUND_MVC_DARK = "https://images.tokopedia.net/img/android/home/mvc_dark_mode.png"
    }

    override fun bind(element: CarouselMerchantVoucherDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselMerchantVoucherDataModel){
        if (itemView.context.isDarkMode()) {
            binding?.imageDividerVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N150))
            binding?.imageBackgroundVoucher?.loadImageNoRounded(
                BACKGROUND_MVC_DARK,
                com.tokopedia.home_component.R.drawable.placeholder_grey
            )
        }
        else {
            binding?.imageDividerVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N75))
            binding?.imageBackgroundVoucher?.loadImageNormal(
                BACKGROUND_MVC_WHITE,
                com.tokopedia.home_component.R.drawable.placeholder_grey
            )
        }
        binding?.shopName?.text = element.shopName
        binding?.titleBenefit?.text = element.benefit
        binding?.benefitPrice?.text = element.benefitPrice
        binding?.totalOtherCoupon?.text = element.totalOtherCoupon
        if (element.iconBadge.isNotBlank()) {
            binding?.imageBadge?.visible()
            binding?.shopName?.setMargin(4f.toDpInt(), 7f.toDpInt(), 0f.toDpInt(), 0f.toDpInt())
            binding?.imageBadge?.loadImageNoRounded(
                element.iconBadge,
                com.tokopedia.home_component.R.drawable.placeholder_grey,
                listener = object : ImageHandler.ImageLoaderStateListener {
                    override fun successLoad() {}

                    override fun failedLoad() {
                        failedLoadShopBadge()
                    }
                })
        } else {
            failedLoadShopBadge()
        }
        binding?.imageProduct?.loadImageNoRounded(element.imageProduct, com.tokopedia.home_component.R.drawable.placeholder_grey)
        binding?.containerShop?.setOnClickListener {
            element.merchantVoucherComponentListener.onShopClicked(element, adapterPosition)
        }
        binding?.containerProduct?.setOnClickListener {
            element.merchantVoucherComponentListener.onProductClicked(element, adapterPosition)
        }
        itemView.addOnImpressionListener(element.impressHolder) {
            element.merchantVoucherComponentListener.onMerchantImpressed(element, adapterPosition)
        }
    }

    private fun failedLoadShopBadge() {
        binding?.imageBadge?.gone()
        binding?.shopName?.setMargin(15f.toDpInt(), 7f.toDpInt(), 0f.toDpInt(), 0f.toDpInt())
    }
}