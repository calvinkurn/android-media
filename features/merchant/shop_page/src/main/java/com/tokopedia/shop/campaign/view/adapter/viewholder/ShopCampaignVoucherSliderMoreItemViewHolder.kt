package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.R
import com.tokopedia.shop.campaign.view.customview.ExclusiveLaunchMoreVoucherView
import com.tokopedia.shop.campaign.view.listener.ShopCampaignInterface
import com.tokopedia.shop.campaign.view.model.ExclusiveLaunchMoreVoucherUiModel
import com.tokopedia.shop.databinding.ShopCampaignVoucherSliderMoreItemBinding
import com.tokopedia.shop.home.view.model.ShopWidgetVoucherSliderUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopCampaignVoucherSliderMoreItemViewHolder(
    itemView: View,
    private val shopCampaignListener: ShopCampaignInterface,
    private val listener: Listener,
    private val parentUiModel: ShopWidgetVoucherSliderUiModel?
) : RecyclerView.ViewHolder(itemView) {

    interface Listener {
        fun onCampaignVoucherSliderMoreItemClick(listCategorySlug: List<String>, parentUiModel: ShopWidgetVoucherSliderUiModel?)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shop_campaign_voucher_slider_more_item
    }
    private val binding: ShopCampaignVoucherSliderMoreItemBinding? by viewBinding()
    private var moreVoucherView: ExclusiveLaunchMoreVoucherView? = binding?.moreVoucherView

    fun bind(uiModel: ExclusiveLaunchMoreVoucherUiModel?) {
        setMoreVoucherViewData(uiModel)
    }

    private fun setMoreVoucherViewData(uiModel: ExclusiveLaunchMoreVoucherUiModel?) {
        moreVoucherView?.apply {
            setRemainingVoucher(uiModel?.totalRemainingVoucher.orZero())
            configColorMode(shopCampaignListener.isCampaignTabDarkMode())
            setOnClickListener {
                listener.onCampaignVoucherSliderMoreItemClick(
                    uiModel?.listCategorySlug.orEmpty(),
                    parentUiModel
                )
            }
        }
    }

}
