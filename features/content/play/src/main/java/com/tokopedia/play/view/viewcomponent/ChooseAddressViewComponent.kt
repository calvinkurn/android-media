package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 03/06/22
 */
class ChooseAddressViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_choose_address_widget) {

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    private val chooseAddressWidget: ChooseAddressWidget = findViewById(R.id.widget_choose_addres)

    fun showWithHeight(height: Int) {
        if (rootView.height != height) {
            val layoutParams = rootView.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.height = height
            rootView.layoutParams = layoutParams
        }

        show()
    }

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {
        fun getFragmentForAddress(view: ChooseAddressViewComponent) : Fragment
    }
}