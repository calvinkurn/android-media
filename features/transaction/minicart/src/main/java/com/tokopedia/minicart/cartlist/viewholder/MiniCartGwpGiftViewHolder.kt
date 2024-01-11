package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.bmsm_widget.presentation.adapter.viewholder.ProductGiftViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartGwpGiftBinding

class MiniCartGwpGiftViewHolder (
    private val viewBinding: ItemMiniCartGwpGiftBinding,
    private val listener: MiniCartGwpGiftListener? = null
) : AbstractViewHolder<MiniCartGwpGiftUiModel>(viewBinding.root) {
    companion object {
        val LAYOUT = R.layout.item_mini_cart_gwp_gift
    }

    override fun bind(element: MiniCartGwpGiftUiModel) {
        viewBinding.root.apply {
            setupProductGiftListener(object : ProductGiftViewHolder.ProductGiftListener {
                override fun onImpressProductGift() {
                    listener?.onImpressProductGift(element.offerId)
                }
            })
            setupCtaClickListener(element.ctaText) {
                listener?.onClickCta(element.offerId)
                RouteManager.route(context, "tokopedia://now")
            }
            updateData(element.giftList)
            setRibbonText(element.ribbonText)
        }
    }

    interface MiniCartGwpGiftListener {
        fun onImpressProductGift(offerId: Long)
        fun onClickCta(offerId: Long)
    }
}

