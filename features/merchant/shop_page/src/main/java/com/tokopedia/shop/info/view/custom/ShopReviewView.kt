package com.tokopedia.shop.info.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.R
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment
import com.tokopedia.unifycomponents.toPx

class ShopReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var isSwipeFromUserInteraction: Boolean = false
    private var countDownTimer: CountDownTimer? = null

    init {
        orientation = VERTICAL
    }

    companion object {
        private const val DOT_INDICATOR_INACTIVE_HEIGHT = 5
        private const val DOT_INDICATOR_INACTIVE_WIDTH = 5
        private const val DOT_INDICATOR_ACTIVE_HEIGHT = 5
        private const val DOT_INDICATOR_ACTIVE_WIDTH = 28
        private const val DOT_INDICATOR_MARGIN_START = 3
        private const val DOT_INDICATOR_MARGIN_TOP = 16

        private const val COUNTDOWN_TIMER_TOTAL_TIME: Long = 5000
        private const val COUNTDOWN_TIMER_INTERVAL: Long = 250
        private const val ONE_HUNDRED = 100
    }

    fun render(review: ShopReview) {
        val viewpager = createViewpager()
        val tabIndicator = createTabIndicator(review.reviews.size)
        addView(viewpager)
        addView(tabIndicator)

        setupCountDownTimer(viewpager, tabIndicator, review.reviews.size)

     /*   if (tabIndicator.size.isMoreThanZero()) {
            val firstTabIndicator = tabIndicator[0]
            val progressBar = (firstTabIndicator as? ProgressBar) ?: return
            startTimer(progressBar, viewpager, review.reviews.size)
        }*/

        setupViewPager(viewpager, tabIndicator, review)
    }

    private fun getSelectedTabIndicator(tabIndicators: LinearLayout): ProgressBar? {
        for (i in 0..tabIndicators.childCount) {
            val indicator = tabIndicators.getChildAt(i)
            if (indicator is ProgressBar) {
                return indicator
            }
        }
        return null
    }

    private fun setupCountDownTimer(
        viewPager: ViewPager2,
        tabIndicators: LinearLayout,
        reviewCount: Int
    ) {
        val progressBar = getSelectedTabIndicator(tabIndicators)

        countDownTimer =
            object : CountDownTimer(COUNTDOWN_TIMER_TOTAL_TIME, COUNTDOWN_TIMER_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    val remainingProgressPercent = (millisUntilFinished.toFloat() / COUNTDOWN_TIMER_TOTAL_TIME.toFloat()) * ONE_HUNDRED.toFloat()
                    val progressPercent = ONE_HUNDRED.toFloat() - remainingProgressPercent

                    progressBar?.progress = progressPercent.toInt()
                }

                override fun onFinish() {
                    progressBar?.progress = ONE_HUNDRED

                    val currentItem = viewPager.currentItem
                    val isLastItem = currentItem == reviewCount - 1
                    val nextItem = currentItem + 1

                    if (isLastItem) {
                        viewPager.setCurrentItem(0, true)
                    } else {
                        viewPager.setCurrentItem(nextItem, true)
                    }
                }
            }
    }

    private fun setupViewPager(
        viewPager: ViewPager2,
        tabIndicator: LinearLayout,
        review: ShopReview
    ) {
        val fragments = createFragments(review.reviews)
        val fragmentActivity = context.findActivity() ?: return

        val pagerAdapter = ReviewViewPagerAdapter(fragmentActivity, fragments)
        viewPager.adapter = pagerAdapter

        addViewPagerPageChangeListener(viewPager, tabIndicator, review.reviews)
    }

    private fun addViewPagerPageChangeListener(
        viewPager: ViewPager2,
        tabIndicator: LinearLayout,
        reviews: List<ShopReview.Review>
    ) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                countDownTimer?.cancel()
                super.onPageSelected(position)

                if (isSwipeFromUserInteraction) {
                    tabIndicator.removeAllViews()
                }

                for (currentIndex in Int.ZERO until reviews.size) {
                    if (currentIndex == position) {
                        val progressBar = createSelectedDot()
                        tabIndicator.addView(progressBar)
                        setupCountDownTimer(viewPager, tabIndicator, reviews.size)
                        startTimer()
                    } else {
                        tabIndicator.addView(createUnselectedDot())
                    }
                }

                startTimer()
                isSwipeFromUserInteraction = true
            }
        })
    }

    private fun createViewpager(): ViewPager2 {
        return ViewPager2(context).apply {
            layoutParams = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun createTabIndicator(reviewCount: Int): LinearLayout {
        val tabIndicator = LinearLayout(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            params.topMargin = DOT_INDICATOR_MARGIN_TOP.toPx()
            layoutParams = params

            orientation = HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        for (currentIndex in Int.ZERO until reviewCount) {
            if (currentIndex == Int.ZERO) {
                tabIndicator.addView(createSelectedDot())
            } else {
                tabIndicator.addView(createUnselectedDot())
            }
        }

        return tabIndicator
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer?.start()
    }

    @SuppressLint("UnifyComponentUsage")
    private fun createSelectedDot(): ProgressBar {
        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
            layoutParams = LayoutParams(
                DOT_INDICATOR_ACTIVE_WIDTH.toPx(),
                DOT_INDICATOR_ACTIVE_HEIGHT.toPx()
            ).apply {
                leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
            }
            max = ONE_HUNDRED
            progress = Int.ZERO
        }

        val backgroundDrawable = ContextCompat.getDrawable(context, R.drawable.shape_shop_review_rounded_progressbar_background)
        val progressDrawable = ContextCompat.getDrawable(context, R.drawable.shape_shop_review_rounded_progressbar_progress)

        progressBar.progressDrawable = progressDrawable
        progressBar.background = backgroundDrawable

        return progressBar
    }

    private fun createUnselectedDot(): ImageView {
        val imageView = ImageView(context)
        val params = LayoutParams(DOT_INDICATOR_INACTIVE_WIDTH.toPx(), DOT_INDICATOR_INACTIVE_HEIGHT.toPx())
        params.leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
        imageView.layoutParams = params

        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shop_info_review_dot_indicator_unselected))

        return imageView
    }

    private fun createFragments(reviews: List<ShopReview.Review>): List<Fragment> {
        return reviews.map { review ->
            ReviewViewPagerItemFragment.newInstance(review)
        }
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
