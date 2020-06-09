package com.tokopedia.promocheckoutmarketplace.presentation.lastseen.presentation

import android.view.View
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.promocheckoutmarketplace.R

class PromoCheckoutLastSeenBottomsheet {

    fun show(parentView: View) {

        val viewTarget: LinearLayout = parentView.findViewById(R.id.bottom_sheet_promo_last_seen)
        val bottomSheetBehavior = BottomSheetBehavior.from(viewTarget)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

//        val bottomSheetBehaviorContent = LinearLayout(parentView.context)
//        bottomSheetBehaviorContent.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

//        viewTarget.addView(bottomSheetBehaviorContent)

//        bottomSheetBehaviorContent.removeAllViews()
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehaviorContent.addView(generateDynamicView())

    }
}