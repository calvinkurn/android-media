package com.tokopedia.epharmacy.ui.fragment

import android.content.Context
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.epharmacy.adapters.EPharmacySecondaryActionButtonAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.epharmacy.R
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse

class EPharmacySecondaryActionButtonBottomSheet(
    context: Context,
    private val listener: ActionButtonClickListener?) {

    private val bottomSheet: BottomSheetUnify by lazy {
        setupEPharmacySecondaryActionButtonBottomSheet(context)
    }
    private val adapter: EPharmacySecondaryActionButtonAdapter by lazy {
        EPharmacySecondaryActionButtonAdapter(listener)
    }
    private val childView: RecyclerView by lazy {
        createChildView(context)
    }

    private fun createChildView(context: Context): RecyclerView {
        val rvSecondaryActionButton = View.inflate(context, R.layout.item_epharmacy_order_detail_secondary_action_button, null) as RecyclerView
        rvSecondaryActionButton.apply {
            adapter = this@EPharmacySecondaryActionButtonBottomSheet.adapter
        }
        return rvSecondaryActionButton
    }

    private fun setupEPharmacySecondaryActionButtonBottomSheet(context: Context): BottomSheetUnify {
        return BottomSheetUnify().apply {
            setTitle("")
            clearContentPadding = true
            showCloseIcon = true
            overlayClickDismiss = true
            setChild(childView)
        }
    }

    fun setSecondaryActionButtons(secondaryActionButtons: List<EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?>) {
        adapter.setSecondaryActionButtons(secondaryActionButtons)
    }

    fun show(fragmentManager: FragmentManager) {
        bottomSheet.show(fragmentManager, EPharmacySecondaryActionButtonBottomSheet::class.java.simpleName)
    }

    fun dismiss() {
        if (bottomSheet.isAdded) {
            bottomSheet.dismiss()
        }
    }

    interface ActionButtonClickListener {
        fun onActionButtonClicked(isFromPrimaryButton: Boolean, button: EPharmacyOrderDetailResponse.GetConsultationOrderDetail.EPharmacyOrderButtonModel?)
    }
}
