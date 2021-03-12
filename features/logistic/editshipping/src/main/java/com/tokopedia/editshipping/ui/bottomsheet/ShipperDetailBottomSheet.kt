package com.tokopedia.editshipping.ui.bottomsheet

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorFragment
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShippingEditorDetailsAdapter
import com.tokopedia.editshipping.util.EditShippingConstant.BOTTOMSHEET_SHIPPER_DETAIL_TITLE
import com.tokopedia.unifycomponents.BottomSheetUnify

class ShipperDetailBottomSheet {

    private var bottomSheet: BottomSheetUnify? = null

    fun show(fragment: ShippingEditorFragment, adapter: ShippingEditorDetailsAdapter) {
        fragment.fragmentManager?.let {
            bottomSheet = BottomSheetUnify().apply {
                setTitle(BOTTOMSHEET_SHIPPER_DETAIL_TITLE)

                val child = View.inflate(fragment.context, R.layout.bottomsheet_shipper_detail, null)
                setupChild(child, adapter)
                setChild(child)
                setOnDismissListener { dismiss() }
                setCloseClickListener { dismiss() }

                show(it, null)
            }
        }
    }

    private fun setupChild(child: View, shippingDetailsAdapter: ShippingEditorDetailsAdapter) {
        child.findViewById<RecyclerView>(R.id.rv_shipper_detail)?.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = shippingDetailsAdapter
        }
    }

}