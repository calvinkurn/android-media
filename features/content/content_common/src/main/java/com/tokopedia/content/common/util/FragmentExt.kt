package com.tokopedia.content.common.util

import androidx.fragment.app.Fragment

/**
 * Created By : Jonathan Darwin on September 14, 2022
 */
inline fun <reified T : Fragment> Fragment.getParentFragmentByInstance(): T? {
    var parent = parentFragment

    while(parent != null) {
        if(parent is T) return parent

        parent = parent.parentFragment
    }

    return null
}