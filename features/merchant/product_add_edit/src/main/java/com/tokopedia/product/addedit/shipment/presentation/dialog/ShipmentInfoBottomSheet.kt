package com.tokopedia.product.addedit.shipment.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.customview.TabletAdaptiveBottomSheet
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShipmentInfoBottomSheet: TabletAdaptiveBottomSheet() {

    private var bottomSheetState: Int = 1

    init {
        setCloseClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        if (bottomSheetState == SHIPMENT_ON_DEMAND_STATE) {
            setTitle(getString(R.string.title_shipment_on_demand))
        } else {
            setTitle(getString(R.string.title_shipment_conventional))
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupShipmentInfoBottomSheet()
    }

    private fun initChildLayout() {
        val view: View? = View.inflate(context, R.layout.add_edit_product_shipment_bottomsheet_info, null)
        setChild(view)
    }

    private fun setupShipmentInfoBottomSheet() {
        val tvShipmentInfoDesc = requireView().findViewById<Typography>(R.id.tv_info_desc)
        val btnClose = requireView().findViewById<UnifyButton>(R.id.btn_close)

        if (bottomSheetState == SHIPMENT_ON_DEMAND_STATE) {
            tvShipmentInfoDesc.text = getString(R.string.text_shipment_on_demand)
        } else {
            tvShipmentInfoDesc.text = getString(R.string.text_shipment_conventional)
        }

        btnClose.setOnClickListener {
            dismiss()
        }
    }

    fun show(manager: FragmentManager?, state: Int) {
        bottomSheetState = state
        manager?.run {
            super.show(this, "")
        }
    }

    companion object {
        val SHIPMENT_ON_DEMAND_STATE = 1
        val SHIPMENT_CONVENTIONAL_STATE = 2
    }

}

