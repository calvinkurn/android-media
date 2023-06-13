package com.tokopedia.abstraction.base.view.fragment

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

interface IBaseMultiFragment {
    fun getScreenName():String
    fun getFragmentToolbar(): Toolbar?
    fun getFragmentTitle(): String?

    fun navigateToNewFragment(fragment: Fragment)
    fun onResume()
    fun getShouldPopBackStackImmediate(): Boolean
}
