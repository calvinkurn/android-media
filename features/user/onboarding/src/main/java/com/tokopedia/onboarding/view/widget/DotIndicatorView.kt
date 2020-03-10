package com.tokopedia.onboarding.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.onboarding.R

class DotIndicatorView : LinearLayout {

    private var dots: MutableList<ImageView> = mutableListOf()

    private lateinit var viewDot: LinearLayout
    private lateinit var viewPager: ViewPager2

    constructor(context: Context) : super(context) {
        dots.clear()
        inflateLayout()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        dots.clear()
        inflateLayout()
    }

    constructor(context: Context, attributeSet: AttributeSet, styleAttr: Int) : super(context, attributeSet, styleAttr) {
        dots.clear()
        inflateLayout()
    }

    private fun inflateLayout() {
        val layout: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.layout_dot_indicator, this, true)

        viewDot = view.findViewById(R.id.layoutDots)
    }

    fun setViewpager(viewPager2: ViewPager2) {
        this.viewPager = viewPager2
    }

    fun addDots(dotSize: Int) {
        dots.clear()
        viewDot.removeAllViews()
        (0 until dotSize).forEach { index ->
            dots.add(index, ImageView(context).apply {
                layoutParams = LayoutParams(24, 24)
                setPadding(5, 0, 5, 0)
                setImageResource(getDefaultIndicatorUnselected())
                setOnClickListener {
                    viewPager.setCurrentItem(index, true)
                }
            })

            viewDot.addView(dots[index])
        }
    }

    fun setCurrent(position: Int) {
        if (position <= viewPager.adapter?.itemCount ?: 0) {
            setAllDefaultDot()
            dots[position].apply {
                setImageResource(getDefaultIndicatorSelected())
            }
        }
    }

    private fun setAllDefaultDot() {
        dots.forEach {
            it.setImageResource(getDefaultIndicatorUnselected())
        }
    }

    //override this to change indicator color as you want
    fun getDefaultIndicatorSelected(): Int = R.drawable.unify_default_indicator_selected
    fun getDefaultIndicatorUnselected(): Int = R.drawable.unify_default_indicator_unselected
}