package com.tokopedia.shop.info.view.custom

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment
import com.tokopedia.unifycomponents.toPx

class ShopReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    companion object {
        private const val COUNTDOWN_TIMER_TOTAL_TIME: Long = 5000
        private const val COUNTDOWN_TIMER_INTERVAL: Long = 100
        private const val DOT_INDICATOR_MARGIN_TOP = 16
    }

    fun render(review: ShopReview) {
        val viewpager = createViewpager()
        val tabIndicator = createTabIndicator()
        addView(viewpager)
        addView(tabIndicator)

        setupViewPager(viewpager, review, tabIndicator)
    }

    private fun setupViewPager(
        viewPager: ViewPager2,
        review: ShopReview,
        tabIndicator: ProgressibleTabLayoutView
    ) {
        val fragments = createFragments(review.reviews)
        val fragmentActivity = context.findActivity() ?: return

        val pagerAdapter = ReviewViewPagerAdapter(fragmentActivity, fragments)
        viewPager.adapter = pagerAdapter

        addViewPagerPageChangeListener(viewPager, review.reviews.size, tabIndicator)
    }

    private fun addViewPagerPageChangeListener(
        viewPager: ViewPager2,
        reviewCount: Int,
        tabIndicator: ProgressibleTabLayoutView
    ) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                println("Timer. onPageSelected position $position")
                super.onPageSelected(position)

                val config = ProgressibleTabLayoutView.Config(
                    itemCount = reviewCount,
                    totalDuration = COUNTDOWN_TIMER_TOTAL_TIME,
                    intervalDuration = COUNTDOWN_TIMER_INTERVAL
                )

                tabIndicator.renderTabIndicator(
                    config = config,
                    selectedTabIndicatorIndex = position,
                    onTimerTick = {
                    },
                    onTimerFinish = {
                        val currentItem = viewPager.currentItem
                        val isLastItem = currentItem == reviewCount - Int.ONE
                        val nextItem = currentItem + Int.ONE

                        if (isLastItem) {
                            viewPager.currentItem = Int.ZERO
                        } else {
                            viewPager.setCurrentItem(nextItem, true)
                        }
                    }
                )
            }
        })
    }

    private fun createViewpager(): ViewPager2 {
        return ViewPager2(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

    private fun createTabIndicator(): ProgressibleTabLayoutView {
        val tabIndicator = ProgressibleTabLayoutView(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            params.topMargin = DOT_INDICATOR_MARGIN_TOP.toPx()
            layoutParams = params

            orientation = HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        return tabIndicator
    }

    private fun createFragments(reviews: List<ShopReview.Review>): List<Fragment> {
        return reviews.map { review -> ReviewViewPagerItemFragment.newInstance(review) }
    }

    private class ReviewViewPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

    private fun Context.findActivity(): FragmentActivity? {
        var context = this
        while (context is ContextWrapper) {
            if (context is FragmentActivity) return context
            context = context.baseContext
        }
        return null
    }
}
