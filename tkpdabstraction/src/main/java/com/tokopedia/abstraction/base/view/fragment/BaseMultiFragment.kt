package com.tokopedia.abstraction.base.view.fragment

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity

/**
 * Fragment that used alongside BaseMultiFragActivity to support changing title
 */
abstract class BaseMultiFragment : TkpdBaseV4Fragment(), IBaseMultiFragment {
    override fun getScreenName() = ""
    abstract override fun getFragmentToolbar(): Toolbar?
    abstract override fun getFragmentTitle(): String?

    override fun navigateToNewFragment(fragment: Fragment){
        (activity as? BaseMultiFragActivity)?.navigateToNewFragment(fragment)
    }

    override fun onResume() {
        super.onResume()
        val actvt = activity
        if (actvt!= null && actvt is BaseMultiFragActivity) {
            actvt.title = getFragmentTitle()
            actvt.setUpActionBar(getFragmentToolbar())
        }
    }
}