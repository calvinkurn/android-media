package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

class CheckoutPromoViewHolder(private val binding: ItemCheckoutPromoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(promo: LastApplyUiModel) {
        binding.btnCheckoutPromo.showActive(
            "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
            "gak bisa nih",
            IconUnify.CHEVRON_DOWN
        ) {
            binding.btnCheckoutPromo.showError {
                binding.btnCheckoutPromo.showInactive(
                    "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                    "errr"
                ) {
                    binding.btnCheckoutPromo.showActive(
                        "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                        "gak bisa nih",
                        IconUnify.CHEVRON_DOWN,
                        true,
                        true
                    ) {
                        binding.btnCheckoutPromo.showActive(
                            "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                            "gak bisa nih 2",
                            IconUnify.CHEVRON_DOWN,
                            true,
                            true
                        ) {
                            binding.btnCheckoutPromo.showInactive(
                                "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                                "errr",
                                true
                            ) {
                                binding.btnCheckoutPromo.showApplied(
                                    "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                                    "gak bisa nih 2",
                                    IconUnify.CHEVRON_DOWN,
                                    emptyList()
                                ) {
                                    binding.btnCheckoutPromo.showApplied(
                                        "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
                                        "gak bisa nih 2",
                                        IconUnify.CHEVRON_DOWN,
                                        listOf(
                                            PromoEntryPointSummaryItem("bebas ongkir", "rp20.000"),
                                            PromoEntryPointSummaryItem("cashback gopay coins", "rp10.000")
                                        )
                                    ) {

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_promo
    }
}
