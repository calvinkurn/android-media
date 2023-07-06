package com.tokopedia.play_common.viewcomponent

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by kenny.hadisaputra on 05/04/23
 */
abstract class BottomSheetViewComponent(
    view: View
) : ViewComponent(view) {

    constructor(container: ViewGroup, @IdRes rootId: Int) : this(container.findViewById<View>(rootId))

    private val bottomSheetBehavior = try {
        BottomSheetBehavior.from(rootView)
    } catch (e: IllegalArgumentException) {
        null
    }

    init {
        bottomSheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    rootView.invisible()
                } else {
                    rootView.show()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        //first time bottomsheet should be invisible and hidden
        if (bottomSheetBehavior != null) {
            rootView.invisible()
            hide()
        }
    }

    final override fun show() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else super.show()
    }

    final override fun hide() {
        if (bottomSheetBehavior != null) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else super.hide()
    }
}
