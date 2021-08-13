package com.tokopedia.shop.common.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show


class CustomAnimation(view: View, duration: Long, type: Int) : Animation() {
    companion object {
        const val COLLAPSE = 1
        const val EXPAND = 0
    }

    private var mView: View? = null
    private var mEndHeight: Int? = null
    private var mType: Int? = null
    private var mLayoutParams: LinearLayout.LayoutParams? = null
    var height: Int
        get() = mView?.height ?: 0
        set(height) {
            mEndHeight = height
        }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        if (interpolatedTime < 1.0f) {
            if (mType == EXPAND) {
                mLayoutParams?.height = (mEndHeight?.times(interpolatedTime))?.toInt()
            } else {
                mLayoutParams?.height = (mEndHeight?.times((1 - interpolatedTime)))?.toInt()
            }
            mView?.requestLayout()
        } else {
            if (mType == EXPAND) {
                mLayoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                mView?.requestLayout()
            } else {
                mView?.gone()
            }
        }
    }

    init {
        setDuration(duration)
        mView = view
        mEndHeight = mView?.height
        mLayoutParams = view.layoutParams as LinearLayout.LayoutParams
        mType = type
        if (mType == EXPAND) {
            mLayoutParams?.height = 0
        } else {
            mLayoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        view.show()
    }
}