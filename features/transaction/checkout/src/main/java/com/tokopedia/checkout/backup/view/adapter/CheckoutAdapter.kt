package com.tokopedia.checkout.backup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.backup.view.address
import com.tokopedia.checkout.backup.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutItem
import com.tokopedia.checkout.backup.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutProductBenefitModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.backup.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.backup.view.viewholder.CheckoutAddressViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutButtonPaymentViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutCostViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutCrossSellViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutEpharmacyViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutOrderViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutProductBenefitViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutPromoViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutTickerErrorViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutTickerViewHolder
import com.tokopedia.checkout.backup.view.viewholder.CheckoutUpsellViewHolder
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.databinding.ItemCheckoutButtonPaymentBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCostBackupBinding
import com.tokopedia.checkout.databinding.ItemCheckoutCrossSellBinding
import com.tokopedia.checkout.databinding.ItemCheckoutEpharmacyBinding
import com.tokopedia.checkout.databinding.ItemCheckoutOrderBackupBinding
import com.tokopedia.checkout.databinding.ItemCheckoutProductBenefitBinding
import com.tokopedia.checkout.databinding.ItemCheckoutProductBackupBinding
import com.tokopedia.checkout.databinding.ItemCheckoutPromoBinding
import com.tokopedia.checkout.databinding.ItemCheckoutTickerErrorBinding
import com.tokopedia.checkout.databinding.ItemCheckoutUpsellBinding
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
                    ItemCheckoutProductBackupBinding.inflate(
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
                    ItemCheckoutOrderBackupBinding.inflate(
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

            CheckoutCostViewHolder.VIEW_TYPE -> {
                CheckoutCostViewHolder(ItemCheckoutCostBackupBinding.inflate(inflater, parent, false), inflater, listener)
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
