package com.tokopedia.sellerorder.list.presentation.adapter.viewholders

import android.animation.LayoutTransition.CHANGING
import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_ACCEPT_ORDER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CHANGE_COURIER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_REQUEST_PICKUP
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RESPOND_TO_CANCELLATION
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_RETURN_TO_SHIPPER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_TRACK_SELLER
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_UBAH_NO_RESI
import com.tokopedia.sellerorder.common.util.SomConsts.KEY_VIEW_COMPLAINT_SELLER
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.databinding.ItemSomListOrderBinding
import com.tokopedia.sellerorder.list.presentation.models.SomListOrderUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.dpToPx
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

open class SomListOrderViewHolder(
        itemView: View,
        protected val listener: SomListOrderItemListener
) : AbstractViewHolder<SomListOrderUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_som_list_order

        const val CARD_MARGIN_TOP_ORDER_REGULAR = 0
        const val CARD_MARGIN_TOP_ORDER_PLUS = 13
        const val CARD_ALPHA_SELECTABLE = 1f
        const val CARD_ALPHA_NOT_SELECTABLE = 0.5f

        private val completedOrderStatusCodes = intArrayOf(690, 691, 695, 698, 699, 700, 701)
        private val cancelledOrderStatusCodes = intArrayOf(0, 4, 6, 10, 11, 15)
        private val endedOrderStatusCode = completedOrderStatusCodes.plus(cancelledOrderStatusCodes)
    }

    protected val binding by viewBinding<ItemSomListOrderBinding>()

    override fun bind(element: SomListOrderUiModel?) {
        if (element != null) {
            setupOrderPlusRibbon(element)
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
            if (it is Pair<*, *>) {
                val oldItem = it.first
                val newItem = it.second
                if (oldItem is SomListOrderUiModel && newItem is SomListOrderUiModel) {
                    setupOrderCard(newItem)
                    val oldIsEnded = oldItem.orderStatusId in endedOrderStatusCode
                    val newIsEnded = newItem.orderStatusId in endedOrderStatusCode
                    val multiSelectEnableChanged = oldItem.multiSelectEnabled != newItem.multiSelectEnabled
                    binding?.container?.layoutTransition?.enableTransitionType(CHANGING)
                    if (oldItem.orderPlusData != newItem.orderPlusData) {
                        setupOrderPlusRibbon(newItem)
                    }
                    if (oldItem.statusIndicatorColor != newItem.statusIndicatorColor || multiSelectEnableChanged) {
                        setupStatusIndicator(newItem)
                    }
                    if (oldItem.isChecked != newItem.isChecked || oldItem.cancelRequest != newItem.cancelRequest || multiSelectEnableChanged) {
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
                    if (oldItem.buttons.firstOrNull() != newItem.buttons.firstOrNull() || multiSelectEnableChanged) {
                        setupQuickActionButton(newItem)
                    }
                    onBindFinished(newItem)
                    binding?.container?.layoutTransition?.disableTransitionType(CHANGING)
                    return
                }
            }
        }
        super.bind(element, payloads)
    }

    override fun onViewRecycled() {
        binding?.icSomListOrderPlusRibbon?.clearImage()
        super.onViewRecycled()
    }

    protected open fun setupQuickActionButton(element: SomListOrderUiModel) {
        binding?.run{
            val firstButton = element.buttons.firstOrNull()
            if (firstButton != null && !element.multiSelectEnabled) {
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
        binding?.run {
            tvSomListDestinationValue.text = element.destinationProvince
            icSomListDestination.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
            tvSomListDestinationValue.showWithCondition(element.destinationProvince.isNotBlank() && !orderEnded)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupCourierInfo(element: SomListOrderUiModel, orderEnded: Boolean) {
        binding?.run {
            tvSomListCourierValue.text = "${element.courierName}${" - ${element.courierProductName}".takeIf { element.courierProductName.isNotBlank() }.orEmpty()}"
            icSomListCourier.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
            tvSomListCourierValue.showWithCondition(element.courierName.isNotBlank() && !orderEnded)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupProductList(element: SomListOrderUiModel) {
        binding?.run {
            ivSomListProduct.apply {
                loadImageRounded(element.orderProduct.firstOrNull()?.picture.orEmpty())
                if (element.tickerInfo.text.isNotBlank()) {
                    setMargin(12.toPx(), 6.5f.dpToPx().toInt(), Int.ZERO, Int.ZERO)
                } else {
                    setMargin(12.toPx(), 11f.dpToPx().toInt(), Int.ZERO, Int.ZERO)
                }
            }
            val displayedProduct = element.orderProduct.firstOrNull()
            displayedProduct?.let { product ->
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
                        setPadding(Int.ZERO, Int.ZERO, 1.5f.dpToPx().toInt(), Int.ZERO)
                    } else {
                        setPadding(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                    }
                    text = productName
                    return@apply
                }
                tvSomListProductVariant.apply {
                    text = productVariant
                    showWithCondition(productVariant.isNotBlank())
                }
                tvSomListProductExtra.apply {
                    text = getString(R.string.som_list_more_products, (element.productCount - 1).toString())
                    showWithCondition(element.productCount > 1)
                }
            }
        }
    }

    private fun setupTicker(element: SomListOrderUiModel) {
        binding?.run {
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
        binding?.run {
            val deadlineText = element.deadlineText
            val deadlineBackground = Utils.getColoredDeadlineBackground(
                context = root.context,
                colorHex = element.deadlineColor,
                defaultColor = com.tokopedia.unifyprinciples.R.color.Unify_YN600
            )
            if (deadlineText.isNotBlank()) {
                tvSomListDeadline.text = deadlineText
                tvSomListResponseLabel.text = composeDeadlineLabel(element.preOrderType != 0)
                layoutSomListDeadline.background = deadlineBackground
                tvSomListResponseLabel.show()
                layoutSomListDeadline.show()
            } else {
                tvSomListResponseLabel.gone()
                layoutSomListDeadline.gone()
            }
        }
    }

    private fun setupInvoice(element: SomListOrderUiModel) {
        binding?.tvSomListInvoice?.run {
            text = element.orderResi
            showWithCondition(element.orderResi.isNotBlank())
        }
    }

    private fun setupBuyerName(element: SomListOrderUiModel) {
        binding?.tvSomListBuyerName?.run {
            text = element.buyerName
            showWithCondition(element.buyerName.isNotBlank())
        }
    }

    private fun setupOrderStatus(element: SomListOrderUiModel) {
        binding?.tvSomListOrderStatus?.run {
            text = element.status
            showWithCondition(element.status.isNotBlank())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCheckBox(element: SomListOrderUiModel) {
        binding?.run {
            checkBoxSomListMultiSelect.showWithCondition(element.multiSelectEnabled)
            checkBoxSomListMultiSelect.isChecked = element.isChecked
            checkBoxSomListMultiSelect.skipAnimation()
            checkBoxSomListMultiSelect.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (element.cancelRequest != Int.ZERO && element.cancelRequestStatus != Int.ZERO) {
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
        binding?.run {
            if (element.multiSelectEnabled) {
                somOrderListIndicator.gone()
            } else {
                somOrderListIndicator.background = Utils.getColoredIndicator(root.context, element.statusIndicatorColor)
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
                KEY_RETURN_TO_SHIPPER -> listener.onReturnToShipper(element.orderId)
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
        binding?.btnQuickAction?.let { btnQuickAction ->
            listener.onFinishBindOrder(btnQuickAction, adapterPosition.takeIf { it != RecyclerView.NO_POSITION }.orZero())
        }
    }

    protected fun touchCheckBox(element: SomListOrderUiModel) {
        if (element.cancelRequest != Int.ZERO && element.cancelRequestStatus != Int.ZERO) {
            listener.onCheckBoxClickedWhenDisabled()
        } else {
            binding?.checkBoxSomListMultiSelect?.run {
                isChecked = !isChecked
                element.isChecked = isChecked
            }
            listener.onCheckChanged()
        }
    }

    private fun setupOrderPlusRibbon(element: SomListOrderUiModel) {
        val showOrderPlusRibbon = element.orderPlusData?.logoUrl.isNullOrBlank().not()
        binding?.ivSomListRibbonBackground?.loadImage(R.drawable.ic_som_list_order_plus_ribbon)
        binding?.loaderIcSomListOrderPlusRibbon?.showWithCondition(showOrderPlusRibbon)
        binding?.icSomListOrderPlusRibbon?.run {
            loadImage(element.orderPlusData?.logoUrl.orEmpty()) {
                listener(onSuccess = { _, _ ->
                    binding?.loaderIcSomListOrderPlusRibbon?.gone()
                }, onError = {
                    binding?.loaderIcSomListOrderPlusRibbon?.gone()
                })
            }
            showWithCondition(showOrderPlusRibbon)
        }
        binding?.containerSomListOrderPlusRibbon?.showWithCondition(showOrderPlusRibbon)
    }

    protected open fun setupOrderCard(element: SomListOrderUiModel) {
        binding?.cardSomOrder?.alpha =
            if (element.multiSelectEnabled && element.hasActiveRequestCancellation()) {
                CARD_ALPHA_NOT_SELECTABLE
            } else {
                CARD_ALPHA_SELECTABLE
            }
        binding?.root?.setOnClickListener {
            if (element.multiSelectEnabled) touchCheckBox(element)
            else listener.onOrderClicked(element)
        }
        binding?.cardSomOrder?.setMargin(
            Int.ZERO,
            if (element.orderPlusData != null) {
                CARD_MARGIN_TOP_ORDER_PLUS.toPx()
            } else {
                CARD_MARGIN_TOP_ORDER_REGULAR.toPx()
            },
            Int.ZERO,
            Int.ZERO
        )
    }

    private fun skipValidateOrder(element: SomListOrderUiModel): Boolean {
        return element.cancelRequest != Int.ZERO && element.cancelRequestStatus == Int.ZERO
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
        fun onFinishBindOrder(view: View, itemIndex: Int)
        fun onReturnToShipper(orderId: String)
    }
}
