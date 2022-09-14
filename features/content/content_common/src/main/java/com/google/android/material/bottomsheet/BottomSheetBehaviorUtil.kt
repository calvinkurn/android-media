package com.google.android.material.bottomsheet

import android.view.View
import java.lang.ref.WeakReference

/**
 * Created by kenny.hadisaputra on 09/06/22
 */
fun BottomSheetBehavior<View>.updateScrollingChild(child: View) {
    nestedScrollingChildRef = WeakReference(child)
}