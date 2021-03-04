package com.tokopedia.product.addedit.shipment.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class ShipmentInsuranceBottomSheet: BottomSheetUnify() {

    companion object {
        const val TAG = "Tag New Shipment Insurance Bottom Sheet"
    }

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDismissButton()
    }

    private fun setupDismissButton() {
        requireView().findViewById<UnifyButton>(R.id.btn_dismiss).setOnClickListener {
            dismiss()
        }
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }

    private fun initChildLayout() {
        overlayClickDismiss = true
        val contentView: View? = View.inflate(context,
                R.layout.add_edit_product_shipment_insurance_bottom_sheet_content, null)
        setChild(contentView)
    }
}