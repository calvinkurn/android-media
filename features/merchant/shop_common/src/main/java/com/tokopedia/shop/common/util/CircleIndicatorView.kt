package com.tokopedia.shop.common.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.shop.common.R

/**
 * Use this by calling
 * #setIndicator first then call
 * #setCurrentIndicator inside your onScrollRecyclerView
 */

class CircleIndicatorView : LinearLayout {

    lateinit var circleIndicator: LinearLayout
    private var globalIndicatorSize: Int = 0
    protected var indicatorItems: ArrayList<ImageView> = arrayListOf()

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
        globalIndicatorSize = indicatorSize
        if (indicatorSize > 1) {
            circleIndicator.visibility = View.VISIBLE
            (0..indicatorSize).forEach {
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
            (0..globalIndicatorSize).forEach {
                if (currentPosition != it) indicatorItems[it].setImageResource(getDefaultIndicatorUnselected())
                else indicatorItems[it].setImageResource(getDefaultIndicatorSelected())
            }
        }
    }

    fun getDefaultIndicatorSelected(): Int = R.drawable.ic_default_indicator_selected
    fun getDefaultIndicatorUnselected(): Int = R.drawable.ic_default_idicator_unselected
}