package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcMerchantVoucherBinding
import com.tokopedia.home_component.databinding.GlobalDcMixTopBinding
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.visitable.MerchantVoucherDataModel
import com.tokopedia.utils.view.binding.viewBinding

class MerchantVoucherViewHolder(
    itemView: View
) : AbstractViewHolder<MerchantVoucherDataModel>(itemView) {
    private var binding: GlobalDcMerchantVoucherBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_merchant_voucher
        val RECYCLER_VIEW_ID = R.id.recycleList
        private const val FPM_MIX_LEFT = "home_merchant_voucher"
    }

    override fun bind(element: MerchantVoucherDataModel) {
        setHeaderComponent(element = element)
    }

    private fun setChannelDivider(element: MerchantVoucherDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: MerchantVoucherDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }

            override fun onChannelExpired(channelModel: ChannelModel) {

            }
        })
    }
}