package com.tokopedia.product.info.widget

import android.animation.*
import android.view.View
import android.view.ViewGroup


/**
 * Created by Yehezkiel on 16/10/20
 */
object ExpandableAnimation {
    fun expand(view: View, customParentWidth: Int = view.width, customHeight: Int = 0, onAnimationEndListener: (() -> Unit)? = null) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(customParentWidth, View.MeasureSpec.AT_MOST)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        if (view is ViewGroup) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            view.measure(widthSpec, heightSpec)
        }

        val actualheight = if (customHeight != 0) customHeight else view.measuredHeight

        view.layoutParams.height = 0
        view.visibility = View.VISIBLE
        view.alpha = 0F

        val anim = ValueAnimator.ofInt(actualheight)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.height = `val`
            view.layoutParams = layoutParams
        }
        val alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0F, 1F)

        alphaAnim.duration = 300
        anim.duration = 300

        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                onAnimationEndListener?.invoke()
            }
        })
        set.playTogether(anim, alphaAnim)
        set.start()
    }

    fun collapse(view: View) {
        val actualHeight = view.measuredHeight

        val anim = ValueAnimator.ofInt(actualHeight, 0)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.height = `val`
            view.layoutParams = layoutParams
        }

        val alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 1F, 0F)

        alphaAnim.duration = 300
        anim.duration = 300

        val set = AnimatorSet()
        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.visibility = View.GONE
            }
        })
        set.playTogether(anim, alphaAnim)
        set.start()
    }
}