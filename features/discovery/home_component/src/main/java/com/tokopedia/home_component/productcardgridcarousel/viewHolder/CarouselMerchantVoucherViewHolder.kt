package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import com.tokopedia.imageassets.TokopediaImageUrl

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
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CarouselMerchantVoucherViewHolder (
    view: View,
    private val cardInteraction: Boolean = false
): AbstractViewHolder<CarouselMerchantVoucherDataModel>(view) {

    private var binding: HomeBannerItemMerchantVoucherBinding? by viewBinding()
    companion object{
        val LAYOUT = R.layout.home_banner_item_merchant_voucher
        private const val BACKGROUND_MVC_WHITE = TokopediaImageUrl.BACKGROUND_MVC_WHITE
        private const val BACKGROUND_MVC_DARK = TokopediaImageUrl.BACKGROUND_MVC_DARK
    }

    override fun bind(element: CarouselMerchantVoucherDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselMerchantVoucherDataModel){
        if (itemView.context.isDarkMode()) {
            binding?.imageDividerVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            binding?.imageBackgroundVoucher?.loadImageNoRounded(
                BACKGROUND_MVC_DARK,
                com.tokopedia.topads.sdk.R.drawable.placeholder_grey
            )
        }
        else {
            binding?.imageDividerVoucher?.setColorFilter(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN50))
            binding?.imageBackgroundVoucher?.loadImageNormal(
                BACKGROUND_MVC_WHITE,
                com.tokopedia.topads.sdk.R.drawable.placeholder_grey
            )
        }
        binding?.shopName?.text = element.shopName.parseAsHtml()
        binding?.titleBenefit?.text = element.benefit
        binding?.benefitPrice?.text = element.benefitPrice
        binding?.totalOtherCoupon?.text = element.totalOtherCoupon
        if (element.iconBadge.isNotBlank()) {
            binding?.imageBadge?.visible()
            binding?.shopName?.setMargin(4f.toDpInt(), 7f.toDpInt(), 0f.toDpInt(), 0f.toDpInt())
            binding?.imageBadge?.loadImageNoRounded(
                element.iconBadge,
                com.tokopedia.topads.sdk.R.drawable.placeholder_grey,
                listener = object : ImageHandler.ImageLoaderStateListener {
                    override fun successLoad() {}

                    override fun failedLoad() {
                        failedLoadShopBadge()
                    }
                })
        } else {
            failedLoadShopBadge()
        }
        binding?.imageProduct?.loadImageNoRounded(element.imageProduct, com.tokopedia.topads.sdk.R.drawable.placeholder_grey)
        binding?.containerShop?.setOnClickListener {
            element.merchantVoucherComponentListener.onShopClicked(element, adapterPosition)
        }
        binding?.cardContainerMvc?.apply {
            setCardUnifyBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            cardType = CardUnify2.TYPE_CLEAR
            animateOnPress = if(cardInteraction) CardUnify2.ANIMATE_BOUNCE else CardUnify2.ANIMATE_NONE
            setOnClickListener {
                element.merchantVoucherComponentListener.onProductClicked(element, adapterPosition)
            }
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
