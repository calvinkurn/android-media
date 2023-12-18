package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartGwpGiftBinding

class MiniCartGwpGiftViewHolder (
    private val viewBinding: ItemMiniCartGwpGiftBinding,
) : AbstractViewHolder<MiniCartGwpGiftUiModel>(viewBinding.root) {
    companion object {
        val LAYOUT = R.layout.item_mini_cart_gwp_gift
    }
    override fun bind(element: MiniCartGwpGiftUiModel) {
        viewBinding.root.apply {
            updateData(element.giftList)
            setRibbonText(element.ribbonText)
            setupCtaClickListener(element.ctaText) {
                RouteManager.route(context, "tokopedia://now")
            }
        }
    }
}

