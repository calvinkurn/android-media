package com.tokopedia.review.feature.inbox.buyerreview.view.listener

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

open class GlobalMainTabSelectedListener constructor(
    private val viewPager: ViewPager,
    private val activity: Activity?
) : OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager.currentItem = tab.position
        if (activity != null) {
            val focus: View? = activity.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, activity)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        if (activity != null) {
            val focus: View? = activity.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, activity)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        if (activity != null) {
            val focus: View? = activity.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, activity)
            }
        }
    }

    private fun hiddenKeyboard(v: View, activity: Activity?) {
        val keyboard: InputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(v.windowToken, 0)
    }

    companion object {
        private val TAG: String = GlobalMainTabSelectedListener::class.java.simpleName
    }
}