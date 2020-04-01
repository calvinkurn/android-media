package com.tokopedia.gamification.cracktoken.util

import android.animation.ValueAnimator

class AnimatorArray {

    val currentValues = arrayListOf<Int>()
    val animatorList = arrayListOf<ValueAnimator>()

    fun toInt(listOfPairs: List<android.util.Pair<Int, Int>>, callback: Function1<ArrayList<Int>, Int>) {
        listOfPairs.forEachIndexed { i, it ->
            currentValues.add(it.first)
            val animator = ValueAnimator.ofInt(it.first, it.second)
            animator.addUpdateListener {
                currentValues[i] = it.animatedValue as Int
                callback.invoke(currentValues)
            }
            animatorList.add(animator)
        }

    }
}