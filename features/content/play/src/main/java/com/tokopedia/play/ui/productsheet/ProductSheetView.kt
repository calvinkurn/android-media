package com.tokopedia.play.ui.productsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetView(container: ViewGroup) : UIView(container) {

    private val view: View = LayoutInflater.from(container.context).inflate(R.layout.view_product_sheet, container, true)
            .findViewById(R.id.cl_product_sheet)

    private val bottomSheetBehavior = BottomSheetBehavior.from(view)

    private val maxHeight : Int
            get() = (container.height * 0.6).toInt()

    override val containerId: Int = view.id

    override fun show() {
        if (view.height != maxHeight) {
            val layoutParams = view.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = (container.height * 0.6).toInt()
            view.layoutParams = layoutParams
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
}