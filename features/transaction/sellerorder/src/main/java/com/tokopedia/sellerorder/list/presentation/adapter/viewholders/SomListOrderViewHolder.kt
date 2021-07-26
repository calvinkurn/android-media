package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.animation.LayoutTransition.CHANGING
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CHANGE_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESPOND_TO_CANCELLATION
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UBAH_NO_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_VIEW_COMPLAINT_SELLER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.item_som_list_order.view.*

open class SomListOrderViewHolder(
        itemView: View?,
        protected val listener: SomListOrderItemListener
) : AbstractViewHolder<SomListOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_order

        const val TOGGLE_SELECTION = "toggle_selection"

        private val completedOrderStatusCodes = intArrayOf(690, 691, 695, 698, 699, 700, 701)
        private val cancelledOrderStatusCodes = intArrayOf(0, 4, 6, 10, 11, 15)
        private val endedOrderStatusCode = completedOrderStatusCodes.plus(cancelledOrderStatusCodes)
    }

    override fun bind(element: SomListOrderUiModel?) {
        if (element != null) {
            setupOrderCard(element)
            // header
            setupStatusIndicator(element)
            setupCheckBox(element)
            setupOrderStatus(element)
            setupInvoice(element)
            setupBuyerName(element)
            setupDeadline(element)
            // body
            setupTicker(element)
            setupProductList(element)
            // footer
            val isOrderEnded = element.orderStatusId in endedOrderStatusCode
            setupCourierInfo(element, isOrderEnded)
            setupDestinationInfo(element, isOrderEnded)
            setupQuickActionButton(element)
            onBindFinished(element)
        }
    }

    override fun bind(element: SomListOrderUiModel?, payloads: MutableList<Any>) {
        payloads.firstOrNull()?.let {
            if (it is Bundle) {
                if (it.containsKey(TOGGLE_SELECTION)) {
                    itemView.container?.layoutTransition?.enableTransitionType(CHANGING)
                    element?.let {
                        setupOrderCard(it)
                        setupStatusIndicator(it)
                        setupCheckBox(it)
                        setupQuickActionButton(element)
                    }
                    itemView.container?.layoutTransition?.disableTransitionType(CHANGING)
                    return
                }
            } else if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is SomListOrderUiModel && newItem is SomListOrderUiModel) {
                    setupOrderCard(newItem)
                    val oldIsEnded = oldItem.orderStatusId in endedOrderStatusCode
                    val newIsEnded = newItem.orderStatusId in endedOrderStatusCode
                    itemView.container?.layoutTransition?.enableTransitionType(CHANGING)
                    if (oldItem.statusIndicatorColor != newItem.statusIndicatorColor) {
                        setupStatusIndicator(newItem)
                    }
                    if (oldItem.isChecked != newItem.isChecked || oldItem.cancelRequest != newItem.cancelRequest) {
                        setupCheckBox(newItem)
                    }
                    if (oldItem.status != newItem.status) {
                        setupOrderStatus(newItem)
                    }
                    if (oldItem.orderResi != newItem.orderResi) {
                        setupInvoice(newItem)
                    }
                    if (oldItem.buyerName != newItem.buyerName) {
                        setupBuyerName(newItem)
                    }
                    if (oldItem.deadlineText != newItem.deadlineText || oldItem.deadlineColor != newItem.deadlineColor || oldItem.preOrderType != newItem.preOrderType) {
                        setupDeadline(newItem)
                    }
                    if (oldItem.tickerInfo != newItem.tickerInfo) {
                        setupTicker(newItem)
                    }
                    if (oldItem.tickerInfo != newItem.tickerInfo || oldItem.orderProduct != newItem.orderProduct) {
                        setupProductList(newItem)
                    }
                    if (oldItem.tickerInfo != newItem.tickerInfo || oldItem.orderProduct != newItem.orderProduct) {
                        setupProductList(newItem)
                    }
                    if (oldIsEnded != newIsEnded || oldItem.courierName != newItem.courierName || oldItem.courierProductName != newItem.courierProductName) {
                        setupCourierInfo(newItem, newIsEnded)
                    }
                    if (oldIsEnded != newIsEnded || oldItem.destinationProvince != newItem.destinationProvince) {
                        setupDestinationInfo(newItem, newIsEnded)
                    }
                    if (oldItem.buttons.firstOrNull() != newItem.buttons.firstOrNull()) {
                        setupQuickActionButton(newItem)
                    }
                    onBindFinished(newItem)
                    itemView.container?.layoutTransition?.disableTransitionType(CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    protected open fun setupQuickActionButton(element: SomListOrderUiModel) {
        with(itemView) {
            val firstButton = element.buttons.firstOrNull()
            if (firstButton != null && !listener.isMultiSelectEnabled()) {
                btnQuickAction?.text = firstButton.displayName
                btnQuickAction?.buttonVariant = if (firstButton.type == SomConsts.KEY_PRIMARY_DIALOG_BUTTON) UnifyButton.Variant.FILLED else UnifyButton.Variant.GHOST
                btnQuickAction?.setOnClickListener { onQuickActionButtonClicked(element) }
                btnQuickAction?.show()
            } else {
                btnQuickAction?.gone()
            }
        }
    }

    private fun setupDestinationInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        with(itemView) {
            tvSomListDestinationValue.text = element.destinationProvince
            icSomListDestination.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
            tvSomListDestinationValue.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCourierInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        with(itemView) {
            tvSomListCourierValue.text = "${element.courierName}${" - ${element.courierProductName}".takeIf { element.courierProductName.isNotBlank() }.orEmpty()}"
            icSomListCourier.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
            tvSomListCourierValue.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupProductList(element: SomListOrderUiModel) {
        with(itemView) {
            ivSomListProduct.apply {
                loadImageRounded(element.orderProduct.firstOrNull()?.picture.orEmpty())
                if (element.tickerInfo.text.isNotBlank()) {
                    setMargin(12.toPx(), 6.5f.dpToPx().toInt(), 0, 0)
                } else {
                    setMargin(12.toPx(), 11f.dpToPx().toInt(), 0, 0)
                }
            }
            element.orderProduct.firstOrNull()?.let { product ->
                val productName = product.productName.split(" - ").firstOrNull().orEmpty().trim()
                val productVariant = product.productName.split(" - ").takeIf { it.size > 1 }?.lastOrNull().orEmpty().replace(Regex("\\s*,\\s*"), " | ").trim()

                tvSomListProductName.apply {
                    if (productVariant.isBlank()) {
                        maxLines = 2
                        isSingleLine = false
                    } else {
                        maxLines = 1
                        isSingleLine = true
                    }
                    if (element.orderProduct.size == 1 && productVariant.isBlank()) {
                        setPadding(0, 0, 1.5f.dpToPx().toInt(), 0)
                    } else {
                        setPadding(0, 0, 0, 0)
                    }
                    text = productName
                    return@apply
                }
                tvSomListProductVariant.apply {
                    text = productVariant
                    showWithCondition(productVariant.isNotBlank())
                }
                tvSomListProductExtra.apply {
                    text = getString(R.string.som_list_more_products, (element.orderProduct.size - 1).toString())
                    showWithCondition(element.orderProduct.size > 1)
                }
            }
        }
    }

    private fun setupTicker(element: SomListOrderUiModel) {
        with(itemView) {
            if (element.tickerInfo.text.isNotBlank()) {
                tickerSomListOrder.apply {
                    setTextDescription(element.tickerInfo.text)
                    tickerType = Utils.mapStringTickerTypeToUnifyTickerType(element.tickerInfo.type)
                }
            }
            tickerSomListOrder.showWithCondition(element.tickerInfo.text.isNotBlank())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDeadline(element: SomListOrderUiModel) {
        with(itemView) {
            val deadlineText = element.deadlineText
            val deadlineColor = element.deadlineColor
            if (deadlineText.isNotBlank() && deadlineColor.isNotBlank()) {
                val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900), Color.parseColor(deadlineColor))
                val textBackgroundDrawable = MethodChecker.getDrawable(context, R.drawable.bg_due_response_text).apply {
                    colorFilter = filter
                }
                val iconBackgroundDrawable = MethodChecker.getDrawable(context, R.drawable.bg_due_response_icon).apply {
                    colorFilter = filter
                }
                tvSomListDeadline.apply {
                    text = deadlineText
                    background = textBackgroundDrawable
                    val padding = getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                    setPadding(padding, padding, padding, padding)
                }
                icDeadline.apply {
                    background = iconBackgroundDrawable
                    colorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G900), ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                    val padding = getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2)
                    setPadding(padding, padding, 0, padding)
                }
                tvSomListResponseLabel.text = composeDeadlineLabel(element.preOrderType != 0)
                tvSomListResponseLabel.show()
                tvSomListDeadline.show()
                icDeadline.show()
            } else {
                tvSomListResponseLabel.gone()
                tvSomListDeadline.gone()
                icDeadline.gone()
            }
        }
    }

    private fun setupInvoice(element: SomListOrderUiModel) {
        itemView.tvSomListInvoice.apply {
            text = element.orderResi
            showWithCondition(element.orderResi.isNotBlank())
        }
    }

    private fun setupBuyerName(element: SomListOrderUiModel) {
        itemView.tvSomListBuyerName.apply {
            text = element.buyerName
            showWithCondition(element.buyerName.isNotBlank())
        }
    }

    private fun setupOrderStatus(element: SomListOrderUiModel) {
        itemView.tvSomListOrderStatus.apply {
            text = element.status
            showWithCondition(element.status.isNotBlank())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCheckBox(element: SomListOrderUiModel) {
        with(itemView) {
            checkBoxSomListMultiSelect.showWithCondition(listener.isMultiSelectEnabled())
            checkBoxSomListMultiSelect.isChecked = element.isChecked
            checkBoxSomListMultiSelect.skipAnimation()
            checkBoxSomListMultiSelect.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (element.cancelRequest != 0 && element.cancelRequestStatus != 0) {
                        listener.onCheckBoxClickedWhenDisabled()
                        true
                    } else {
                        element.isChecked = !checkBoxSomListMultiSelect.isChecked
                        listener.onCheckChanged()
                        false
                    }
                } else {
                    false
                }
            }
        }
    }

    private fun setupStatusIndicator(element: SomListOrderUiModel) {
        with(itemView) {
            if (listener.isMultiSelectEnabled()) {
                somOrderListIndicator.gone()
            } else {
                somOrderListIndicator.background = Utils.getColoredIndicator(context, element.statusIndicatorColor)
                somOrderListIndicator.show()
            }
        }
    }

    private fun onQuickActionButtonClicked(element: SomListOrderUiModel) {
        element.buttons.firstOrNull()?.let { button ->
            when (button.key) {
                KEY_TRACK_SELLER -> listener.onTrackButtonClicked(element.orderId, button.url)
                KEY_CONFIRM_SHIPPING -> listener.onConfirmShippingButtonClicked(button.displayName, element.orderId, skipValidateOrder(element))
                KEY_ACCEPT_ORDER -> listener.onAcceptOrderButtonClicked(button.displayName, element.orderId, skipValidateOrder(element))
                KEY_REQUEST_PICKUP -> listener.onRequestPickupButtonClicked(button.displayName, element.orderId, skipValidateOrder(element))
                KEY_RESPOND_TO_CANCELLATION -> listener.onRespondToCancellationButtonClicked(element)
                KEY_VIEW_COMPLAINT_SELLER -> listener.onViewComplaintButtonClicked(element)
                KEY_UBAH_NO_RESI -> listener.onEditAwbButtonClicked(element.orderId)
                KEY_CHANGE_COURIER -> listener.onChangeCourierClicked(element.orderId)
            }
        }
    }

    private fun composeDeadlineLabel(isPreOrder: Boolean): SpannableStringBuilder {
        return SpannableStringBuilder(getString(R.string.som_list_response_before_label)).apply {
            if (isPreOrder) {
                val preOrderFlagString = getString(R.string.som_list_pre_order_flag)
                append(" $preOrderFlagString")
                setSpan(StyleSpan(Typeface.BOLD), length - preOrderFlagString.length - 1, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    protected open fun onBindFinished(element: SomListOrderUiModel) {
        itemView.btnQuickAction?.let { btnQuickAction ->
            if (element.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED &&
                    element.buttons.firstOrNull()?.key == KEY_ACCEPT_ORDER &&
                    btnQuickAction.isVisible) {
                listener.onFinishBindNewOrder(btnQuickAction, adapterPosition.takeIf { it != RecyclerView.NO_POSITION }.orZero())
            }
        }
    }

    protected fun touchCheckBox(element: SomListOrderUiModel) {
        if (element.cancelRequest != 0 && element.cancelRequestStatus != 0) {
            listener.onCheckBoxClickedWhenDisabled()
        } else {
            itemView.checkBoxSomListMultiSelect.apply {
                isChecked = !isChecked
                element.isChecked = isChecked
            }
            listener.onCheckChanged()
        }
    }

    protected open fun setupOrderCard(element: SomListOrderUiModel) {
        itemView.cardSomOrder.alpha = if (listener.isMultiSelectEnabled() && hasActiveRequestCancellation(element)) 0.5f else 1f
        itemView.setOnClickListener {
            if (listener.isMultiSelectEnabled()) touchCheckBox(element)
            else listener.onOrderClicked(element)
        }
    }

    private fun hasActiveRequestCancellation(element: SomListOrderUiModel): Boolean {
        return element.cancelRequest != 0 && element.cancelRequestStatus != 0
    }

    private fun skipValidateOrder(element: SomListOrderUiModel): Boolean {
        return element.cancelRequest != 0 && element.cancelRequestStatus == 0
    }

    interface SomListOrderItemListener {
        fun onCheckChanged()
        fun onCheckBoxClickedWhenDisabled()
        fun onOrderClicked(order: SomListOrderUiModel)
        fun onTrackButtonClicked(orderId: String, url: String)
        fun onConfirmShippingButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean)
        fun onAcceptOrderButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean)
        fun onRequestPickupButtonClicked(actionName: String, orderId: String, skipValidateOrder: Boolean)
        fun onRespondToCancellationButtonClicked(order: SomListOrderUiModel)
        fun onViewComplaintButtonClicked(order: SomListOrderUiModel)
        fun onEditAwbButtonClicked(orderId: String)
        fun onChangeCourierClicked(orderId: String)
        fun onFinishBindNewOrder(view: View, itemIndex: Int)
        fun isMultiSelectEnabled(): Boolean
    }
}