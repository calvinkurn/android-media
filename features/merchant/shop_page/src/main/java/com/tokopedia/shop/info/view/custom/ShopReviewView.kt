package com.tokopedia.shop.info.view.custom

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ShopInfoTopReviewViewpagerBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment
import com.tokopedia.unifycomponents.toPx

class ShopReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private var binding: ShopInfoTopReviewViewpagerBinding? = null
    private var isSwipeFromUserInteraction : Boolean = false 
    
    init {
        binding = ShopInfoTopReviewViewpagerBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun render(review: ShopReview) {
        val fragments = createFragments(review.reviews)
        val fragmentActivity = context.findActivity() ?: return
        
        val viewpager = binding?.viewpager  ?: return
        val pagerAdapter = ReviewViewPagerAdapter(fragmentActivity, fragments)
        viewpager.adapter = pagerAdapter

        val linearLayout = binding?.tabIndicator ?: return

        for (currentIndex in 0 until review.reviews.size) {
            if (currentIndex == 0) {
                linearLayout.addView(createSelectedTabIndicator())
            } else {
                linearLayout.addView(createUnselectedTabIndicator())
            }
        }
        
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                
                if (isSwipeFromUserInteraction) {
                    linearLayout.removeAllViews()    
                }
                
                for (currentIndex in 0 until review.reviews.size) {
                    if (currentIndex == position) {
                        linearLayout.addView(createSelectedTabIndicator())
                    } else {
                        linearLayout.addView(createUnselectedTabIndicator())
                    }
                }
                
                isSwipeFromUserInteraction = true
            }
        })
        
    }

    private fun createSelectedTabIndicator(): ImageView {
        val imageView = ImageView(context)
        val params = ViewGroup.LayoutParams(28.toPx() , 5.toPx())
        imageView.layoutParams = params
        
        imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.shop_info_review_dot_indicator_selected))
        
        return imageView
    }

    private fun createUnselectedTabIndicator(): ImageView {
        val imageView = ImageView(context)
        val params = ViewGroup.LayoutParams(5.toPx() , 5.toPx())
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
