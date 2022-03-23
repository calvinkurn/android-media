package com.tokopedia.abstraction.base.view.fragment

import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity

/**
 * Fragment that used alongside BaseMultiFragActivity to support changing title
 */
abstract class BaseMultiFragment : TkpdBaseV4Fragment() {
    override fun getScreenName() = null
    abstract fun getFragmentToolbar(): Toolbar?
    abstract fun getFragmentTitle(): String?

    override fun onResume() {
        super.onResume()
        val actvt = activity
        if (actvt!= null && actvt is BaseMultiFragActivity) {
            actvt.title = getFragmentTitle()
            actvt.setUpActionBar(getFragmentToolbar())
        }
    }
}