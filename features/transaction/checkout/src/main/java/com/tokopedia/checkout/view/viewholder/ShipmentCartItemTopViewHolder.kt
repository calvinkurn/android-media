package com.tokopedia.checkout.view.viewholder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupHeaderBinding
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.util.*

class ShipmentCartItemTopViewHolder(
    itemView: View,
    private val listener: Listener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_header

        private const val VIEW_ALPHA_ENABLED = 1.0f
        private const val VIEW_ALPHA_DISABLED = 0.5f
    }

    private val binding: ItemShipmentGroupHeaderBinding =
        ItemShipmentGroupHeaderBinding.bind(itemView)

    fun bind(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        renderShop(shipmentCartItemTopModel.shipmentCartItemModel)
        renderFulfillment(shipmentCartItemTopModel.shipmentCartItemModel)
        renderErrorAndWarning(shipmentCartItemTopModel.shipmentCartItemModel)
        renderCustomError(shipmentCartItemTopModel.shipmentCartItemModel)
    }

    private fun renderShop(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerSellerInfo) {
            if (shipmentCartItemModel.orderNumber > 0) {
                textOrderNumber.text = itemView.context.getString(
                    R.string.label_order_counter,
                    shipmentCartItemModel.orderNumber
                )
                textOrderNumber.visible()
            } else {
                textOrderNumber.gone()
            }
            if (!shipmentCartItemModel.preOrderInfo.isNullOrBlank()) {
                labelPreOrder.text = shipmentCartItemModel.preOrderInfo
                labelPreOrder.visible()
                separatorPreOrder.visible()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
            val freeShippingBadgeUrl = shipmentCartItemModel.freeShippingBadgeUrl
            if (!freeShippingBadgeUrl.isNullOrBlank()) {
                imgFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (shipmentCartItemModel.isFreeShippingPlus) {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
                } else {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
                }
                imgFreeShipping.visible()
                separatorFreeShipping.visible()
                if (!shipmentCartItemModel.hasSeenFreeShippingBadge && shipmentCartItemModel.isFreeShippingPlus) {
                    shipmentCartItemModel.hasSeenFreeShippingBadge = true
                    listener?.onViewFreeShippingPlusBadge()
                }
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
            if (!shipmentCartItemModel.shopAlertMessage.isNullOrBlank()) {
                labelIncidentShopLevel.text = shipmentCartItemModel.shopAlertMessage
                labelIncidentShopLevel.visible()
                separatorIncidentShopLevel.visible()
            } else {
                labelIncidentShopLevel.gone()
                separatorIncidentShopLevel.gone()
            }
            var hasTradeInItem = false
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                if (cartItemModel.isValidTradeIn) {
                    hasTradeInItem = true
                    break
                }
            }
            if (hasTradeInItem) {
                tvTradeInLabel.visible()
            } else {
                tvTradeInLabel.gone()
            }
            if (shipmentCartItemModel.shopTypeInfoData.shopBadge.isNotEmpty()) {
                imgShopBadge.setImageUrl(shipmentCartItemModel.shopTypeInfoData.shopBadge)
                imgShopBadge.visible()
                imgShopBadge.contentDescription = itemView.context.getString(
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                    shipmentCartItemModel.shopTypeInfoData.title.lowercase(
                        Locale.getDefault()
                    )
                )
            } else {
                imgShopBadge.gone()
            }
            val shopName = shipmentCartItemModel.shopName
            tvShopName.text = shopName
            if (shipmentCartItemModel.enablerLabel.isBlank()) {
                labelEpharmacy.gone()
            } else {
                labelEpharmacy.setLabel(shipmentCartItemModel.enablerLabel)
                labelEpharmacy.visible()
            }
        }
    }

    private fun renderFulfillment(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerSellerInfo) {
            if (!shipmentCartItemModel.shopLocation.isNullOrBlank()) {
                val fulfillmentBadgeUrl = shipmentCartItemModel.fulfillmentBadgeUrl
                if (shipmentCartItemModel.isFulfillment && !fulfillmentBadgeUrl.isNullOrBlank()) {
                    iuImageFulfill.setImageUrl(fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.visible()
                tvFulfillDistrict.text = shipmentCartItemModel.shopLocation
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderErrorAndWarning(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isError) {
            binding.llHeaderContainer.alpha = VIEW_ALPHA_DISABLED
            binding.containerWarningAndError.root.visible()
        } else {
            binding.llHeaderContainer.alpha = VIEW_ALPHA_ENABLED
            binding.containerWarningAndError.root.gone()
        }
        renderError(shipmentCartItemModel)
        renderWarningCloseable(shipmentCartItemModel)
    }

    private fun renderError(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerWarningAndError) {
            if (shipmentCartItemModel.isError) {
                val errorTitle = shipmentCartItemModel.errorTitle
                val errorDescription = shipmentCartItemModel.errorDescription
                if (!errorTitle.isNullOrEmpty()) {
                    if (!errorDescription.isNullOrEmpty()) {
                        tickerError.tickerTitle = errorTitle
                        tickerError.setTextDescription(errorDescription)
                        tickerError.setDescriptionClickEvent(object : TickerCallback {
                            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                // no-op
                            }

                            override fun onDismiss() {
                                // no-op
                            }
                        })
                    } else {
                        if (shipmentCartItemModel.isCustomEpharmacyError) {
                            tickerError.setHtmlDescription(
                                "$errorTitle " + itemView.context.getString(
                                    R.string.checkout_ticker_lihat_cta_suffix
                                )
                            )
                            tickerError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    listener?.onClickLihatOnTickerOrderError(
                                        shipmentCartItemModel.shopId.toString(),
                                        errorTitle
                                    )
                                    if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                                        shipmentCartItemModel.isTriggerScrollToErrorProduct = true
                                        shipmentCartItemModel.isStateAllItemViewExpanded = true
                                        listener?.onErrorShouldExpandProduct(shipmentCartItemModel)
                                        return
                                    }
                                    listener?.onErrorShouldScrollToProduct(shipmentCartItemModel)
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                            if (shipmentCartItemModel.isTriggerScrollToErrorProduct) {
                                listener?.onErrorShouldScrollToProduct(shipmentCartItemModel)
                            }
                        } else {
                            tickerError.setTextDescription(errorTitle)
                            tickerError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    // no op
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                        }
                    }
                    tickerError.tickerType = Ticker.TYPE_ERROR
                    tickerError.tickerShape = Ticker.SHAPE_LOOSE
                    tickerError.closeButtonVisibility = View.GONE
                    tickerError.visible()
                } else {
                    tickerError.gone()
                }
            } else {
                layoutError.gone()
                tickerError.gone()
            }
        }
    }

    private fun renderWarningCloseable(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding) {
            if (!shipmentCartItemModel.isError && !TextUtils.isEmpty(
                    shipmentCartItemModel.shopTicker
                )
            ) {
                tickerWarningCloseable.tickerTitle =
                    shipmentCartItemModel.shopTickerTitle
                tickerWarningCloseable.setHtmlDescription(shipmentCartItemModel.shopTicker)
                tickerWarningCloseable.visible()
                tickerWarningCloseable.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        // no-op
                    }

                    override fun onDismiss() {
                        shipmentCartItemModel.shopTicker = ""
                        tickerWarningCloseable.gone()
                    }
                })
            } else {
                tickerWarningCloseable.gone()
            }
        }
    }

    private fun renderCustomError(shipmentCartItemModel: ShipmentCartItemModel) {
        with(binding.containerWarningAndError) {
            if ((
                !shipmentCartItemModel.isError && shipmentCartItemModel.isHasUnblockingError &&
                    !TextUtils.isEmpty(shipmentCartItemModel.unblockingErrorMessage)
                ) &&
                shipmentCartItemModel.firstProductErrorIndex > -1
            ) {
                val errorMessage = shipmentCartItemModel.unblockingErrorMessage
                binding.containerWarningAndError.root.visibility = View.VISIBLE
                tickerError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
                tickerError.setDescriptionClickEvent(object : TickerCallback {

                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        listener?.onClickLihatOnTickerOrderError(
                            shipmentCartItemModel.shopId.toString(),
                            errorMessage
                        )
                        if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                            tickerError.setOnClickListener {
                                shipmentCartItemModel.isTriggerScrollToErrorProduct =
                                    true
                                listener?.onErrorShouldExpandProduct(shipmentCartItemModel)
                            }
                            return
                        }
                        listener?.onErrorShouldScrollToProduct(shipmentCartItemModel)
                    }

                    override fun onDismiss() {
                        // no-op
                    }
                })
                tickerError.tickerType = Ticker.TYPE_ERROR
                tickerError.tickerShape = Ticker.SHAPE_LOOSE
                tickerError.closeButtonVisibility = View.GONE
                tickerError.visibility = View.VISIBLE
                layoutError.visibility = View.VISIBLE
                if (shipmentCartItemModel.isTriggerScrollToErrorProduct) {
                    listener?.onErrorShouldScrollToProduct(shipmentCartItemModel)
                }
            }
        }
    }

    interface Listener {

        fun onViewFreeShippingPlusBadge()

        fun onClickLihatOnTickerOrderError(shopId: String?, errorMessage: String?)

        fun onErrorShouldExpandProduct(shipmentCartItemModel: ShipmentCartItemModel)

        fun onErrorShouldScrollToProduct(shipmentCartItemModel: ShipmentCartItemModel)
    }
}
