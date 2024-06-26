package com.tokopedia.checkout.revamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCostBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellBinding
import com.tokopedia.checkout.databinding.ItemCheckoutEpharmacyBinding
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBinding
import com.tokopedia.checkout.databinding.ItemCheckoutPaymentBinding
import com.tokopedia.checkout.databinding.ItemCheckoutProductBenefitBinding
import com.tokopedia.checkout.databinding.ItemCheckoutProductBinding
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.checkout.databinding.ItemCheckoutTickerErrorBinding
import com.tokopedia.checkout.databinding.ItemCheckoutUpsellBinding
import com.tokopedia.checkout.revamp.view.address
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutAddressViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutButtonPaymentViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCostViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutCrossSellViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutEpharmacyViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutOrderViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutPaymentViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductBenefitViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutPromoViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutTickerErrorViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutTickerViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutUpsellViewHolder
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder

class CheckoutAdapter(
    private val listener: CheckoutAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CheckoutItem> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is CheckoutTickerErrorModel -> CheckoutTickerErrorViewHolder.VIEW_TYPE
            is CheckoutTickerModel -> TickerAnnouncementViewHolder.LAYOUT
            is CheckoutAddressModel -> CheckoutAddressViewHolder.VIEW_TYPE
            is CheckoutUpsellModel -> CheckoutUpsellViewHolder.VIEW_TYPE
            is CheckoutProductModel -> CheckoutProductViewHolder.VIEW_TYPE
            is CheckoutProductBenefitModel -> CheckoutProductBenefitViewHolder.VIEW_TYPE
            is CheckoutOrderModel -> CheckoutOrderViewHolder.VIEW_TYPE
            is CheckoutEpharmacyModel -> UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD
            is CheckoutPromoModel -> CheckoutPromoViewHolder.VIEW_TYPE
            is CheckoutPaymentModel -> CheckoutPaymentViewHolder.VIEW_TYPE
            is CheckoutCostModel -> CheckoutCostViewHolder.VIEW_TYPE
            is CheckoutCrossSellGroupModel -> CheckoutCrossSellViewHolder.VIEW_TYPE
            is CheckoutButtonPaymentModel -> CheckoutButtonPaymentViewHolder.VIEW_TYPE
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CheckoutTickerErrorViewHolder.VIEW_TYPE -> {
                CheckoutTickerErrorViewHolder(
                    ItemCheckoutTickerErrorBinding.inflate(inflater, parent, false)
                )
            }

            TickerAnnouncementViewHolder.LAYOUT -> {
                CheckoutTickerViewHolder(
                    inflater.inflate(
                        TickerAnnouncementViewHolder.LAYOUT,
                        parent,
                        false
                    )
                )
            }

            CheckoutAddressViewHolder.VIEW_TYPE -> {
                CheckoutAddressViewHolder(
                    ItemCheckoutAddressBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutUpsellViewHolder.VIEW_TYPE -> {
                CheckoutUpsellViewHolder(
                    ItemCheckoutUpsellBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutProductViewHolder.VIEW_TYPE -> {
                CheckoutProductViewHolder(
                    ItemCheckoutProductBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutProductBenefitViewHolder.VIEW_TYPE -> {
                CheckoutProductBenefitViewHolder(
                    ItemCheckoutProductBenefitBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutOrderViewHolder.VIEW_TYPE -> {
                CheckoutOrderViewHolder(
                    ItemCheckoutOrderBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD -> {
                CheckoutEpharmacyViewHolder(
                    ItemCheckoutEpharmacyBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutPromoViewHolder.VIEW_TYPE -> {
                CheckoutPromoViewHolder(ItemCheckoutPromoBinding.inflate(inflater, parent, false), listener)
            }

            CheckoutPaymentViewHolder.VIEW_TYPE -> {
                CheckoutPaymentViewHolder(ItemCheckoutPaymentBinding.inflate(inflater, parent, false), listener)
            }

            CheckoutCostViewHolder.VIEW_TYPE -> {
                CheckoutCostViewHolder(ItemCheckoutCostBinding.inflate(inflater, parent, false), inflater, listener)
            }

            CheckoutCrossSellViewHolder.VIEW_TYPE -> {
                CheckoutCrossSellViewHolder(ItemCheckoutCrossSellBinding.inflate(inflater, parent, false), listener)
            }

            else -> {
                CheckoutButtonPaymentViewHolder(ItemCheckoutButtonPaymentBinding.inflate(inflater, parent, false), listener)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
            is CheckoutTickerErrorModel -> {
                (holder as CheckoutTickerErrorViewHolder).bind(item)
            }

            is CheckoutTickerModel -> {
                (holder as CheckoutTickerViewHolder).bind(item)
            }

            is CheckoutAddressModel -> {
                (holder as CheckoutAddressViewHolder).bind(item)
            }

            is CheckoutUpsellModel -> {
                (holder as CheckoutUpsellViewHolder).bind(item.upsell)
            }

            is CheckoutProductModel -> {
                (holder as CheckoutProductViewHolder).bind(item)
            }

            is CheckoutProductBenefitModel -> {
                (holder as CheckoutProductBenefitViewHolder).bind(item)
            }

            is CheckoutOrderModel -> {
                (holder as CheckoutOrderViewHolder).bind(item, list.address())
            }

            is CheckoutEpharmacyModel -> {
                (holder as CheckoutEpharmacyViewHolder).bindViewHolder(item.epharmacy)
            }

            is CheckoutPromoModel -> {
                (holder as CheckoutPromoViewHolder).bind(item)
            }

            is CheckoutPaymentModel -> {
                (holder as CheckoutPaymentViewHolder).bind(item)
            }

            is CheckoutCostModel -> {
                (holder as CheckoutCostViewHolder).bind(item)
            }

            is CheckoutCrossSellGroupModel -> {
                (holder as CheckoutCrossSellViewHolder).bind(item)
            }

            is CheckoutButtonPaymentModel -> {
                (holder as CheckoutButtonPaymentViewHolder).bind(item)
            }

            else -> {
            }
        }
    }

    val uploadPrescriptionPosition: Int
        get() {
            return list.indexOfLast { it is CheckoutEpharmacyModel }
        }
}
