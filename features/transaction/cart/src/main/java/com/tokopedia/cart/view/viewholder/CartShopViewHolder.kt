package com.tokopedia.cart.view.viewholder

import android.view.View
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.adapter.cart.CartItemAdapter
import com.tokopedia.cart.view.uimodel.CartShopHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.purchase_platform.common.utils.rxViewClickDebounce
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.SHAPE_LOOSE
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_ERROR
import com.tokopedia.unifycomponents.ticker.Ticker.Companion.TYPE_WARNING
import com.tokopedia.unifyprinciples.Typography
import rx.Subscriber
import rx.subscriptions.CompositeSubscription

class CartShopViewHolder(itemView: View,
                         private val actionListener: ActionListener,
                         private val cartItemAdapterListener: CartItemAdapter.ActionListener,
                         private val compositeSubscription: CompositeSubscription) : RecyclerView.ViewHolder(itemView) {

    private val llWarningAndError: LinearLayout = itemView.findViewById(R.id.ll_warning_and_error)
    private val flShopItemContainer: FrameLayout = itemView.findViewById(R.id.fl_shop_item_container)
    private val llShopContainer: LinearLayout = itemView.findViewById(R.id.ll_shop_container)
    private val cbSelectShop: CheckBox = itemView.findViewById(R.id.cb_select_shop)
    private val tvShopName: Typography = itemView.findViewById(R.id.tv_shop_name)
    private val imgShopBadge: ImageView = itemView.findViewById(R.id.img_shop_badge)
    private val tvFulfillDistrict: Typography = itemView.findViewById(R.id.tv_fulfill_district)
    private val rvCartItem: RecyclerView = itemView.findViewById(R.id.rv_cart_item)
    private val layoutError: LinearLayout = itemView.findViewById(R.id.layout_error)
    private val tickerError: Ticker = itemView.findViewById(R.id.ticker_error)
    private val layoutWarning: LinearLayout = itemView.findViewById(R.id.layout_warning)
    private val tickerWarning: Ticker = itemView.findViewById(R.id.ticker_warning)
    private val separatorPreOrder: Typography = itemView.findViewById(R.id.separator_pre_order)
    private val labelPreOrder: Label = itemView.findViewById(R.id.label_pre_order)
    private val separatorIncident: Typography = itemView.findViewById(R.id.separator_incident)
    private val labelIncident: Label = itemView.findViewById(R.id.label_incident)
    private val separatorFreeShipping: Typography = itemView.findViewById(R.id.separator_free_shipping)
    private val imgFreeShipping: ImageView = itemView.findViewById(R.id.img_free_shipping)
    private val labelFulfillment: Label = itemView.findViewById(R.id.label_fulfillment)
    private val separatorEstimatedTimeArrival: Typography = itemView.findViewById(R.id.separator_estimated_time_arrival)
    private val textEstimatedTimeArrival: Typography = itemView.findViewById(R.id.text_estimated_time_arrival)

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
            llWarningAndError.show()
        } else {
            llWarningAndError.gone()
        }
    }

    private fun renderShopName(cartShopHolderData: CartShopHolderData) {
        val shopName = cartShopHolderData.shopGroupAvailableData.shopName
        tvShopName.text = shopName
        tvShopName.setOnClickListener { v: View? ->
            actionListener.onCartShopNameClicked(
                    cartShopHolderData.shopGroupAvailableData.shopId,
                    cartShopHolderData.shopGroupAvailableData.shopName)
        }
    }

    private fun renderShopBadge(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.isOfficialStore || cartShopHolderData.shopGroupAvailableData.isGoldMerchant) {
            if (cartShopHolderData.shopGroupAvailableData.shopBadge?.isNotEmpty() == true) {
                ImageHandler.loadImageWithoutPlaceholder(imgShopBadge, cartShopHolderData.shopGroupAvailableData.shopBadge)
                imgShopBadge.show()
            }
        } else {
            imgShopBadge.gone()
        }
    }

    private fun renderCartItems(cartShopHolderData: CartShopHolderData) {
        val cartItemAdapter = CartItemAdapter(cartItemAdapterListener, compositeSubscription, adapterPosition)
        cartItemAdapter.addDataList(cartShopHolderData.shopGroupAvailableData.cartItemDataList)
        val linearLayoutManager = LinearLayoutManager(rvCartItem.context)
        rvCartItem.layoutManager = linearLayoutManager
        rvCartItem.adapter = cartItemAdapter
        (rvCartItem.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    }

    private fun renderCheckBox(cartShopHolderData: CartShopHolderData) {
        cbSelectShop.isEnabled = !cartShopHolderData.shopGroupAvailableData.isError
        cbSelectShop.isChecked = cartShopHolderData.isAllSelected
        initCheckboxWatcherDebouncer(cartShopHolderData, compositeSubscription)
    }

    private fun initCheckboxWatcherDebouncer(cartShopHolderData: CartShopHolderData, compositeSubscription: CompositeSubscription) {
        cbSelectShop.let {
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
        labelFulfillment.visibility = if (cartShopHolderData.shopGroupAvailableData.isFulfillment) View.VISIBLE else View.GONE
        if (cartShopHolderData.shopGroupAvailableData.fulfillmentName?.isNotBlank() == true) {
            tvFulfillDistrict.show()
            tvFulfillDistrict.text = cartShopHolderData.shopGroupAvailableData.fulfillmentName
        } else {
            tvFulfillDistrict.gone()
        }
    }

    private fun renderEstimatedTimeArrival(cartShopHolderData: CartShopHolderData) {
        val eta = cartShopHolderData.shopGroupAvailableData.estimatedTimeArrival
        if (eta.isNotBlank()) {
            textEstimatedTimeArrival.text = eta
            textEstimatedTimeArrival.show()
            separatorEstimatedTimeArrival.show()
        } else {
            textEstimatedTimeArrival.gone()
            separatorEstimatedTimeArrival.gone()
        }
    }

    private fun renderErrorItemHeader(data: CartShopHolderData) {
        if (data.shopGroupAvailableData.isError) {
            cbSelectShop.isEnabled = false
            flShopItemContainer.foreground = ContextCompat.getDrawable(flShopItemContainer.context, R.drawable.fg_disabled_item)
            llShopContainer.setBackgroundResource(R.drawable.bg_error_shop)
            if (data.shopGroupAvailableData.errorTitle?.isNotBlank() == true) {
                val errorDescription = data.shopGroupAvailableData.errorDescription
                if (errorDescription?.isNotBlank() == true) {
                    tickerError.tickerTitle = data.shopGroupAvailableData.errorTitle
                    tickerError.setTextDescription(errorDescription)
                } else {
                    tickerError.tickerTitle = null
                    tickerError.setTextDescription(data.shopGroupAvailableData.errorTitle ?: "")
                }
                tickerError.tickerType = TYPE_ERROR
                tickerError.tickerShape = SHAPE_LOOSE
                tickerError.closeButtonVisibility = View.GONE
                tickerError.show()
                tickerError.post {
                    tickerError.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                    tickerError.requestLayout()
                }
                layoutError.show()
            } else {
                layoutError.gone()
            }
        } else {
            cbSelectShop.isEnabled = true
            flShopItemContainer.foreground = ContextCompat.getDrawable(flShopItemContainer.context, R.drawable.fg_enabled_item)
            llShopContainer.setBackgroundColor(llShopContainer.context.resources.getColor(R.color.white))
            layoutError.gone()
        }
    }

    private fun renderWarningItemHeader(data: CartShopHolderData) {
        if (data.shopGroupAvailableData.isWarning) {
            val warningDescription = data.shopGroupAvailableData.warningDescription
            if (warningDescription?.isNotBlank() == true) {
                tickerWarning.tickerTitle = data.shopGroupAvailableData.warningTitle
                tickerWarning.setTextDescription(warningDescription)
            } else {
                tickerWarning.tickerTitle = null
                tickerWarning.setTextDescription(data.shopGroupAvailableData.warningTitle ?: "")
            }
            tickerWarning.tickerType = TYPE_WARNING
            tickerWarning.tickerShape = SHAPE_LOOSE
            tickerWarning.closeButtonVisibility = View.GONE
            tickerWarning.show()
            tickerWarning.post {
                tickerWarning.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                tickerWarning.requestLayout()
            }
            layoutWarning.show()
        } else {
            tickerWarning.gone()
            layoutWarning.gone()
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
            labelPreOrder.text = cartShopHolderData.shopGroupAvailableData.preOrderInfo
            labelPreOrder.show()
            separatorPreOrder.show()
        } else {
            labelPreOrder.gone()
            separatorPreOrder.gone()
        }
    }

    private fun renderIncidentLabel(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.incidentInfo.isNotBlank()) {
            labelIncident.text = cartShopHolderData.shopGroupAvailableData.incidentInfo
            labelIncident.show()
            separatorIncident.show()
        } else {
            labelIncident.gone()
            separatorIncident.gone()
        }
    }

    private fun renderFreeShipping(cartShopHolderData: CartShopHolderData) {
        if (cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl.isNotBlank()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(
                    imgFreeShipping, cartShopHolderData.shopGroupAvailableData.freeShippingBadgeUrl
            )
            imgFreeShipping.show()
            separatorFreeShipping.show()
        } else {
            imgFreeShipping.gone()
            separatorFreeShipping.gone()
        }
    }

    companion object {
        val TYPE_VIEW_ITEM_SHOP = R.layout.item_shop

        const val CHECKBOX_WATCHER_DEBOUNCE_TIME = 500L
    }

}