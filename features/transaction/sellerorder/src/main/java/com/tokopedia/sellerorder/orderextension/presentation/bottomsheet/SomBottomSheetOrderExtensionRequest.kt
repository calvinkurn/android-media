package com.tokopedia.sellerorder.orderextension.presentation.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.common.util.Utils.hideKeyboard
import com.tokopedia.sellerorder.databinding.BottomsheetOrderExtensionRequestInfoBinding
import com.tokopedia.sellerorder.orderextension.presentation.adapter.OrderExtensionRequestInfoAdapter
import com.tokopedia.sellerorder.orderextension.presentation.adapter.itemdecoration.RequestExtensionInfoItemDecoration
import com.tokopedia.sellerorder.orderextension.presentation.adapter.typefactory.OrderExtensionRequestInfoAdapterTypeFactory
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.OrderExtensionRequestInfoCommentViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.adapter.viewholder.OrderExtensionRequestInfoOptionViewHolder
import com.tokopedia.sellerorder.orderextension.presentation.model.OrderExtensionRequestInfoUiModel
import com.tokopedia.sellerorder.orderextension.presentation.viewmodel.SomOrderExtensionViewModel
import com.tokopedia.unifycomponents.toPx

class SomBottomSheetOrderExtensionRequest(
    context: Context,
    private var orderId: String,
    private var data: OrderExtensionRequestInfoUiModel,
    private var viewModel: SomOrderExtensionViewModel
) : SomBottomSheet<BottomsheetOrderExtensionRequestInfoBinding>(
    childViewsLayoutResourceId = LAYOUT,
    showOverlay = true,
    showCloseButton = true,
    showKnob = false,
    clearPadding = true,
    draggable = false,
    bottomSheetTitle = "",
    context = context,
    dismissOnClickOverlay = true
), OrderExtensionRequestInfoOptionViewHolder.SomRequestExtensionOptionListener,
    OrderExtensionRequestInfoCommentViewHolder.OrderExtensionRequestInfoCommentListener {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_order_extension_request_info
    }

    private var adapter: OrderExtensionRequestInfoAdapter? = null
    private val smoothScroller by lazy {
        object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_END
            }
        }
    }

    override fun bind(view: View): BottomsheetOrderExtensionRequestInfoBinding {
        return BottomsheetOrderExtensionRequestInfoBinding.bind(view)
    }

    override fun setupChildView() {
        setupRequestExtensionInfo()
        setupRequestExtensionButton()
        setupDismissKeyboardHandler()
    }

    override fun onBottomSheetExpanded() {
        viewModel.getSomOrderExtensionRequestInfo(orderId)
    }

    override fun onBottomSheetHidden() {
        adapter?.updateItems(emptyList())
        super.onBottomSheetHidden()
    }

    override fun show() {
        if (data.completed) {
            super.dismiss()
        } else {
            (bottomSheetLayout?.findViewById<View>(com.tokopedia.unifycomponents.R.id.bottom_sheet_header)?.layoutParams as? LinearLayout.LayoutParams)?.setMargins(
                BOTTOM_SHEET_GAP_DEFAULT.toPx(),
                Int.ZERO,
                BOTTOM_SHEET_GAP_DEFAULT.toPx(),
                BOTTOM_SHEET_GAP_DEFAULT.toPx()
            )
            super.show()
        }
    }

    override fun dismiss(): Boolean {
        return if (isShowing()) {
            if (!dismissing) {
                dismissing = true
                viewModel.requestDismissOrderExtensionRequestInfoBottomSheet()
            }
            true
        } else false
    }

    override fun onCommentChange(element: OrderExtensionRequestInfoUiModel.CommentUiModel) {
        if (!dismissing) {
            viewModel.updateOrderExtensionRequestInfoOnCommentChanged(element)
        }
    }

    override fun onOptionChecked(element: OrderExtensionRequestInfoUiModel.OptionUiModel?) {
        if (!dismissing) {
            viewModel.updateOrderExtensionRequestInfoOnSelectedOptionChanged(element)
        }
    }

    private fun setupRequestExtensionInfo() {
        binding?.run {
            if (rvRequestExtensionInfo.adapter == null) {
                adapter = adapter ?: OrderExtensionRequestInfoAdapter(
                    OrderExtensionRequestInfoAdapterTypeFactory(
                        this@SomBottomSheetOrderExtensionRequest,
                        this@SomBottomSheetOrderExtensionRequest
                    )
                )
                rvRequestExtensionInfo.adapter = adapter
            }
            if (rvRequestExtensionInfo.itemDecorationCount == Int.ZERO) {
                rvRequestExtensionInfo.addItemDecoration(RequestExtensionInfoItemDecoration())
            }
            updateRequestExtensionInfoItemViews()
        }
    }

    private fun setupRequestExtensionButton() {
        binding?.btnSubmitRequestExtension?.run {
            isLoading = data.processing
            if (!data.processing) {
                text = context.getString(R.string.bottomsheet_order_extension_request_button_text)
            }
            setOnClickListener {
                binding?.root?.hideKeyboard()
                if (!dismissing && !data.isLoadingOrderExtensionRequestInfo()) {
                    binding?.rvRequestExtensionInfo?.focusedChild?.clearFocus()
                    viewModel.sendOrderExtensionRequest(orderId)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupDismissKeyboardHandler() {
        binding?.rvRequestExtensionInfo?.run { hideKeyboardHandler.attachListener(this) }
    }

    private fun updateRequestExtensionInfoItemViews() {
        binding?.rvRequestExtensionInfo?.post {
            adapter?.updateItems(data.items.filter { it.show })
            scrollToRequestFocusItem()
        }
    }

    private fun scrollToRequestFocusItem() {
        binding?.rvRequestExtensionInfo?.post {
            val requestFocusPosition = adapter?.getRequestFocusItemPosition() ?: RecyclerView.NO_POSITION
            if (requestFocusPosition != RecyclerView.NO_POSITION) {
                smoothScroller.targetPosition = requestFocusPosition
                binding?.rvRequestExtensionInfo?.layoutManager?.startSmoothScroll(smoothScroller)
            }
        }
    }

    fun setOrderId(orderId: String) {
        this.orderId = orderId
    }

    fun setData(data: OrderExtensionRequestInfoUiModel) {
        this.data = data
    }
}