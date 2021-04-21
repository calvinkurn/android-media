package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemShopBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ERROR
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class CartShopViewHolder(private val binding: ItemShopBinding,
                         private val actionListener: ActionListener,
                         private val cartItemAdapterListener: CartItemAdapter.ActionListener,
                         private val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(cartShopHolderData: CartShopHolderData) {
        renderWarningAndError(cartShopHolderData)
        renderErrorItemHeader(cartShopHolderData)
        renderWarningItemHeader(cartShopHolderData)
        renderShopName(cartShopHolderData)
        renderShopBadge(cartShopHolderData)
        renderCartItems(cartShopHolderData)
        renderCheckBox(cartShopHolderData)
        renderFulfillment(cartShopHolderData)
        renderPreOrder(cartShopHolderData)
        renderIncidentLabel(cartShopHolderData)
        renderFreeShipping(cartShopHolderData)
        renderEstimatedTimeArrival(cartShopHolderData)
    }

    private fun renderWarningAndError(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.isError || cartShopHolderData.shopGroupAvailableData.isWarning) {
            binding.llWarningAndError.root.show()
        } else {
            binding.llWarningAndError.root.gone()
        }
    }

    private fun renderShopName(cartShopHolderData: CartShopHolderData) {
        val shopName = cartShopHolderData.shopGroupAvailableData.shopName
        binding.tvShopName.text = shopName
        binding.tvShopName.setOnClickListener { v: View? ->
            actionListener.onCartShopNameClicked(
                    cartShopHolderData.shopGroupAvailableData.shopId,
                    cartShopHolderData.shopGroupAvailableData.shopName)
        }
    }

    private fun renderShopBadge(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.isOfficialStore || cartShopHolderData.shopGroupAvailableData.isGoldMerchant) {
            if (cartShopHolderData.shopGroupAvailableData.shopBadge?.isNotEmpty() == true) {
                ImageHandler.loadImageWithoutPlaceholder(binding.imgShopBadge, cartShopHolderData.shopGroupAvailableData.shopBadge)
                val shopType = if (cartShopHolderData.shopGroupAvailableData.isOfficialStore) {
                    itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_shop_type_official_store)
                } else {
                    itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_shop_type_power_merchant)
                }
                binding.imgShopBadge.contentDescription = itemView.context.getString(com.tokopedia.purchase_platform.common.R.string.pp_cd_image_shop_badge_with_shop_type, shopType)
                binding.imgShopBadge.show()
            }
        } else {
            binding.imgShopBadge.gone()
        }
    }

    private fun renderCartItems(cartShopHolderData: CartShopHolderData) {
        val cartItemAdapter = CartItemAdapter(cartItemAdapterListener, compositeSubscription, adapterPosition)
        cartItemAdapter.addDataList(cartShopHolderData.shopGroupAvailableData.cartItemDataList)
        val linearLayoutManager = LinearLayoutManager(binding.rvCartItem.context)
        binding.rvCartItem.layoutManager = linearLayoutManager
        binding.rvCartItem.adapter = cartItemAdapter
        (binding.rvCartItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun renderCheckBox(cartShopHolderData: CartShopHolderData) {
        binding.cbSelectShop.isEnabled = !cartShopHolderData.shopGroupAvailableData.isError
        binding.cbSelectShop.isChecked = cartShopHolderData.isAllSelected
        binding.cbSelectShop.skipAnimation()
        initCheckboxWatcherDebouncer(cartShopHolderData, compositeSubscription)
    }

    private fun initCheckboxWatcherDebouncer(cartShopHolderData: CartShopHolderData, compositeSubscription: CompositeSubscription) {
        binding.cbSelectShop.let {
            compositeSubscription.add(
                    rxViewClickDebounce(it, CHECKBOX_WATCHER_DEBOUNCE_TIME).subscribe(object : Subscriber<Boolean>() {
                        override fun onNext(isChecked: Boolean) {
                            cbSelectShopClickListener(cartShopHolderData)
                        }

                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                        }
                    })
            )
        }
    }

    private fun renderFulfillment(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.shopGroupAvailableData.fulfillmentName?.isNotBlank() == true) {
                if (cartShopHolderData.shopGroupAvailableData.isFulfillment && cartShopHolderData.shopGroupAvailableData.fulfillmentBadgeUrl.isNotEmpty()) {
                    iuImageFulfill.show()
                    iuImageFulfill.loadImageWithoutPlaceholder(cartShopHolderData.shopGroupAvailableData.fulfillmentBadgeUrl)
                } else {
                    iuImageFulfill.gone()
                }
                tvFulfillDistrict.show()
                tvFulfillDistrict.text = cartShopHolderData.shopGroupAvailableData.fulfillmentName
            } else {
                iuImageFulfill.gone()
                tvFulfillDistrict.gone()
            }
        }
    }

    private fun renderEstimatedTimeArrival(cartShopHolderData: CartShopHolderData) {
        val eta = cartShopHolderData.shopGroupAvailableData.estimatedTimeArrival
        with(binding) {
            if (eta.isNotBlank()) {
                textEstimatedTimeArrival.text = eta
                textEstimatedTimeArrival.show()
                separatorEstimatedTimeArrival.show()
            } else {
                textEstimatedTimeArrival.gone()
                separatorEstimatedTimeArrival.gone()
            }
        }
    }

    private fun renderErrorItemHeader(data: CartShopHolderData) {
        with(binding) {
            if (data.shopGroupAvailableData.isError) {
                cbSelectShop.isEnabled = false
                flShopItemContainer.foreground = ContextCompat.getDrawable(flShopItemContainer.context, R.drawable.fg_disabled_item)
                llShopContainer.setBackgroundResource(R.drawable.bg_error_shop)
                if (data.shopGroupAvailableData.errorTitle?.isNotBlank() == true) {
                    val errorDescription = data.shopGroupAvailableData.errorDescription
                    if (errorDescription?.isNotBlank() == true) {
                        llWarningAndError.tickerError.tickerTitle = data.shopGroupAvailableData.errorTitle
                        llWarningAndError.tickerError.setTextDescription(errorDescription)
                    } else {
                        llWarningAndError.tickerError.tickerTitle = null
                        llWarningAndError.tickerError.setTextDescription(data.shopGroupAvailableData.errorTitle
                                ?: "")
                    }
                    llWarningAndError.tickerError.tickerType = TYPE_ERROR
                    llWarningAndError.tickerError.tickerShape = SHAPE_LOOSE
                    llWarningAndError.tickerError.closeButtonVisibility = View.GONE
                    llWarningAndError.tickerError.show()
                    llWarningAndError.tickerError.post {
                        binding.llWarningAndError.tickerError.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        binding.llWarningAndError.tickerError.requestLayout()
                    }
                    llWarningAndError.layoutError.show()
                } else {
                    llWarningAndError.layoutError.gone()
                }
            } else {
                cbSelectShop.isEnabled = true
                flShopItemContainer.foreground = ContextCompat.getDrawable(flShopItemContainer.context, com.tokopedia.purchase_platform.common.R.drawable.fg_enabled_item)
                llShopContainer.setBackgroundColor(llShopContainer.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
                llWarningAndError.layoutError.gone()
            }
        }
    }

    private fun renderWarningItemHeader(data: CartShopHolderData) {
        with(binding.llWarningAndError) {
            if (data.shopGroupAvailableData.isWarning) {
                val warningDescription = data.shopGroupAvailableData.warningDescription
                if (warningDescription?.isNotBlank() == true) {
                    tickerWarning.tickerTitle = data.shopGroupAvailableData.warningTitle
                    tickerWarning.setTextDescription(warningDescription)
                } else {
                    tickerWarning.tickerTitle = null
                    tickerWarning.setTextDescription(data.shopGroupAvailableData.warningTitle
                            ?: "")
                }
                tickerWarning.tickerType = TYPE_WARNING
                tickerWarning.tickerShape = SHAPE_LOOSE
                tickerWarning.closeButtonVisibility = View.GONE
                tickerWarning.show()
                tickerWarning.post {
                    binding.llWarningAndError.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    binding.llWarningAndError.tickerWarning.requestLayout()
                }
                layoutWarning.show()
            } else {
                tickerWarning.gone()
                layoutWarning.gone()
            }
        }
    }

    private fun cbSelectShopClickListener(cartShopHolderData: CartShopHolderData) {
        if (!cartShopHolderData.shopGroupAvailableData.isError) {
            val isChecked: Boolean
            if (cartShopHolderData.isPartialSelected) {
                isChecked = true
                cartShopHolderData.isAllSelected = true
                cartShopHolderData.isPartialSelected = false
            } else {
                isChecked = !cartShopHolderData.isAllSelected
            }
            var isAllSelected = true
            cartShopHolderData.shopGroupAvailableData.cartItemDataList?.forEach {
                if (it.cartItemData?.isError == true && it.cartItemData?.isSingleChild == true) {
                    isAllSelected = false
                    return@forEach
                }
            }
            cartShopHolderData.isAllSelected = isAllSelected
            if (adapterPosition != RecyclerView.NO_POSITION) {
                actionListener.onShopItemCheckChanged(adapterPosition, isChecked)
            }
        }
    }

    private fun renderPreOrder(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.shopGroupAvailableData.preOrderInfo.isNotBlank()) {
                labelPreOrder.text = cartShopHolderData.shopGroupAvailableData.preOrderInfo
                labelPreOrder.show()
                separatorPreOrder.show()
            } else {
                labelPreOrder.gone()
                separatorPreOrder.gone()
            }
        }
    }

    private fun renderIncidentLabel(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.shopGroupAvailableData.incidentInfo.isNotBlank()) {
                labelIncident.text = cartShopHolderData.shopGroupAvailableData.incidentInfo
                labelIncident.show()
                separatorIncident.show()
            } else {
                labelIncident.gone()
                separatorIncident.gone()
            }
        }
    }

    private fun renderFreeShipping(cartShopHolderData: CartShopHolderData) {
        with(binding) {
            if (cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl.isNotBlank()) {
                ImageHandler.loadImageWithoutPlaceholderAndError(
                        imgFreeShipping, cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl
                )
                val contentDescriptionStringResource = if (cartShopHolderData.shopGroupAvailableData.isFreeShippingExtra) {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_boe
                } else {
                    com.tokopedia.purchase_platform.common.R.string.pp_cd_image_badge_bo
                }
                imgFreeShipping.contentDescription = itemView.context.getString(contentDescriptionStringResource)
                imgFreeShipping.show()
                separatorFreeShipping.show()
            } else {
                imgFreeShipping.gone()
                separatorFreeShipping.gone()
            }
        }
    }

    companion object {
        val TYPE_VIEW_ITEM_SHOP = R.layout.item_shop

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
    }

}