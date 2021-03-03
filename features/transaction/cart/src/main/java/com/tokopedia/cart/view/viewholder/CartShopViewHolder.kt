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

//    private val llWarningAndError: LinearLayout = itemView.findViewById(R.id.ll_warning_and_error)
//    private val flShopItemContainer: FrameLayout = itemView.findViewById(R.id.fl_shop_item_container)
//    private val llShopContainer: LinearLayout = itemView.findViewById(R.id.ll_shop_container)
//    private val cbSelectShop: CheckboxUnify = itemView.findViewById(R.id.cb_select_shop)
//    private val tvShopName: Typography = itemView.findViewById(R.id.tv_shop_name)
//    private val imgShopBadge: ImageView = itemView.findViewById(R.id.img_shop_badge)
//    private val tvFulfillDistrict: Typography = itemView.findViewById(R.id.tv_fulfill_district)
//    private val rvCartItem: RecyclerView = itemView.findViewById(R.id.rv_cart_item)
//    private val layoutError: LinearLayout = itemView.findViewById(R.id.layout_error)
//    private val tickerError: Ticker = itemView.findViewById(R.id.ticker_error)
//    private val layoutWarning: LinearLayout = itemView.findViewById(R.id.layout_warning)
//    private val tickerWarning: Ticker = itemView.findViewById(R.id.ticker_warning)
//    private val separatorPreOrder: Typography = itemView.findViewById(R.id.separator_pre_order)
//    private val labelPreOrder: Label = itemView.findViewById(R.id.label_pre_order)
//    private val separatorIncident: Typography = itemView.findViewById(R.id.separator_incident)
//    private val labelIncident: Label = itemView.findViewById(R.id.label_incident)
//    private val separatorFreeShipping: Typography = itemView.findViewById(R.id.separator_free_shipping)
//    private val imgFreeShipping: ImageView = itemView.findViewById(R.id.img_free_shipping)
//    private val labelFulfillment: Label = itemView.findViewById(R.id.label_fulfillment)
//    private val separatorEstimatedTimeArrival: Typography = itemView.findViewById(R.id.separator_estimated_time_arrival)
//    private val textEstimatedTimeArrival: Typography = itemView.findViewById(R.id.text_estimated_time_arrival)

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
        binding.labelFulfillment.visibility = if (cartShopHolderData.shopGroupAvailableData.isFulfillment) View.VISIBLE else View.GONE
        if (cartShopHolderData.shopGroupAvailableData.fulfillmentName?.isNotBlank() == true) {
            binding.tvFulfillDistrict.show()
            binding.tvFulfillDistrict.text = cartShopHolderData.shopGroupAvailableData.fulfillmentName
        } else {
            binding.tvFulfillDistrict.gone()
        }
    }

    private fun renderEstimatedTimeArrival(cartShopHolderData: CartShopHolderData) {
        val eta = cartShopHolderData.shopGroupAvailableData.estimatedTimeArrival
        if (eta.isNotBlank()) {
            binding.textEstimatedTimeArrival.text = eta
            binding.textEstimatedTimeArrival.show()
            binding.separatorEstimatedTimeArrival.show()
        } else {
            binding.textEstimatedTimeArrival.gone()
            binding.separatorEstimatedTimeArrival.gone()
        }
    }

    private fun renderErrorItemHeader(data: CartShopHolderData) {
        if (data.shopGroupAvailableData.isError) {
            binding.cbSelectShop.isEnabled = false
            binding.flShopItemContainer.foreground = ContextCompat.getDrawable(binding.flShopItemContainer.context, R.drawable.fg_disabled_item)
            binding.llShopContainer.setBackgroundResource(R.drawable.bg_error_shop)
            if (data.shopGroupAvailableData.errorTitle?.isNotBlank() == true) {
                val errorDescription = data.shopGroupAvailableData.errorDescription
                if (errorDescription?.isNotBlank() == true) {
                    binding.llWarningAndError.tickerError.tickerTitle = data.shopGroupAvailableData.errorTitle
                    binding.llWarningAndError.tickerError.setTextDescription(errorDescription)
                } else {
                    binding.llWarningAndError.tickerError.tickerTitle = null
                    binding.llWarningAndError.tickerError.setTextDescription(data.shopGroupAvailableData.errorTitle ?: "")
                }
                binding.llWarningAndError.tickerError.tickerType = TYPE_ERROR
                binding.llWarningAndError.tickerError.tickerShape = SHAPE_LOOSE
                binding.llWarningAndError.tickerError.closeButtonVisibility = View.GONE
                binding.llWarningAndError.tickerError.show()
                binding.llWarningAndError.tickerError.post {
                    binding.llWarningAndError.tickerError.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    binding.llWarningAndError.tickerError.requestLayout()
                }
                binding.llWarningAndError.layoutError.show()
            } else {
                binding.llWarningAndError.layoutError.gone()
            }
        } else {
            binding.cbSelectShop.isEnabled = true
            binding.flShopItemContainer.foreground = ContextCompat.getDrawable(binding.flShopItemContainer.context, R.drawable.fg_enabled_item)
            binding.llShopContainer.setBackgroundColor(binding.llShopContainer.context.resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))
            binding.llWarningAndError.layoutError.gone()
        }
    }

    private fun renderWarningItemHeader(data: CartShopHolderData) {
        if (data.shopGroupAvailableData.isWarning) {
            val warningDescription = data.shopGroupAvailableData.warningDescription
            if (warningDescription?.isNotBlank() == true) {
                binding.llWarningAndError.tickerWarning.tickerTitle = data.shopGroupAvailableData.warningTitle
                binding.llWarningAndError.tickerWarning.setTextDescription(warningDescription)
            } else {
                binding.llWarningAndError.tickerWarning.tickerTitle = null
                binding.llWarningAndError.tickerWarning.setTextDescription(data.shopGroupAvailableData.warningTitle ?: "")
            }
            binding.llWarningAndError.tickerWarning.tickerType = TYPE_WARNING
            binding.llWarningAndError.tickerWarning.tickerShape = SHAPE_LOOSE
            binding.llWarningAndError.tickerWarning.closeButtonVisibility = View.GONE
            binding.llWarningAndError.tickerWarning.show()
            binding.llWarningAndError.tickerWarning.post {
                binding.llWarningAndError.tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                binding.llWarningAndError.tickerWarning.requestLayout()
            }
            binding.llWarningAndError.layoutWarning.show()
        } else {
            binding.llWarningAndError.tickerWarning.gone()
            binding.llWarningAndError.layoutWarning.gone()
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
        if (cartShopHolderData.shopGroupAvailableData.preOrderInfo.isNotBlank()) {
            binding.labelPreOrder.text = cartShopHolderData.shopGroupAvailableData.preOrderInfo
            binding.labelPreOrder.show()
            binding.separatorPreOrder.show()
        } else {
            binding.labelPreOrder.gone()
            binding.separatorPreOrder.gone()
        }
    }

    private fun renderIncidentLabel(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.incidentInfo.isNotBlank()) {
            binding.labelIncident.text = cartShopHolderData.shopGroupAvailableData.incidentInfo
            binding.labelIncident.show()
            binding.separatorIncident.show()
        } else {
            binding.labelIncident.gone()
            binding.separatorIncident.gone()
        }
    }

    private fun renderFreeShipping(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    binding.imgFreeShipping, cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl
            )
            binding.imgFreeShipping.show()
            binding.separatorFreeShipping.show()
        } else {
            binding.imgFreeShipping.gone()
            binding.separatorFreeShipping.gone()
        }
    }

    companion object {
        val TYPE_VIEW_ITEM_SHOP = R.layout.item_shop

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
    }

}