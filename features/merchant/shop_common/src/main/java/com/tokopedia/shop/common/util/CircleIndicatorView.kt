package com.tokopedia.shop.common.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.shop.common.R

/**
 * For RecyclerView Indicator
 *
 * How to use this :
 *
 * 1. Make sure RecyclerView implement PagerSnapHelper()
 * 2. Call [setIndicator] to set total indicator (Call FIRST)
 * 3. Then call [setCurrentIndicator] inside your onScrollRecyclerView or wherever its need (set current indicator when swipe the recyclerview)
 * 4. #Optional @Override [getDefaultIndicatorSelected] and [getDefaultIndicatorUnselected] to customize indicator drawable
 *
 */

class CircleIndicatorView : LinearLayout {

    lateinit var circleIndicator: LinearLayout
    private var globalIndicatorSize: Int = 0
    protected var indicatorItems: ArrayList<ImageView> = arrayListOf()
    private var listTemp = mutableListOf<Int>()

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initView()
    }

    private fun initView() {
        val view = View.inflate(context, R.layout.item_circle_indicator_rv, this)
        circleIndicator = view.findViewById(R.id.circle_indicator)
    }

    fun setIndicator(indicatorSize: Int) {
        indicatorItems.clear()
        circleIndicator.removeAllViews()
        globalIndicatorSize = indicatorSize
        if (indicatorSize > 1) {
            circleIndicator.visibility = View.VISIBLE
            (0 until indicatorSize).forEach {
                val circleView = ImageView(context)
                circleView.setPadding(5, 0, 5, 0)

                if (it == 0) circleView.setImageResource(getDefaultIndicatorSelected())
                else circleView.setImageResource(getDefaultIndicatorUnselected())

                indicatorItems.add(circleView)
                circleIndicator.addView(circleView)
            }
        } else {
            circleIndicator.visibility = View.GONE
        }

    }

    fun setCurrentIndicator(currentPosition: Int) {
        if (globalIndicatorSize > 1) {
            (0 until globalIndicatorSize).forEach {
                if (currentPosition != it) indicatorItems[it].setImageResource(getDefaultIndicatorUnselected())
                else indicatorItems[it].setImageResource(getDefaultIndicatorSelected())
            }
        }
    }

    //override this to change indicator color as you want
    fun getDefaultIndicatorSelected(): Int = R.drawable.ic_default_indicator_selected
    fun getDefaultIndicatorUnselected(): Int = R.drawable.ic_default_idicator_unselected
}