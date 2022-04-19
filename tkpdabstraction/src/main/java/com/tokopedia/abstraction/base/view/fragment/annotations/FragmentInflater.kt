package com.tokopedia.abstraction.base.view.fragment.annotations

import androidx.annotation.StringDef
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater.Companion.VIEW_PAGER
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater.Companion.DEFAULT
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater.Companion.ACTIVITY
import com.tokopedia.abstraction.base.view.fragment.annotations.FragmentInflater.Companion.FRAGMENT

@Retention(AnnotationRetention.SOURCE)
@StringDef(VIEW_PAGER, ACTIVITY, FRAGMENT, DEFAULT)
annotation class FragmentInflater {
    companion object {
        const val VIEW_PAGER = "vp"
        const val ACTIVITY = "activity"
        const val FRAGMENT = "fragment"
        const val DEFAULT = "default"
    }
}