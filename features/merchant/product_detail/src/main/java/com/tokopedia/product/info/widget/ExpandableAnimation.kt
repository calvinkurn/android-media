package com.tokopedia.product.info.widget

import android.animation.*
import android.view.View
import android.view.ViewGroup


/**
 * Created by Yehezkiel on 16/10/20
 */
object ExpandableAnimation {
    fun expand(view: View, customHeight: Int = 0, onAnimationEndListener: (() -> Unit)? = null) {
        view.visibility = View.VISIBLE

        if (view is ViewGroup) {
            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        } else {
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        }

        val actualheight = if (customHeight != 0) customHeight else view.measuredHeight

        view.layoutParams.height = 0
        view.alpha = 0F

        val anim = ValueAnimator.ofInt(actualheight)
        anim.addUpdateListener { valueAnimator ->
            val `val` = valueAnimator.animatedValue as Int
            val layoutParams: ViewGroup.LayoutParams = view.layoutParams
            layoutParams.height = `val`
            view.layoutParams = layoutParams
        }
        val alphaAnim = ObjectAnimator.ofFloat(view, "alpha", 0F, 1F)

        alphaAnim.duration = if (actualheight > 2000) 600 else 300
        anim.duration = if (actualheight > 2000) 600 else 300

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

        alphaAnim.duration = if (actualHeight > 2000) 600 else 300
        anim.duration = if (actualHeight > 2000) 600 else 300

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