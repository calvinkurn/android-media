package com.tokopedia.review.feature.inbox.buyerreview.view.listener

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

open class GlobalMainTabSelectedListener constructor(
    private val viewPager: ViewPager?,
    private val activity: Activity?
) : OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab) {
        viewPager?.currentItem = tab.position
        activity?.let {
            val focus: View? = it.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, it)
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {
        activity?.let {
            val focus: View? = it.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, it)
            }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab) {
        activity?.let {
            val focus: View? = it.currentFocus
            if (focus != null) {
                hiddenKeyboard(focus, it)
            }
        }
    }

    private fun hiddenKeyboard(v: View, activity: Activity) {
        val keyboard: InputMethodManager? =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        keyboard?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}