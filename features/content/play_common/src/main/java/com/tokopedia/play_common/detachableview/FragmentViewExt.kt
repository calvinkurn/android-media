package com.tokopedia.play_common.detachableview

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

/**
 * Created by jegul on 26/04/21
 */
fun <F, V: View> F.detachableView(@IdRes idRes: Int) : FragmentViewDelegate<V> where F : Fragment, F : FragmentWithDetachableView {
    return FragmentViewDelegate(idRes)
}