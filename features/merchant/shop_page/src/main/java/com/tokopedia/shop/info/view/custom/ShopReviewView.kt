package com.tokopedia.shop.info.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment
import com.tokopedia.shop_widget.customview.ProgressibleTabIndicatorView
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ShopReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    companion object {
        private const val DOT_INDICATOR_VERTICAL_MARGIN = 16
        private const val AUTO_SWIPE_INTERVAL_MILLIS = 6500L
    }

    private var onReviewImageClick: (ShopReview.Review, Int) -> Unit = { _, _ -> }
    private var onReviewImageViewAllClick: (ShopReview.Review) -> Unit = {}
    private var onReviewSwiped: (Int) -> Unit = {}
    private var reviewCount: Int = 0
    private var viewpager: ViewPager2? = null
    private var viewPagerAdapter: ReviewViewPagerAdapter? = null
    private val autoScrollJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + autoScrollJob)

    fun renderReview(lifecycle: Lifecycle, fragment: Fragment, review: ShopReview) {
        removeAllViews()

        this.reviewCount = review.reviews.size
        viewpager = createViewpager()
        addView(viewpager)

        val tabIndicator = if (review.reviews.size == Int.ONE) {
            null
        } else {
            val tabIndicator = createTabIndicator()
            addView(tabIndicator)
            tabIndicator
        }

        setupViewPager(viewpager ?: return, review, fragment, lifecycle, tabIndicator)
    }

    private fun setupViewPager(
        viewPager: ViewPager2,
        review: ShopReview,
        fragment: Fragment,
        lifecycle: Lifecycle,
        tabIndicator: ProgressibleTabIndicatorView?
    ) {
        val fragments = createFragments(review.reviews)

        viewPagerAdapter = ReviewViewPagerAdapter(fragment, fragments)
        viewPager.adapter = viewPagerAdapter

        addViewPagerPageChangeListener(viewPager, review.reviews.size, lifecycle, tabIndicator)
    }

    private fun addViewPagerPageChangeListener(
        viewPager: ViewPager2,
        reviewCount: Int,
        lifecycle: Lifecycle,
        tabIndicator: ProgressibleTabIndicatorView?
    ) {
        tabIndicator?.initWithLifecycle(
            tabIndicatorCount = reviewCount,
            lifecycle = lifecycle
        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                onReviewSwiped(position)

                stopAutoScroll()
                startAutoScroll()

                tabIndicator?.setIndicatorActive(selectedIndex = position)
            }
        })
    }

    private fun createViewpager(): ViewPager2 {
        return ViewPager2(context).apply {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.bottomMargin = DOT_INDICATOR_VERTICAL_MARGIN.toPx()
            layoutParams = params
        }
    }

    private fun createTabIndicator(): ProgressibleTabIndicatorView {
        val tabIndicator = ProgressibleTabIndicatorView(context).apply {
            val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = DOT_INDICATOR_VERTICAL_MARGIN.toPx()
            layoutParams = params

            orientation = HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
        }

        return tabIndicator
    }

    private fun createFragments(reviews: List<ShopReview.Review>): List<Fragment> {
        return reviews.map { review ->
            val fragment = ReviewViewPagerItemFragment.newInstance(review)
            fragment.setOnReviewImageClick { review, reviewImageIndex ->
                onReviewImageClick(review, reviewImageIndex)
            }
            fragment.setOnReviewImageViewAllClick { onReviewImageViewAllClick(it) }
            fragment
        }
    }

    fun setOnReviewImageClick(onReviewImageClick: (ShopReview.Review, Int) -> Unit) {
        this.onReviewImageClick = onReviewImageClick
    }

    fun setOnReviewImageViewAllClick(onReviewImageViewAllClick: (ShopReview.Review) -> Unit) {
        this.onReviewImageViewAllClick = onReviewImageViewAllClick
    }

    fun setOnReviewSwiped(onReviewSwiped: (Int) -> Unit) {
        this.onReviewSwiped = onReviewSwiped
    }

    private class ReviewViewPagerAdapter(
        fragment: Fragment,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

    fun startAutoScroll() {
        coroutineScope.launch {
            while (isActive) {
                delay(AUTO_SWIPE_INTERVAL_MILLIS)
                val itemCount = viewPagerAdapter?.itemCount ?: 0

                if (itemCount > 0) {
                    val currentItemPosition = viewpager?.currentItem.orZero()
                    val isLastItem = currentItemPosition == reviewCount - Int.ONE
                    val nextItem = currentItemPosition + Int.ONE

                    if (isLastItem) {
                        viewpager?.currentItem = Int.ZERO
                    } else {
                        viewpager?.setCurrentItem(nextItem, true)
                    }
                }
            }
        }
    }
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAutoScroll()
    }
    fun stopAutoScroll() {
        autoScrollJob.cancelChildren()
    }
}
