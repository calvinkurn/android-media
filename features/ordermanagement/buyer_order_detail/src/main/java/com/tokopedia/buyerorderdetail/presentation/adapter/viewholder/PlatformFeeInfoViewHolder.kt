package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailPlatformFeeInfoBinding
import com.tokopedia.buyerorderdetail.presentation.model.PlatformFeeInfoUiModel
import com.tokopedia.buyerorderdetail.presentation.model.StringRes
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.utils.view.binding.viewBinding

class PlatformFeeInfoViewHolder(
    itemView: View?,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<PlatformFeeInfoUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_platform_fee_info
    }

    private val binding by viewBinding<ItemBuyerOrderDetailPlatformFeeInfoBinding>()

    override fun bind(element: PlatformFeeInfoUiModel?) {
        if (element != null) {
            binding?.bindPlatformFeeInfoText(element.text)
        }
    }

    override fun bind(element: PlatformFeeInfoUiModel?, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun ItemBuyerOrderDetailPlatformFeeInfoBinding.bindPlatformFeeInfoText(text: StringRes) {
        tvBuyerOrderDetailPlatformFeeInfo.movementMethod = LinkMovementMethod.getInstance()
        tvBuyerOrderDetailPlatformFeeInfo.text = HtmlLinkHelper(
            context = root.context,
            htmlString = text.getStringValue(root.context)
        ).attachLinkClickListener().spannedString
    }

    private fun HtmlLinkHelper.attachLinkClickListener(): HtmlLinkHelper {
        return apply {
            val urlManager = urlList.firstOrNull()
            urlManager?.setOnClickListener { onPlatformFeeInfoLinkClicked(urlManager.linkUrl) }
        }
    }

    private fun onPlatformFeeInfoLinkClicked(linkUrl: String) {
        navigator.openAppLink(linkUrl, false)
    }
}
