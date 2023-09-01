package com.tokopedia.scp_rewards.detail.presentation.ui

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

private const val POSITION_ACTIVE = 0
private const val POSITION_HISTORY = 1

class BonusPagerAdapter : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return when (position) {
            POSITION_ACTIVE -> {

            }
            POSITION_HISTORY -> {

            }
            else -> {  }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

    override fun getCount() = 2
}
