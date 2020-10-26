package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESPOND_TO_CANCELLATION
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UBAH_NO_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_VIEW_COMPLAINT_SELLER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_som_list_order.view.*

class SomListOrderViewHolder(
        itemView: View?,
        private val listener: SomListOrderItemListener
) : AbstractViewHolder<SomListOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_order

        private val completedOrderStatusCodes = intArrayOf(690, 691, 695, 698, 699, 700, 701)
        private val cancelledOrderStatusCodes = intArrayOf(0, 4, 6, 10, 11, 15)

        var multiEditEnabled = false
    }

    override fun bind(element: SomListOrderUiModel?) {
        if (element != null) {
            itemView.setOnClickListener {
                if (!multiEditEnabled) listener.onOrderClicked(element)
                else touchCheckBox(element)
            }
            itemView.alpha = if (multiEditEnabled && element.cancelRequest != 0) 0.5f else 1f
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
        }
    }

    private fun touchCheckBox(element: SomListOrderUiModel) {
        itemView.checkBoxSomListMultiSelect.apply {
            isChecked = !isChecked
            element.isChecked = isChecked
        }
        listener.onCheckChanged()
    }

    private fun setupQuickActionButton(element: SomListOrderUiModel) {
        with(itemView) {
            val firstButton = element.buttons.firstOrNull()
            if (firstButton != null && !multiEditEnabled) {
                btnQuickAction.text = firstButton.displayName
                btnQuickAction.buttonVariant = if (firstButton.type == SomConsts.KEY_SECONDARY_DIALOG_BUTTON) UnifyButton.Variant.FILLED else UnifyButton.Variant.GHOST
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

    private fun setupCourierInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        with(itemView) {
            tvSomListCourierValue.text = element.courierName
            tvSomListCourierLabel.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
            tvSomListCourierValue.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
        }
    }

    private fun setupProductList(element: SomListOrderUiModel) {
        with(itemView) {
            ivSomListProduct.loadImageRounded(element.orderProduct.firstOrNull()?.picture.orEmpty())
            tvSomListProductName.text = element.orderProduct.firstOrNull()?.productName.orEmpty()
            tvSomListProductExtra.text = if (element.orderProduct.size > 1) {
                getString(R.string.som_list_more_products, (element.orderProduct.size - 1).toString())
            } else ""
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
                val filter: ColorFilter = LightingColorFilter(Color.BLACK, Color.parseColor(deadlineColor))
                val textBackgroundDrawable = MethodChecker.getDrawable(context, R.drawable.bg_due_response_text).apply {
                    colorFilter = filter
                }
                val iconBackgroundDrawable = MethodChecker.getDrawable(context, R.drawable.bg_due_response_icon).apply {
                    colorFilter = filter
                }
                tvSomListDeadline.apply {
                    text = deadlineText
                    background = textBackgroundDrawable
                    val padding = getDimens(R.dimen.spacing_lvl2)
                    setPadding(padding, padding, padding, padding)
                }
                icDeadline.apply {
                    background = iconBackgroundDrawable
                    colorFilter = LightingColorFilter(Color.BLACK, Color.WHITE)
                    val padding = getDimens(R.dimen.spacing_lvl2)
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
            checkBoxSomListMultiSelect.showWithCondition(multiEditEnabled)
            checkBoxSomListMultiSelect.isChecked = element.isChecked
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
            if (multiEditEnabled) {
                somOrderListIndicator.gone()
            } else {
                somOrderListIndicator.background = getColoredIndicator(context, element.statusColor)
                somOrderListIndicator.show()
            }
        }
    }

    private fun getColoredIndicator(context: Context, colorHex: String): Drawable? {
        val color = if (colorHex.length > 1) Color.parseColor(colorHex)
        else MethodChecker.getColor(context, R.color.Unify_N0)
        val drawable = MethodChecker.getDrawable(context, R.drawable.ic_order_status_indicator)
        val filter: ColorFilter = LightingColorFilter(Color.BLACK, color)
        drawable.colorFilter = filter
        return drawable
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
    }
}