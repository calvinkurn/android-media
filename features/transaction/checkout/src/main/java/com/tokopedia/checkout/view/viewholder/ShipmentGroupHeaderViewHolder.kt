package com.tokopedia.checkout.view.viewholder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupHeaderBinding
import com.tokopedia.checkout.view.uimodel.ShipmentGroupHeaderModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.util.*

class ShipmentGroupHeaderViewHolder(
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

    fun bind(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        renderShop(shipmentGroupHeader)
        renderFulfillment(shipmentGroupHeader)
        renderErrorAndWarning(shipmentGroupHeader)
        renderCustomError(shipmentGroupHeader)
    }

    private fun renderShop(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        with(binding.containerSellerInfo) {
            if (shipmentGroupHeader.shipmentCartItem.orderNumber > 0) {
                textOrderNumber.text = itemView.context.getString(
                    R.string.label_order_counter,
                    shipmentGroupHeader.shipmentCartItem.orderNumber
                )
                textOrderNumber.visible()
            } else {
                textOrderNumber.gone()
            }
            if (!shipmentGroupHeader.shipmentCartItem.preOrderInfo.isNullOrBlank()) {
                labelPreOrder.text = shipmentGroupHeader.shipmentCartItem.preOrderInfo
                labelPreOrder.visible()
                separatorPreOrder.visible()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
            val freeShippingBadgeUrl = shipmentGroupHeader.shipmentCartItem.freeShippingBadgeUrl
            if (!freeShippingBadgeUrl.isNullOrBlank()) {
                imgFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (shipmentGroupHeader.shipmentCartItem.isFreeShippingPlus) {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
                } else {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
                }
                imgFreeShipping.visible()
                separatorFreeShipping.visible()
                if (!shipmentGroupHeader.shipmentCartItem.hasSeenFreeShippingBadge && shipmentGroupHeader.shipmentCartItem.isFreeShippingPlus) {
                    shipmentGroupHeader.shipmentCartItem.hasSeenFreeShippingBadge = true
                    listener?.onViewFreeShippingPlusBadge()
                }
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
            if (!shipmentGroupHeader.shipmentCartItem.shopAlertMessage.isNullOrBlank()) {
                labelIncidentShopLevel.text = shipmentGroupHeader.shipmentCartItem.shopAlertMessage
                labelIncidentShopLevel.visible()
                separatorIncidentShopLevel.visible()
            } else {
                labelIncidentShopLevel.gone()
                separatorIncidentShopLevel.gone()
            }
            var hasTradeInItem = false
            for (cartItemModel in shipmentGroupHeader.shipmentCartItem.cartItemModels) {
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
            if (shipmentGroupHeader.shipmentCartItem.shopTypeInfoData.shopBadge.isNotEmpty()) {
                imgShopBadge.setImageUrl(shipmentGroupHeader.shipmentCartItem.shopTypeInfoData.shopBadge)
                imgShopBadge.visible()
                imgShopBadge.contentDescription = itemView.context.getString(
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                    shipmentGroupHeader.shipmentCartItem.shopTypeInfoData.title.lowercase(
                        Locale.getDefault()
                    )
                )
            } else {
                imgShopBadge.gone()
            }
            val shopName = shipmentGroupHeader.shipmentCartItem.shopName
            tvShopName.text = shopName
            if (shipmentGroupHeader.shipmentCartItem.enablerLabel.isBlank()) {
                labelEpharmacy.gone()
            } else {
                labelEpharmacy.setLabel(shipmentGroupHeader.shipmentCartItem.enablerLabel)
                labelEpharmacy.visible()
            }
        }
    }

    private fun renderFulfillment(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        with(binding.containerSellerInfo) {
            if (!shipmentGroupHeader.shipmentCartItem.shopLocation.isNullOrBlank()) {
                val fulfillmentBadgeUrl = shipmentGroupHeader.shipmentCartItem.fulfillmentBadgeUrl
                if (shipmentGroupHeader.shipmentCartItem.isFulfillment && !fulfillmentBadgeUrl.isNullOrBlank()) {
                    iuImageFulfill.setImageUrl(fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.visible()
                tvFulfillDistrict.text = shipmentGroupHeader.shipmentCartItem.shopLocation
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderErrorAndWarning(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        if (shipmentGroupHeader.shipmentCartItem.isError) {
            binding.llHeaderContainer.alpha = VIEW_ALPHA_DISABLED
            binding.containerWarningAndError.root.visible()
        } else {
            binding.llHeaderContainer.alpha = VIEW_ALPHA_ENABLED
            binding.containerWarningAndError.root.gone()
        }
        renderError(shipmentGroupHeader)
        renderWarningCloseable(shipmentGroupHeader)
    }

    private fun renderError(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        with(binding.containerWarningAndError) {
            if (shipmentGroupHeader.shipmentCartItem.isError) {
                val errorTitle = shipmentGroupHeader.shipmentCartItem.errorTitle
                val errorDescription = shipmentGroupHeader.shipmentCartItem.errorDescription
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
                        if (shipmentGroupHeader.shipmentCartItem.isCustomEpharmacyError) {
                            tickerError.setHtmlDescription(
                                "$errorTitle " + itemView.context.getString(
                                    R.string.checkout_ticker_lihat_cta_suffix
                                )
                            )
                            tickerError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    listener?.onClickLihatOnTickerOrderError(
                                        shipmentGroupHeader.shipmentCartItem.shopId.toString(),
                                        errorTitle
                                    )
                                    if (!shipmentGroupHeader.shipmentCartItem.isStateAllItemViewExpanded) {
                                        shipmentGroupHeader.shipmentCartItem.isTriggerScrollToErrorProduct = true
                                        shipmentGroupHeader.shipmentCartItem.isStateAllItemViewExpanded = true
                                        listener?.onErrorShouldExpandProduct(shipmentGroupHeader)
                                        return
                                    }
                                    listener?.onErrorShouldScrollToProduct(shipmentGroupHeader)
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                            if (shipmentGroupHeader.shipmentCartItem.isTriggerScrollToErrorProduct) {
                                listener?.onErrorShouldScrollToProduct(shipmentGroupHeader)
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

    private fun renderWarningCloseable(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        with(binding) {
            if (!shipmentGroupHeader.shipmentCartItem.isError && !TextUtils.isEmpty(
                    shipmentGroupHeader.shipmentCartItem.shopTicker
                )
            ) {
                tickerWarningCloseable.tickerTitle =
                    shipmentGroupHeader.shipmentCartItem.shopTickerTitle
                tickerWarningCloseable.setHtmlDescription(shipmentGroupHeader.shipmentCartItem.shopTicker)
                tickerWarningCloseable.visible()
                tickerWarningCloseable.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        // no-op
                    }

                    override fun onDismiss() {
                        shipmentGroupHeader.shipmentCartItem.shopTicker = ""
                        tickerWarningCloseable.gone()
                    }
                })
            } else {
                tickerWarningCloseable.gone()
            }
        }
    }

    private fun renderCustomError(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        with(binding.containerWarningAndError) {
            if ((
                !shipmentGroupHeader.shipmentCartItem.isError && shipmentGroupHeader.shipmentCartItem.isHasUnblockingError &&
                    !TextUtils.isEmpty(shipmentGroupHeader.shipmentCartItem.unblockingErrorMessage)
                ) &&
                shipmentGroupHeader.shipmentCartItem.firstProductErrorIndex > -1
            ) {
                val errorMessage = shipmentGroupHeader.shipmentCartItem.unblockingErrorMessage
                binding.containerWarningAndError.root.visibility = View.VISIBLE
                tickerError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
                tickerError.setDescriptionClickEvent(object : TickerCallback {

                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        listener?.onClickLihatOnTickerOrderError(
                            shipmentGroupHeader.shipmentCartItem.shopId.toString(),
                            errorMessage
                        )
                        if (!shipmentGroupHeader.shipmentCartItem.isStateAllItemViewExpanded) {
                            tickerError.setOnClickListener {
                                shipmentGroupHeader.shipmentCartItem.isTriggerScrollToErrorProduct =
                                    true
                                listener?.onErrorShouldExpandProduct(shipmentGroupHeader)
                            }
                            return
                        }
                        listener?.onErrorShouldScrollToProduct(shipmentGroupHeader)
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
                if (shipmentGroupHeader.shipmentCartItem.isTriggerScrollToErrorProduct) {
                    listener?.onErrorShouldScrollToProduct(shipmentGroupHeader)
                }
            }
        }
    }

    interface Listener {

        fun onViewFreeShippingPlusBadge()

        fun onClickLihatOnTickerOrderError(shopId: String?, errorMessage: String?)

        fun onErrorShouldExpandProduct(shipmentGroupHeader: ShipmentGroupHeaderModel)

        fun onErrorShouldScrollToProduct(shipmentGroupHeader: ShipmentGroupHeaderModel)
    }
}
