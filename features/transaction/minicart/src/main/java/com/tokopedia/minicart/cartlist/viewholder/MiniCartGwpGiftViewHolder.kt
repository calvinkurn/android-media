package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
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
            updateData(element.giftList)
            setRibbonText(element.ribbonText)

            addOnImpressionListener(element) {
                listener?.onImpressProductGiftWidget(
                    offerId = element.offerId,
                    offerTypeId = element.offerTypeId,
                    productIds = element.giftList.map { it.id },
                    progressiveInfoText = element.progressiveInfoText,
                    position = element.position
                )
            }

            setupCtaClickListener(element.ctaText) {
                listener?.onClickCta(
                    offerId = element.offerId,
                    offerTypeId = element.offerTypeId,
                    progressiveInfoText = element.progressiveInfoText,
                    position = element.position
                )
                RouteManager.route(context, "tokopedia://now")
            }
        }
    }

    interface MiniCartGwpGiftListener {
        fun onImpressProductGiftWidget(
            offerId: Long,
            offerTypeId: Long,
            productIds: List<String>,
            progressiveInfoText: String,
            position: Int
        )
        fun onClickCta(
            offerId: Long,
            offerTypeId: Long,
            progressiveInfoText: String,
            position: Int
        )
    }
}

