package com.tokopedia.editshipping.ui.bottomsheet

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.ui.shippingeditor.ShippingEditorFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class ShipperDetailBottomsheet {

    private var bottomSheet: BottomSheetUnify? = null
    private var rvDetailFeatures: RecyclerView? = null

    fun show(fragment: ShippingEditorFragment) {
        bottomSheet = BottomSheetUnify()
        val viewBottomSheet = View.inflate(fragment.context, R.layout.bottom_sheet_layout, null)


        fragment.context?.let { context ->
            fragment?.fragmentManager?.let {
                bottomSheet?.show(it, "show")
            }
        }
    }

}