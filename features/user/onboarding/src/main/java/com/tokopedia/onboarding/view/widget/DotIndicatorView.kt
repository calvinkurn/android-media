package com.tokopedia.onboarding.view.widget

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.databinding.LayoutDotIndicatorBinding

class DotIndicatorView : LinearLayout {

    private var dots: MutableList<ImageView> = mutableListOf()

    private var binding: LayoutDotIndicatorBinding? = null

    private lateinit var viewPager: ViewPager2

    private var dotSize: Int = 24

    init {
        binding = LayoutDotIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
    }

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
        if (getScreenHeight() <= 700) {
            this.dotSize = 24
        } else {
            this.dotSize = 32
        }
    }

    fun setViewpager(viewPager2: ViewPager2) {
        this.viewPager = viewPager2
    }

    fun addDots(itemsSize: Int) {
        dots.clear()
        binding?.layoutDots?.removeAllViews()
        (0 until itemsSize).forEach { index ->
            dots.add(index, ImageView(context).apply {
                layoutParams = LayoutParams(dotSize, dotSize)
                setPadding(5, 0, 5, 0)
                setImageResource(getDefaultIndicatorUnselected())
                setOnClickListener {
                    viewPager.setCurrentItem(index, true)
                }
            })

            binding?.layoutDots?.addView(dots[index])
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

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        val windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels.pxToDp(displayMetrics)
    }

    //override this to change indicator color as you want
    fun getDefaultIndicatorSelected(): Int = R.drawable.indicator_onboarding_selected_default
    fun getDefaultIndicatorUnselected(): Int = R.drawable.indicator_onboarding_unselected_default
}