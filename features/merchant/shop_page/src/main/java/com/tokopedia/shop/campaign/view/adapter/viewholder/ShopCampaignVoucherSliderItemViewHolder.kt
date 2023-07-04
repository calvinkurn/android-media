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
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignVoucherSliderItemViewHolder(
    itemView: View,
    private val shopCampaignListener: ShopCampaignInterface,
    private val listener: Listener,
    private val itemCount: Int,
    private val parentUiModel: ShopWidgetVoucherSliderUiModel?
) : RecyclerView.ViewHolder(itemView) {

    interface Listener {
        fun onCampaignVoucherSliderItemImpression(model: ExclusiveLaunchVoucher, position: Int)
        fun onCampaignVoucherSliderItemClick(
            parentUiModel: ShopWidgetVoucherSliderUiModel,
            model: ExclusiveLaunchVoucher,
            position: Int
        )
        fun onCampaignVoucherSliderItemCtaClaimClick(
            parentUiModel: ShopWidgetVoucherSliderUiModel,
            model: ExclusiveLaunchVoucher,
            position: Int
        )
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
            setRemainingQuota(uiModel.remainingQuota)
            setVoucherName(uiModel.voucherName)

            val isClaimCtaCtaDisabled = uiModel.isDisabledButton
            if (shopCampaignListener.isCampaignTabDarkMode()) {
                useDarkBackground(isClaimCtaCtaDisabled)
            } else {
                useLightBackground(isClaimCtaCtaDisabled)
            }

            setOnClickListener {
                parentUiModel?.let {
                    listener.onCampaignVoucherSliderItemClick(
                        it,
                        uiModel,
                        bindingAdapterPosition
                    )
                }
            }
            setOnPrimaryCtaClick {
                parentUiModel?.let {
                    listener.onCampaignVoucherSliderItemCtaClaimClick(
                        it,
                        uiModel,
                        bindingAdapterPosition
                    )
                }
            }
            setPrimaryCta(voucherCode = uiModel.couponCode, isDisabledButton = uiModel.isDisabledButton)
        }
    }

}
