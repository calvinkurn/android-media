package com.tokopedia.checkout.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupHeaderBinding
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.util.*

class ShipmentCartItemTopViewHolder(
    itemView: View,
    private val listener: Listener
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
        renderGroupInformation(shipmentCartItemTopModel)
        renderShop(shipmentCartItemTopModel)
        renderErrorAndWarning(shipmentCartItemTopModel)
        renderCustomError(shipmentCartItemTopModel)
    }

    private fun renderGroupInformation(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        with(binding.containerSellerInfo) {
            tvShopName.text = shipmentCartItemTopModel.groupInfoName
            if (shipmentCartItemTopModel.groupInfoBadgeUrl.isNotEmpty()) {
                imgShopBadge.setImageUrl(shipmentCartItemTopModel.groupInfoBadgeUrl)
                imgShopBadge.visible()
                if (shipmentCartItemTopModel.uiGroupType == GroupShop.UI_GROUP_TYPE_NORMAL) {
                    imgShopBadge.contentDescription = itemView.context.getString(
                        com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                        shipmentCartItemTopModel.groupInfoName.lowercase(
                            Locale.getDefault()
                        )
                    )
                }
            } else {
                imgShopBadge.gone()
            }
            if (shipmentCartItemTopModel.groupInfoDescription.isNotBlank()) {
                val fulfillmentBadgeUrl = shipmentCartItemTopModel.groupInfoDescriptionBadgeUrl
                if (fulfillmentBadgeUrl.isNotBlank()) {
                    iuImageFulfill.setImageUrl(fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.text = shipmentCartItemTopModel.groupInfoDescription
                tvFulfillDistrict.visible()
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderShop(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        with(binding.containerSellerInfo) {
            if (shipmentCartItemTopModel.orderNumber > 0) {
                textOrderNumber.text = itemView.context.getString(
                    R.string.label_order_counter,
                    shipmentCartItemTopModel.orderNumber
                )
                textOrderNumber.visible()
            } else {
                textOrderNumber.gone()
            }
            if (shipmentCartItemTopModel.preOrderInfo.isNotBlank()) {
                labelPreOrder.text = shipmentCartItemTopModel.preOrderInfo
                labelPreOrder.visible()
                separatorPreOrder.visible()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
            val freeShippingBadgeUrl = shipmentCartItemTopModel.freeShippingBadgeUrl
            if (freeShippingBadgeUrl.isNotBlank()) {
                imgFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (shipmentCartItemTopModel.isFreeShippingPlus) {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
                } else {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
                }
                imgFreeShipping.visible()
                separatorFreeShipping.visible()
                if (!shipmentCartItemTopModel.hasSeenFreeShippingBadge && shipmentCartItemTopModel.isFreeShippingPlus) {
                    shipmentCartItemTopModel.hasSeenFreeShippingBadge = true
                    listener.onViewFreeShippingPlusBadge()
                }
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
            if (shipmentCartItemTopModel.shopAlertMessage.isNotBlank()) {
                labelIncidentShopLevel.text = shipmentCartItemTopModel.shopAlertMessage
                labelIncidentShopLevel.visible()
                separatorIncidentShopLevel.visible()
            } else {
                labelIncidentShopLevel.gone()
                separatorIncidentShopLevel.gone()
            }
            if (shipmentCartItemTopModel.hasTradeInItem) {
                tvTradeInLabel.visible()
            } else {
                tvTradeInLabel.gone()
            }
            if (shipmentCartItemTopModel.enablerLabel.isBlank()) {
                labelEpharmacy.gone()
            } else {
                labelEpharmacy.setLabel(shipmentCartItemTopModel.enablerLabel)
                labelEpharmacy.visible()
            }
        }
    }

    private fun renderErrorAndWarning(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        if (shipmentCartItemTopModel.isError) {
            binding.containerSellerInfo.root.alpha = VIEW_ALPHA_DISABLED
            binding.containerWarningAndError.root.visible()
        } else {
            binding.containerSellerInfo.root.alpha = VIEW_ALPHA_ENABLED
            binding.containerWarningAndError.root.gone()
        }
        renderError(shipmentCartItemTopModel)
        renderWarningCloseable(shipmentCartItemTopModel)
    }

    private fun renderError(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        with(binding.containerWarningAndError) {
            if (shipmentCartItemTopModel.isError) {
                val errorTitle = shipmentCartItemTopModel.errorTitle
                val errorDescription = shipmentCartItemTopModel.errorDescription
                if (errorTitle.isNotEmpty()) {
                    if (errorDescription.isNotEmpty()) {
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
                        if (shipmentCartItemTopModel.isCustomEpharmacyError) {
                            tickerError.setHtmlDescription(
                                "$errorTitle ${itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix)}"
                            )
                            tickerError.setDescriptionClickEvent(object : TickerCallback {
                                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                                    listener.onClickLihatOnTickerOrderError(
                                        shipmentCartItemTopModel.shopId.toString(),
                                        errorTitle,
                                        shipmentCartItemTopModel
                                    )
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
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
                    layoutError.visible()
                } else {
                    tickerError.gone()
                    layoutError.gone()
                }
            } else {
                tickerError.gone()
                layoutError.gone()
            }
            layoutWarning.gone()
        }
    }

    private fun renderWarningCloseable(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        with(binding) {
            if (!shipmentCartItemTopModel.isError && shipmentCartItemTopModel.shopTicker.isNotEmpty()) {
                tickerWarningCloseable.tickerTitle =
                    shipmentCartItemTopModel.shopTickerTitle
                tickerWarningCloseable.setHtmlDescription(shipmentCartItemTopModel.shopTicker)
                tickerWarningCloseable.visible()
                tickerWarningCloseable.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        // no-op
                    }

                    override fun onDismiss() {
                        shipmentCartItemTopModel.shopTicker = ""
                        tickerWarningCloseable.gone()
                    }
                })
            } else {
                tickerWarningCloseable.gone()
            }
        }
    }

    private fun renderCustomError(shipmentCartItemTopModel: ShipmentCartItemTopModel) {
        with(binding.containerWarningAndError) {
            if ((
                !shipmentCartItemTopModel.isError && shipmentCartItemTopModel.isHasUnblockingError &&
                    shipmentCartItemTopModel.unblockingErrorMessage.isNotEmpty()
                ) &&
                shipmentCartItemTopModel.firstProductErrorIndex > -1
            ) {
                val errorMessage = shipmentCartItemTopModel.unblockingErrorMessage
                binding.containerWarningAndError.root.visibility = View.VISIBLE
                tickerError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
                tickerError.setDescriptionClickEvent(object : TickerCallback {

                    override fun onDescriptionViewClick(linkUrl: CharSequence) {
                        listener.onClickLihatOnTickerOrderError(
                            shipmentCartItemTopModel.shopId.toString(),
                            errorMessage,
                            shipmentCartItemTopModel
                        )
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
            }
        }
    }

    interface Listener {

        fun onViewFreeShippingPlusBadge()

        fun onClickLihatOnTickerOrderError(shopId: String, errorMessage: String, shipmentCartItemTopModel: ShipmentCartItemTopModel)
    }
}
