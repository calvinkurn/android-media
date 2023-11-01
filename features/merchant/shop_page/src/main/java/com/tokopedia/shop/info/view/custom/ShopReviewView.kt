package com.tokopedia.shop.info.view.custom

import android.content.Context
import android.content.ContextWrapper
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
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
    
    private var isSwipeFromUserInteraction : Boolean = false
    private var countDownTimer: CountDownTimer? = null
    private var progressBar: ProgressBar? = null
    
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
    }

    fun render(review: ShopReview) {
        val viewpager = createViewpager()
        val tabIndicator = createTabIndicator(review.reviews.size)
        
        addView(viewpager)
        addView(tabIndicator)
        
        setupViewPager(viewpager, tabIndicator, review)
        startTimer(5_000,1_000)
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

        addViewPagerPageChangeListener(viewPager, tabIndicator, review.reviews.size)
    }

    private fun addViewPagerPageChangeListener(
        viewPager: ViewPager2,
        tabIndicator: LinearLayout,
        reviewCount: Int
    ) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (isSwipeFromUserInteraction) {
                    tabIndicator.removeAllViews()
                }
                
                for (currentIndex in Int.ZERO until reviewCount) {
                    if (currentIndex == position) {
                        tabIndicator.addView(createSelectedDot())
                    } else {
                        tabIndicator.addView(createUnselectedDot())
                    }
                }

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
        val tabIndicator =  LinearLayout(context).apply {
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

    
    private fun startTimer(totalTime: Long, intervalMillis: Long) {
        if (countDownTimer == null) {
            countDownTimer = object : CountDownTimer(totalTime, intervalMillis) {
                override fun onTick(millisUntilFinished: Long) {
                    // Update the progress bar and any other UI elements here
                    val progress = (totalTime - (millisUntilFinished/totalTime))*100
                    progressBar?.progress = progress.toInt()
                }

                override fun onFinish() {
                    // Handle timer completion
                    // For example, show a message or perform a task
                }
            }
            countDownTimer?.start()
        }
    }
    
    private fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
    }
    
    private fun createSelectedDot(): ProgressBar? {
        progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressBar?.max = 100 // Set the maximum progress value
        progressBar?.progress = 0 // Set the current progress value
        progressBar?.layoutParams = LayoutParams(
            DOT_INDICATOR_ACTIVE_WIDTH.toPx(),
            DOT_INDICATOR_ACTIVE_HEIGHT.toPx()
        )
        // Set the custom rounded background
        progressBar?.progressDrawable = ContextCompat.getDrawable(context, R.drawable.shape_shop_review_rounded_progressbar)
        
        /*val imageView = ImageView(context)
        val params = LayoutParams(DOT_INDICATOR_ACTIVE_WIDTH.toPx() , DOT_INDICATOR_ACTIVE_HEIGHT.toPx())
        params.leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
        imageView.layoutParams = params
        
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shop_info_review_dot_indicator_selected))
        
        return imageView*/
        
        return progressBar
    }

    private fun createUnselectedDot(): ImageView {
        val imageView = ImageView(context)
        val params = LayoutParams(DOT_INDICATOR_INACTIVE_WIDTH.toPx() , DOT_INDICATOR_INACTIVE_HEIGHT.toPx())
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
