package com.tokopedia.content.product.preview.view.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.content.product.preview.view.fragment.ProductFragment
import com.tokopedia.content.product.preview.view.fragment.ReviewFragment

class ProductPreviewPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            TAB_PRODUCT_POS -> {
                ProductFragment.getOrCreate(
                    fragmentManager = fragmentManager,
                    classLoader = fragmentActivity.classLoader,
                    bundle = Bundle(),
                )
            }

            TAB_REVIEW_POS -> {
                ReviewFragment.getOrCreate(
                    fragmentManager = fragmentManager,
                    classLoader = fragmentActivity.classLoader,
                    bundle = Bundle(),
                )
            }

            else -> {
                Fragment()
            }
        }
    }

    companion object {
        const val TAB_PRODUCT_POS = 0
        const val TAB_REVIEW_POS = 1
    }
}
