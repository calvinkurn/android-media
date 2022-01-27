package com.google.android.material.bottomsheet

import android.view.View
import java.lang.ref.WeakReference

/**
 * This extension method is placed in [com.google.android.material.bottomsheet] package due to
 * [BottomSheetBehavior.nestedScrollingChildRef] is package-private,
 * therefore this extension function need to be placed in the same package as [BottomSheetBehavior]
 * This solution is inspired by this article:
 * - https://hanru-yeh.medium.com/funny-solution-that-makes-bottomsheetdialog-support-viewpager-with-nestedscrollingchilds-bfdca72235c3
 * - https://stackoverflow.com/questions/42159473/bottomsheetbehavior-with-two-recyclerview
 */
internal fun BottomSheetBehavior<View>.updateScrollingChild(child: View) {
    nestedScrollingChildRef = WeakReference(child)
}