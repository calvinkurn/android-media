package com.tokopedia.promocheckoutmarketplace.presentation.lastseen.presentation

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class PromoCheckoutLastSeenBottomsheet {

    fun show(parentView: View) {

        val viewTarget: LinearLayout = parentView.findViewById(R.id.bottom_sheet_wrapper)
        val bottomSheetBehavior = BottomSheetBehavior.from(viewTarget)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

//        val bottomSheetBehaviorContent = LinearLayout(parentView.context)
//        bottomSheetBehaviorContent.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

//        viewTarget.addView(bottomSheetBehaviorContent)

//        bottomSheetBehaviorContent.removeAllViews()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        bottomSheetBehaviorContent.addView(generateDynamicView())

    }
}