package com.tokopedia.buyerorderdetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.common.utils.BuyerOrderDetailNavigator
import com.tokopedia.buyerorderdetail.databinding.ItemBuyerOrderDetailInsuranceBinding
import com.tokopedia.buyerorderdetail.presentation.model.InsuranceUiModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class OrderInsuranceViewHolder(
    itemView: View,
    private val navigator: BuyerOrderDetailNavigator
) : AbstractViewHolder<InsuranceUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_buyer_order_detail_insurance
    }

    private val binding by viewBinding<ItemBuyerOrderDetailInsuranceBinding>()

    override fun bind(element: InsuranceUiModel?) {
        if (element != null) {
            binding?.bindLogo(element.logoUrl)
            binding?.bindTitle(element.title)
            binding?.bindSubtitle(element.subtitle)
            bindListener(element.appLink)
        }
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindLogo(logoUrl: String) {
        ivBuyerOrderDetailInsuranceLogo.loadImage(logoUrl)
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindTitle(title: String) {
        tvBuyerOrderDetailInsuranceTitle.text = title
    }

    private fun ItemBuyerOrderDetailInsuranceBinding.bindSubtitle(subtitle: String) {
        tvBuyerOrderDetailInsuranceSubtitle.text = subtitle
    }

    private fun bindListener(appLink: String) {
        itemView.setOnClickListener {
            navigator.openAppLink(appLink, false)
        }
    }
}
