package com.tokopedia.editshipping.ui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.editshipping.databinding.BottomsheetShipperDetailBinding
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorFragment
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.editshipping.util.EditShippingConstant.BOTTOMSHEET_SHIPPER_DETAIL_TITLE
import com.tokopedia.unifycomponents.BottomSheetUnify

class ShipperDetailBottomSheet {

    private var bottomSheet: BottomSheetUnify? = null

    fun show(context: Context, fragment: ShippingEditorFragment, adapter: ShippingEditorDetailsAdapter) {
        fragment.fragmentManager?.let {
            bottomSheet = BottomSheetUnify().apply {
                setTitle(BOTTOMSHEET_SHIPPER_DETAIL_TITLE)

                val child =
                    BottomsheetShipperDetailBinding.inflate(LayoutInflater.from(context), null, false)
                setupChild(child, adapter)
                setChild(child.root)
                setOnDismissListener { dismiss() }
                setCloseClickListener { dismiss() }

                show(it, null)
            }
        }
    }

    private fun setupChild(child: BottomsheetShipperDetailBinding, shippingDetailsAdapter: ShippingEditorDetailsAdapter) {
        child.rvShipperDetail.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = shippingDetailsAdapter
        }
    }
}
