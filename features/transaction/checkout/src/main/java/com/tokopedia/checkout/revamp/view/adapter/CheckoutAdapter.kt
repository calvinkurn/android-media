package com.tokopedia.checkout.revamp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.databinding.ItemCheckoutAddressBinding
import com.tokopedia.checkout.databinding.ItemShipmentGroupFooterBinding
import com.tokopedia.checkout.databinding.ItemShipmentProductBinding
import com.tokopedia.checkout.databinding.ItemUpsellNewImprovementBinding
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutAddressViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutOrderViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutProductViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutTickerViewHolder
import com.tokopedia.checkout.revamp.view.viewholder.CheckoutUpsellViewHolder
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionViewHolder
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementViewHolder

class CheckoutAdapter(
    private val listener: CheckoutAdapterListener,
    private val epharmacyListener: UploadPrescriptionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var list: List<CheckoutItem> = emptyList()

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is CheckoutTickerModel -> TickerAnnouncementViewHolder.LAYOUT
            is CheckoutAddressModel -> CheckoutAddressViewHolder.VIEW_TYPE
            is CheckoutUpsellModel -> CheckoutUpsellViewHolder.VIEW_TYPE
            is CheckoutProductModel -> CheckoutProductViewHolder.VIEW_TYPE
            is CheckoutOrderModel -> CheckoutOrderViewHolder.VIEW_TYPE
            is CheckoutEpharmacyModel -> UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
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
                    )
                )
            }

            CheckoutProductViewHolder.VIEW_TYPE -> {
                CheckoutProductViewHolder(
                    ItemShipmentProductBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }

            CheckoutOrderViewHolder.VIEW_TYPE -> {
                CheckoutOrderViewHolder(
                    ItemShipmentGroupFooterBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }

            UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD -> {
                UploadPrescriptionViewHolder(
                    inflater.inflate(
                        UploadPrescriptionViewHolder.ITEM_VIEW_UPLOAD,
                        parent,
                        false
                    ),
                    epharmacyListener
                )
            }

            else -> {
                CheckoutUpsellViewHolder(
                    ItemUpsellNewImprovementBinding.inflate(
                        inflater,
                        parent,
                        false
                    ),
                    listener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = list[position]) {
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

            is CheckoutOrderModel -> {
                (holder as CheckoutOrderViewHolder).bind(item)
            }

            is CheckoutEpharmacyModel -> {
                (holder as UploadPrescriptionViewHolder).bindViewHolder(item.epharmacy)
            }

            else -> {
            }
        }
    }
}
