package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.bmsm_widget.domain.entity.PageSource
import com.tokopedia.bmsm_widget.domain.entity.TierGifts
import com.tokopedia.bmsm_widget.presentation.bottomsheet.GiftListBottomSheet
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartGwpGiftUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartGwpGiftBinding

class MiniCartGwpGiftViewHolder(
    private val viewBinding: ItemMiniCartGwpGiftBinding,
    private val listener: MiniCartGwpGiftListener? = null
) : AbstractViewHolder<MiniCartGwpGiftUiModel>(viewBinding.root) {
    companion object {
        val LAYOUT = R.layout.item_mini_cart_gwp_gift
    }

    override fun bind(element: MiniCartGwpGiftUiModel) {
        viewBinding.root.apply {
            if (element.giftList.isNotEmpty()) {
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
                    val bottomSheet = GiftListBottomSheet.newInstance(
                        offerId = element.offerId,
                        warehouseId = element.warehouseId,
                        tierGifts = listOf(
                            TierGifts(
                                tierId = element.tierId,
                                gifts = element.giftList.map {
                                    TierGifts.GiftProduct(
                                        productId = it.id.toLongOrZero(),
                                        quantity = it.qty
                                    )
                                }
                            )
                        ),
                        pageSource = PageSource.MINICART_NOW,
                        shopId = element.shopId,
                        autoSelectTierChipByTierId = element.tierId,
                        mainProducts = listOf() // need to pass real main products data
                    )

                    listener?.onClickCta(
                        offerId = element.offerId,
                        offerTypeId = element.offerTypeId,
                        progressiveInfoText = element.progressiveInfoText,
                        position = element.position,
                        bottomSheet = bottomSheet
                    )
                }
                viewBinding.root.show()
            } else {
                viewBinding.root.hide()
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
            position: Int,
            bottomSheet: GiftListBottomSheet
        )
    }
}
