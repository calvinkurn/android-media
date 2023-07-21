package com.tokopedia.tokopedianow.category.presentation.callback

import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.addOneTimeGlobalLayoutListener
import com.tokopedia.tokopedianow.common.view.TokoNowView

class CategoryL2PageChangeCallback(
    private val viewPager2: ViewPager2,
    tokoNowView: TokoNowView
) : ViewPager2.OnPageChangeCallback() {

    private val fragmentManager: FragmentManager

    init {
        this.fragmentManager = tokoNowView.getFragmentManagerPage()
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        fragmentManager.fragments.getOrNull(position)?.view?.let { view ->
            view.addOneTimeGlobalLayoutListener {
                val widthMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY)
                val heightMeasureSpec =
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                view.measure(widthMeasureSpec, heightMeasureSpec)

                if (viewPager2.layoutParams.height != view.measuredHeight) {
                    viewPager2.layoutParams = viewPager2.layoutParams.apply {
                        height = view.measuredHeight
                    }
                }
            }
        }
    }
}
