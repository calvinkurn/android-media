package com.tokopedia.shop.info.view.custom

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.tokopedia.shop.databinding.ShopInfoTopReviewViewpagerBinding
import com.tokopedia.shop.info.domain.entity.ShopReview
import com.tokopedia.shop.info.view.fragment.ReviewViewPagerItemFragment

class ShopReviewView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    
    private var binding: ShopInfoTopReviewViewpagerBinding? = null

    init {
        binding = ShopInfoTopReviewViewpagerBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    fun render(review: ShopReview) {
        val viewpager = binding?.viewpager  ?: return
        val tabLayout = binding?.tabLayout ?: return
        
        val fragments = createFragments(review.reviews)
        val fragmentActivity = context.findActivity() ?: return
        
        val pagerAdapter = PagerAdapter(
            fragmentActivity,
            fragments
        )

        viewpager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->

        }.attach()
    }
    

    private fun createFragments(reviews: List<ShopReview.Review>): List<Fragment> {
        return reviews.map { review ->
            ReviewViewPagerItemFragment.newInstance(review)
        }
    }


    private class PagerAdapter(
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
