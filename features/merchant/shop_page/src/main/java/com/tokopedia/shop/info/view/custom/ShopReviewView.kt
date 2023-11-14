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
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment
import com.tokopedia.shop_widget.customview.ProgressibleTabIndicatorView
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
        private const val DOT_INDICATOR_MARGIN_TOP = 16
    }

    private var onReviewImageClick: (ShopReview.Review) -> Unit = {}
    private var onReviewImageViewAllClick: (ShopReview.Review) -> Unit = {}

    fun renderReview(lifecycle: Lifecycle, fragment: Fragment, review: ShopReview) {
        removeAllViews()

        val viewpager = createViewpager()
        addView(viewpager)

        val tabIndicator = if (review.reviews.size == Int.ONE) {
            null
        } else {
            val tabIndicator = createTabIndicator()
            addView(tabIndicator)
            tabIndicator
        }

        setupViewPager(viewpager, review, fragment, lifecycle, tabIndicator)
    }

    private fun setupViewPager(
        viewPager: ViewPager2,
        review: ShopReview,
        fragment: Fragment,
        lifecycle: Lifecycle,
        tabIndicator: ProgressibleTabIndicatorView?
    ) {
        val fragments = createFragments(review.reviews)

        val pagerAdapter = ReviewViewPagerAdapter(fragment, fragments)
        viewPager.adapter = pagerAdapter

        addViewPagerPageChangeListener(viewPager, review.reviews.size, lifecycle, tabIndicator)
    }

    private fun addViewPagerPageChangeListener(
        viewPager: ViewPager2,
        reviewCount: Int,
        lifecycle: Lifecycle,
        tabIndicator: ProgressibleTabIndicatorView?
    ) {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                tabIndicator?.showTabIndicatorWithLifecycle(
                    tabIndicatorCount = reviewCount,
                    lifecycle = lifecycle,
                    selectedPosition = position,
                    onProgressFinish = {
                        val currentItemPosition = viewPager.currentItem
                        val isLastItem = currentItemPosition == reviewCount - Int.ONE
                        val nextItem = currentItemPosition + Int.ONE

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

    private fun createTabIndicator(): ProgressibleTabIndicatorView {
        val tabIndicator = ProgressibleTabIndicatorView(context).apply {
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
        return reviews.map { review ->
            val fragment = ReviewViewPagerItemFragment.newInstance(review)
            fragment.setOnReviewImageClick { onReviewImageClick(it) }
            fragment.setOnReviewImageViewAllClick { onReviewImageViewAllClick(it) }
            fragment
        }
    }

    fun setOnReviewImageClick(onReviewImageClick: (ShopReview.Review) -> Unit) {
        this.onReviewImageClick = onReviewImageClick
    }

    fun setOnReviewImageViewAllClick(onReviewImageViewAllClick: (ShopReview.Review) -> Unit) {
        this.onReviewImageViewAllClick = onReviewImageViewAllClick
    }

    private class ReviewViewPagerAdapter(
        fragment: Fragment,
        private val fragments: List<Fragment>
    ) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }
}
