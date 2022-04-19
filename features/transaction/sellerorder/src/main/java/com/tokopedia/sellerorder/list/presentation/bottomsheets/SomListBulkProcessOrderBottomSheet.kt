package com.tokopedia.sellerorder.list.presentation.bottomsheets

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.presenter.SomBottomSheet
import com.tokopedia.sellerorder.databinding.BottomsheetSomListBulkProcessOrderBinding
import com.tokopedia.sellerorder.list.presentation.adapter.typefactories.SomListBulkProcessOrderTypeFactory
import com.tokopedia.sellerorder.list.presentation.models.BaseSomListBulkProcessOrderUiModel

class SomListBulkProcessOrderBottomSheet(
        context: Context
) : SomBottomSheet<BottomsheetSomListBulkProcessOrderBinding>(LAYOUT, true, true, false, false, false, "", context, true) {

    companion object {
        private val LAYOUT = R.layout.bottomsheet_som_list_bulk_process_order
    }

    private var listener: SomListBulkProcessOrderBottomSheetListener? = null
    private val typeFactory: SomListBulkProcessOrderTypeFactory = SomListBulkProcessOrderTypeFactory(listener)
    private val adapter = BaseListAdapter<BaseSomListBulkProcessOrderUiModel, SomListBulkProcessOrderTypeFactory>(typeFactory)

    private var showBulkActionButton: Boolean = false

    override fun bind(view: View): BottomsheetSomListBulkProcessOrderBinding {
        return BottomsheetSomListBulkProcessOrderBinding.bind(view)
    }

    override fun setupChildView() {
        binding?.run {
            setRecyclerView()
            setBulkAcceptOrderButtonClickListener()
            btnBulkProcessOrder.showWithCondition(showBulkActionButton)
        }
    }

    override fun show() {
        allowDismiss()
        super.show()
    }

    private fun setRecyclerView() {
        binding?.rvBulkProcessOrder?.apply {
            context?.let {
                adapter = this@SomListBulkProcessOrderBottomSheet.adapter
                layoutManager = LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
                val divider = it.getResDrawable(R.drawable.waiting_payment_tips_divider)
                addItemDecoration(SomListBulkProcessItemDivider(divider ?: return))
            }
        }
    }

    private fun setBulkAcceptOrderButtonClickListener() {
        binding?.btnBulkProcessOrder?.setOnClickListener {
            binding?.btnBulkProcessOrder?.isLoading = true
            preventDismiss()
            listener?.onBulkProcessOrderButtonClicked()
            binding?.btnBulkProcessOrder?.isLoading = false
        }
    }

    private fun preventDismiss() {
        dismissOnClickOverlay = false
    }

    private fun allowDismiss() {
        dismissOnClickOverlay = true
    }

    fun setItems(menuItems: List<Visitable<SomListBulkProcessOrderTypeFactory>>) {
        adapter.setElements(menuItems)
    }

    fun showButtonAction() {
        binding?.btnBulkProcessOrder?.show()
        showBulkActionButton = true
    }

    fun hideButtonAction() {
        binding?.btnBulkProcessOrder?.gone()
        showBulkActionButton = false
    }

    fun setListener(listener: SomListBulkProcessOrderBottomSheetListener) {
        this.listener = listener
        typeFactory.menuItemListener = listener
    }

    fun onBulkAcceptOrderFailed() {
        binding?.btnBulkProcessOrder?.isLoading = false
    }

    interface SomListBulkProcessOrderBottomSheetListener {
        fun onBulkProcessOrderButtonClicked()
        fun onMenuItemClicked(keyAction: String)
    }

    inner class SomListBulkProcessItemDivider(private val mDivider: Drawable) : RecyclerView.ItemDecoration() {

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0..childCount - 2) {
                val item = adapter.data.getOrNull(i)
                if (item?.showDivider == true) {
                    val child = parent.getChildAt(i)

                    val params = child.layoutParams as RecyclerView.LayoutParams

                    val dividerTop = child.bottom + params.bottomMargin
                    val dividerBottom = dividerTop + mDivider.intrinsicHeight

                    mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    mDivider.draw(canvas)
                }
            }
        }
    }
}