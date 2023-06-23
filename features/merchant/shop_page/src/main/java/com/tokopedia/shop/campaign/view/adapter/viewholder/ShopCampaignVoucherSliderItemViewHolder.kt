package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.getNumberFormatted
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.domain.entity.ExclusiveLaunchVoucher
import com.tokopedia.shop.campaign.view.customview.ExclusiveLaunchVoucherView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.databinding.ShopCampaignVoucherSliderItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignVoucherSliderItemViewHolder(
    itemView: View,
    private val shopCampaignListener: ShopCampaignInterface,
    private val listener: Listener,
    private val itemCount: Int
) : RecyclerView.ViewHolder(itemView) {

    interface Listener {
        fun onCampaignVoucherSliderItemImpression(model: ExclusiveLaunchVoucher, position: Int)
        fun onCampaignVoucherSliderItemClick(model: ExclusiveLaunchVoucher, position: Int)
        fun onCampaignVoucherSliderItemCtaClaimClick(model: ExclusiveLaunchVoucher, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_campaign_voucher_slider_item
    }

    private val binding: ShopCampaignVoucherSliderItemBinding? by viewBinding()
    private var voucherView: ExclusiveLaunchVoucherView? = binding?.voucher

    fun bind(uiModel: ExclusiveLaunchVoucher?) {
        uiModel?.let {
            setVoucherViewData(it)
            setVoucherWidth()
        }
    }

    private fun setVoucherWidth() {
        voucherView?.layoutParams?.width = if (itemCount == Int.ONE) {
            RecyclerView.LayoutParams.MATCH_PARENT
        } else {
            itemView.resources.getDimension(R.dimen.voucher_slider_widget_item_width).toInt()
        }
    }

    private fun setVoucherViewData(uiModel: ExclusiveLaunchVoucher) {
        voucherView?.apply {
            setMinimumPurchase(
                context.getString(
                    R.string.shop_page_placeholder_minimal_purchase,
                    uiModel.minimumPurchase.getNumberFormatted()
                )
            )
            val remainingQuota = if (uiModel.remainingQuota.isMoreThanZero()) {
                context.getString(R.string.shop_page_placeholder_remaining_quota, uiModel.remainingQuota)
            } else {
                ""
            }
            setRemainingQuota(remainingQuota)
            setVoucherName(uiModel.voucherName)
            if (shopCampaignListener.isCampaignTabDarkMode()) useDarkBackground() else useLightBackground()
            setOnClickListener {
                listener.onCampaignVoucherSliderItemClick(
                    uiModel,
                    bindingAdapterPosition
                )
            }
            val ctaText = uiModel.buttonStr
            setOnPrimaryCtaClick {
                listener.onCampaignVoucherSliderItemCtaClaimClick(
                    uiModel,
                    bindingAdapterPosition
                )
            }
            setPrimaryCta(ctaText = ctaText, isClickable = !uiModel.isDisabledButton)
        }
    }

}
