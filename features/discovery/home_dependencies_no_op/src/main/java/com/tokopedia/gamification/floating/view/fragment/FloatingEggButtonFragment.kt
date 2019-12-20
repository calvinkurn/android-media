package com.tokopedia.gamification.floating.view.fragment

import androidx.fragment.app.Fragment

class FloatingEggButtonFragment: Fragment() {

    fun loadEggData() {

    }

    fun hideOnScrolling() {

    }

    fun setOnDragListener(onDragListener: OnDragListener) {

    }

    fun getEgg(): Egg {
        return Egg()
    }

    fun moveEgg(i: Int) {

    }

    interface OnDragListener {
        fun onDragStart()
        fun onDragEnd()
    }
}

class Egg {

    fun getHeight(): Int {
        return 0
    }

    fun getLocationOnScreen(positionEgg: IntArray) {

    }
}