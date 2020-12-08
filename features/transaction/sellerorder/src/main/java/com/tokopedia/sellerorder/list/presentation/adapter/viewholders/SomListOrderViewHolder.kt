package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.animation.LayoutTransition.CHANGING
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.os.Bundle
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

class SomListOrderViewHolder(
        itemView: View?,
        private val listener: SomListOrderItemListener
) : AbstractViewHolder<SomListOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_order

        const val TOGGLE_SELECTION = "toggle_selection"

        private val completedOrderStatusCodes = intArrayOf(690, 691, 695, 698, 699, 700, 701)
        private val cancelledOrderStatusCodes = intArrayOf(0, 4, 6, 10, 11, 15)
    }

    private var shouldContinueDraw = false

    override fun bind(element: SomListOrderUiModel?) {
        if (element != null) {
            itemView.cardSomOrder.alpha = if (listener.isMultiSelectEnabled() && element.cancelRequest != 0) 0.5f else 1f
            itemView.setOnClickListener {
                if (!listener.isMultiSelectEnabled()) listener.onOrderClicked(element)
                else touchCheckBox(element)
            }
            // header
            setupStatusIndicator(element)
            setupCheckBox(element)
            setupOrderStatus(element)
            setupInvoice(element)
            setupDeadline(element)
            // body
            setupTicker(element)
            setupProductList(element)
            // footer
            val isOrderEnded = element.orderStatusId in completedOrderStatusCodes.plus(cancelledOrderStatusCodes)
            setupCourierInfo(element, isOrderEnded)
            setupDestinationInfo(element, isOrderEnded)
            setupQuickActionButton(element)
            if (element.orderStatusId == SomConsts.STATUS_CODE_ORDER_CREATED &&
                    element.buttons.firstOrNull()?.key == KEY_ACCEPT_ORDER &&
                    itemView.btnQuickAction.isVisible) {
                listener.onFinishBindNewOrder(itemView.btnQuickAction, adapterPosition.takeIf { it != RecyclerView.NO_POSITION }.orZero())
            }
        }
    }

    override fun bind(element: SomListOrderUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        payloads.firstOrNull()?.let {
            it as Bundle
            if (it.containsKey(TOGGLE_SELECTION)) {
                itemView.container?.layoutTransition?.enableTransitionType(CHANGING)
                bind(element)
                itemView.container?.layoutTransition?.disableTransitionType(CHANGING)
            }
        }
    }

    override fun onViewRecycled() {
        shouldContinueDraw = true
        super.onViewRecycled()
    }

    private fun touchCheckBox(element: SomListOrderUiModel) {
        if (element.cancelRequest == 0) {
            itemView.checkBoxSomListMultiSelect.apply {
                isChecked = !isChecked
                element.isChecked = isChecked
            }
            listener.onCheckChanged()
        } else {
            listener.onCheckBoxClickedWhenDisabled()
        }
    }

    private fun setupQuickActionButton(element: SomListOrderUiModel) {
        with(itemView) {
            val firstButton = element.buttons.firstOrNull()
            if (firstButton != null && !listener.isMultiSelectEnabled()) {
                btnQuickAction.text = firstButton.displayName
                btnQuickAction.buttonVariant = if (firstButton.type == SomConsts.KEY_PRIMARY_DIALOG_BUTTON) UnifyButton.Variant.FILLED else UnifyButton.Variant.GHOST
                btnQuickAction.setOnClickListener { onQuickActionButtonClicked(element) }
                btnQuickAction.show()
            } else {
                btnQuickAction.gone()
            }
        }
    }

    private fun setupDestinationInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        with(itemView) {
            tvSomListDestinationValue.text = element.destinationProvince
            tvSomListDestinationLabel.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
            tvSomListDestinationValue.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCourierInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        with(itemView) {
            tvSomListCourierValue.text = "${element.courierName}${" - ${element.courierProductName}".takeIf { element.courierProductName.isNotBlank() }.orEmpty()}"
            tvSomListCourierLabel.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
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
        with(itemView) {
            tvSomListInvoice.text = element.orderResi
        }
    }

    private fun setupOrderStatus(element: SomListOrderUiModel) {
        with(itemView) {
            tvSomListOrderStatus.text = element.status
        }
    }

    private fun setupCheckBox(element: SomListOrderUiModel) {
        with(itemView) {
            checkBoxSomListMultiSelect.showWithCondition(listener.isMultiSelectEnabled())
            checkBoxSomListMultiSelect.isChecked = element.isChecked
            checkBoxSomListMultiSelect.skipAnimation()
            checkBoxSomListMultiSelect.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (element.cancelRequest == 0) {
                        element.isChecked = !checkBoxSomListMultiSelect.isChecked
                        listener.onCheckChanged()
                        false
                    } else {
                        listener.onCheckBoxClickedWhenDisabled()
                        true
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
                KEY_CONFIRM_SHIPPING -> listener.onConfirmShippingButtonClicked(element.orderId)
                KEY_ACCEPT_ORDER -> listener.onAcceptOrderButtonClicked(element.orderId)
                KEY_REQUEST_PICKUP -> listener.onRequestPickupButtonClicked(element.orderId)
                KEY_RESPOND_TO_CANCELLATION -> listener.onRespondToCancellationButtonClicked(element)
                KEY_VIEW_COMPLAINT_SELLER -> listener.onViewComplaintButtonClicked(element)
                KEY_UBAH_NO_RESI -> listener.onEditAwbButtonClicked(element.orderId)
                KEY_CHANGE_COURIER -> listener.onChangeCourierClicked(element.orderId)
            }
        }
    }

    interface SomListOrderItemListener {
        fun onCheckChanged()
        fun onCheckBoxClickedWhenDisabled()
        fun onOrderClicked(order: SomListOrderUiModel)
        fun onTrackButtonClicked(orderId: String, url: String)
        fun onConfirmShippingButtonClicked(orderId: String)
        fun onAcceptOrderButtonClicked(orderId: String)
        fun onRequestPickupButtonClicked(orderId: String)
        fun onRespondToCancellationButtonClicked(order: SomListOrderUiModel)
        fun onViewComplaintButtonClicked(order: SomListOrderUiModel)
        fun onEditAwbButtonClicked(orderId: String)
        fun onChangeCourierClicked(orderId: String)
        fun onFinishBindNewOrder(view: View, itemIndex: Int)
        fun isMultiSelectEnabled(): Boolean
    }
}