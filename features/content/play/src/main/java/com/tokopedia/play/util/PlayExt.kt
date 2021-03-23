package com.tokopedia.play.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2

/**
 * Created by jegul on 14/04/20
 */
internal inline fun View.changeConstraint(transform: ConstraintSet.() -> Unit) {
    require(this is ConstraintLayout)

    val constraintSet = ConstraintSet()

    constraintSet.clone(this)
    constraintSet.transform()
    constraintSet.applyTo(this)
}

/**
 * Current implementation of ViewPager2 add tag to each fragment that follows this pattern:
 * "f{pos}" -> {pos} here means the current fragment index position in the ViewPager2
 * e.g.
 * To get fragment in position 5 of the viewpager, the tag would be f4
 * since position 5 has index position of 4
 */
internal fun ViewPager2.findFragmentByPosition(
        fragmentManager: FragmentManager,
        pos: Int,
): Fragment? {
    return fragmentManager.findFragmentByTag("f$pos")
}

internal fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return findFragmentByPosition(fragmentManager, currentItem)
}