package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * @author by astidhiyaa on 25/11/21
 */
class ShopCouponSheetViewComponent (container: ViewGroup,
                                    private val listener: Listener
) : ViewComponent(container, R.id.cl_shop_coupon_sheet){

    private val bottomSheetBehavior = BottomSheetBehavior.from(rootView)

    override fun show() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun hide() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    interface Listener {

    }
}