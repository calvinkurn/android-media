package com.tokopedia.checkout.view.viewholder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.databinding.ItemShipmentGroupHeaderBinding
import com.tokopedia.checkout.view.ShipmentAdapterActionListener
import com.tokopedia.checkout.view.uimodel.ShipmentGroupHeaderModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import java.util.*

class ShipmentGroupHeaderViewHolder(
    itemView: View,
    private val actionListener: ShipmentAdapterActionListener? = null
) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmField
        val LAYOUT = R.layout.item_shipment_group_header
    }

    private val binding: ItemShipmentGroupHeaderBinding =
        ItemShipmentGroupHeaderBinding.bind(itemView)

    fun bind(shipmentGroupHeader: ShipmentGroupHeaderModel) {
        val shipmentCartItem = shipmentGroupHeader.shipmentCartItem
        renderShop(shipmentCartItem)
        renderFulfillment(shipmentCartItem)
        renderErrorAndWarning(shipmentCartItem)
        renderCustomError(shipmentCartItem)
    }

    private fun renderShop(shipmentCartItem: ShipmentCartItemModel) {
        with(binding.containerSellerInfo) {
            if (shipmentCartItem.orderNumber > 0) {
                textOrderNumber.text = itemView.context.getString(
                    R.string.label_order_counter,
                    shipmentCartItem.orderNumber
                )
                textOrderNumber.visible()
            } else {
                textOrderNumber.gone()
            }
            if (!shipmentCartItem.preOrderInfo.isNullOrBlank()) {
                labelPreOrder.text = shipmentCartItem.preOrderInfo
                labelPreOrder.visible()
                separatorPreOrder.visible()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
            val freeShippingBadgeUrl = shipmentCartItem.freeShippingBadgeUrl
            if (!freeShippingBadgeUrl.isNullOrBlank()) {
                imgFreeShipping.setImageUrl(freeShippingBadgeUrl)
                if (shipmentCartItem.isFreeShippingPlus) {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_plus)
                } else {
                    imgFreeShipping.contentDescription =
                        itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo)
                }
                imgFreeShipping.visible()
                separatorFreeShipping.visible()
                if (!shipmentCartItem.hasSeenFreeShippingBadge && shipmentCartItem.isFreeShippingPlus) {
                    shipmentCartItem.hasSeenFreeShippingBadge = true
                    actionListener?.onViewFreeShippingPlusBadge()
                }
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
            if (!shipmentCartItem.shopAlertMessage.isNullOrBlank()) {
                labelIncidentShopLevel.text = shipmentCartItem.shopAlertMessage
                labelIncidentShopLevel.visible()
                separatorIncidentShopLevel.visible()
            } else {
                labelIncidentShopLevel.gone()
                separatorIncidentShopLevel.gone()
            }
            var hasTradeInItem = false
            for (cartItemModel in shipmentCartItem.cartItemModels) {
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
            if (shipmentCartItem.shopTypeInfoData.shopBadge.isNotEmpty()) {
                imgShopBadge.setImageUrl(shipmentCartItem.shopTypeInfoData.shopBadge)
                imgShopBadge.visible()
                imgShopBadge.contentDescription = itemView.context.getString(
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type,
                    shipmentCartItem.shopTypeInfoData.title.lowercase(
                        Locale.getDefault()
                    )
                )
            } else {
                imgShopBadge.gone()
            }
            val shopName = shipmentCartItem.shopName
            tvShopName.text = shopName
            if (shipmentCartItem.enablerLabel.isBlank()) {
                labelEpharmacy.gone()
            } else {
                labelEpharmacy.setLabel(shipmentCartItem.enablerLabel)
                labelEpharmacy.visible()
            }
        }
    }

    private fun renderFulfillment(model: ShipmentCartItemModel) {
        with(binding.containerSellerInfo) {
            if (!model.shopLocation.isNullOrBlank()) {
                val fulfillmentBadgeUrl = model.fulfillmentBadgeUrl
                if (model.isFulfillment && !fulfillmentBadgeUrl.isNullOrBlank()) {
                    iuImageFulfill.setImageUrl(fulfillmentBadgeUrl)
                    iuImageFulfill.visible()
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.visible()
                tvFulfillDistrict.text = model.shopLocation
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderErrorAndWarning(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isError) {
            binding.containerWarningAndError.root.visible()
        } else {
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
                                    actionListener?.onClickLihatOnTickerOrderError(
                                        shipmentCartItemModel.shopId.toString(),
                                        errorTitle
                                    )
                                    if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                                        shipmentCartItemModel.isTriggerScrollToErrorProduct = true
//                                        showAllProductListener(shipmentCartItemModel).onClick(
//                                            tickerError
//                                        )
                                        return
                                    }
//                                    scrollToErrorProduct(shipmentCartItemModel)
                                }

                                override fun onDismiss() {
                                    // no op
                                }
                            })
                            if (shipmentCartItemModel.isTriggerScrollToErrorProduct) {
//                                scrollToErrorProduct(shipmentCartItemModel)
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
            if (!shipmentCartItemModel.isError && !TextUtils.isEmpty(shipmentCartItemModel.shopTicker)) {
                tickerWarningCloseable.tickerTitle = shipmentCartItemModel.shopTickerTitle
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
                    ) && shipmentCartItemModel.firstProductErrorIndex > -1
            ) {
                val errorMessage = shipmentCartItemModel.unblockingErrorMessage
                binding.containerWarningAndError.root.visibility = View.VISIBLE
                tickerError.setHtmlDescription(errorMessage + " " + itemView.context.getString(R.string.checkout_ticker_lihat_cta_suffix))
                tickerError.setDescriptionClickEvent(object : TickerCallback {
                    override fun onDescriptionViewClick(charSequence: CharSequence) {
                        actionListener?.onClickLihatOnTickerOrderError(
                            shipmentCartItemModel.shopId.toString(),
                            errorMessage
                        )
                        if (!shipmentCartItemModel.isStateAllItemViewExpanded) {
                            shipmentCartItemModel.isTriggerScrollToErrorProduct = true
//                            showAllProductListener(shipmentCartItemModel).onClick(tickerError)
                            return
                        }
//                        scrollToErrorProduct(shipmentCartItemModel)
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
//                    scrollToErrorProduct(shipmentCartItemModel)
                }
            }
        }
    }
}
