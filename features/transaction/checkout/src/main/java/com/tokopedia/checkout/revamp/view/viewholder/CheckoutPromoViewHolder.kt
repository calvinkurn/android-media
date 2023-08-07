package com.tokopedia.checkout.revamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckout.common.view.uimodel.PromoEntryPointSummaryItem
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

class CheckoutPromoViewHolder(private val binding: ItemCheckoutPromoBinding, private val listener: CheckoutAdapterListener) :
    RecyclerView.ViewHolder(binding.root) {

    private var isApplied = false

    fun bind(promo: LastApplyUiModel) {
        val titleValue: String

        when {
            promo.additionalInfo.messageInfo.message.isNotEmpty() -> {
                titleValue = promo.additionalInfo.messageInfo.message
                if (promo.additionalInfo.usageSummaries.isNotEmpty()) {
                    isApplied = true
                    listener.onSendAnalyticsViewPromoCheckoutApplied()
                } else {
                    isApplied = false
                }
            }
            promo.defaultEmptyPromoMessage.isNotBlank() -> {
                titleValue = promo.defaultEmptyPromoMessage
                isApplied = false
            }
            else -> {
                titleValue = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.promo_funnel_label)
                isApplied = false
            }
        }

        if (isApplied) {
            binding.btnCheckoutPromo.showApplied(
                titleValue,
                promo.additionalInfo.messageInfo.detail,
                IconUnify.CHEVRON_RIGHT,
                promo.additionalInfo.usageSummaries.map { PromoEntryPointSummaryItem(it.description, it.amountStr) },
                showConfetti = true
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
        } else {
            binding.btnCheckoutPromo.showActive(
                titleValue,
                IconUnify.CHEVRON_RIGHT
            ) {
                listener.onClickPromoCheckout(promo)
                listener.onSendAnalyticsClickPromoCheckout(isApplied, getAllPromosApplied(promo))
            }
        }

        // todo: testing flip
//        binding.btnCheckoutPromo.showActiveFlipping(
//            "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//            listOf("astaga 1, ini percobaan kalo textnya pannnjaaaaannggg sekaliiiii, astaga 2, ini percobaan kalo textnya pannnjaaaaannggg sekaliiiii", "wow bisa luar biasa sekali, tapi kenapa gak berhenti?"),
//            IconUnify.CHEVRON_RIGHT,
//            2_000,
//            4
//        )

        // todo: testing cart
//        binding.btnCheckoutPromo.showActive(
//            "makin hemat pakai promo",
//            IconUnify.CHEVRON_RIGHT
//        ) {
//            binding.btnCheckoutPromo.showInactive(
//                "Pilih barang dulu sebelum pakai promo"
//            ) {
//                binding.btnCheckoutPromo.showApplied(
//                    "Kamu bisa hemat Rp10.000", "1 promo dipakai", IconUnify.CHEVRON_RIGHT,
//                    emptyList()
//                )
//            }
//        }

        // todo: testing custom
//        binding.btnCheckoutPromo.showActive(
//            "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//            "gak bisa nih",
//            IconUnify.CHEVRON_DOWN
//        ) {
//            binding.btnCheckoutPromo.showError {
//                binding.btnCheckoutPromo.showInactive(
//                    "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                    "errr"
//                ) {
//                    binding.btnCheckoutPromo.showActive(
//                        "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                        "gak bisa nih",
//                        IconUnify.CHEVRON_DOWN,
//                        true,
//                        true
//                    ) {
//                        binding.btnCheckoutPromo.showActive(
//                            "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                            "gak bisa nih 2",
//                            IconUnify.CHEVRON_DOWN,
//                            true,
//                            true
//                        ) {
//                            binding.btnCheckoutPromo.showInactive(
//                                "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                                "errr",
//                                true
//                            ) {
//                                binding.btnCheckoutPromo.showApplied(
//                                    "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                                    "gak bisa nih 2",
//                                    IconUnify.CHEVRON_DOWN,
//                                    emptyList()
//                                ) {
//                                    binding.btnCheckoutPromo.showApplied(
//                                        "https://static.vecteezy.com/system/resources/previews/004/495/473/original/sales-promotion-line-icon-logo-free-vector.jpg",
//                                        "gak bisa nih 2",
//                                        IconUnify.CHEVRON_DOWN,
//                                        listOf(
//                                            PromoEntryPointSummaryItem("bebas ongkir", "rp20.000"),
//                                            PromoEntryPointSummaryItem("cashback gopay coins", "rp10.000")
//                                        )
//                                    ) {
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    private fun getAllPromosApplied(lastApplyData: LastApplyUiModel): List<String> {
        val listPromos = arrayListOf<String>()
        lastApplyData.codes.forEach {
            listPromos.add(it)
        }
        lastApplyData.voucherOrders.forEach {
            listPromos.add(it.code)
        }
        return listPromos
    }

    companion object {
        val VIEW_TYPE = R.layout.item_checkout_promo
    }
}
