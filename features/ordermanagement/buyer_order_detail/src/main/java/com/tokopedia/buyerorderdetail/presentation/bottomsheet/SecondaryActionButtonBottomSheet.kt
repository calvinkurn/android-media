package com.tokopedia.buyerorderdetail.presentation.bottomsheet

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorderdetail.R
import com.tokopedia.buyerorderdetail.presentation.adapter.ActionButtonClickListener
import com.tokopedia.buyerorderdetail.presentation.adapter.SecondaryActionButtonAdapter
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class SecondaryActionButtonBottomSheet(
        context: Context,
        private val listener: ActionButtonClickListener) {

    private val bottomSheet: BottomSheetUnify by lazy {
        setupSecondaryActionButtonBottomSheet(context)
    }
    private val adapter: SecondaryActionButtonAdapter by lazy {
        SecondaryActionButtonAdapter(listener)
    }
    private val childView: RecyclerView by lazy {
        createChildView(context)
    }

    private fun createChildView(context: Context): RecyclerView {
        val rvSecondaryActionButton = View.inflate(context, R.layout.buyer_order_detail_secondary_action_button_bottomsheet, null) as RecyclerView
        rvSecondaryActionButton.apply {
            adapter = this@SecondaryActionButtonBottomSheet.adapter
            addItemDecoration(SecondaryActionButtonAdapter.ViewHolder.ItemDivider(context))
        }
        return rvSecondaryActionButton
    }

    private fun setupSecondaryActionButtonBottomSheet(context: Context): BottomSheetUnify {
        return BottomSheetUnify().apply {
            setTitle(context.getString(R.string.secondary_action_bottomsheet_header))
            showCloseIcon = true
            overlayClickDismiss = true
            setChild(childView)
        }
    }

    fun setSecondaryActionButtons(secondaryActionButtons: List<ActionButtonsUiModel.ActionButton>) {
        adapter.setSecondaryActionButtons(secondaryActionButtons)
    }

    fun show(fragmentManager: FragmentManager) {
        bottomSheet.show(fragmentManager, SecondaryActionButtonBottomSheet::class.java.simpleName)
    }

    fun dismiss() {
        bottomSheet.dismiss()
    }
}