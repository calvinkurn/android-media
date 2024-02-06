package com.tokopedia.content.product.preview.view.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tokopedia.content.product.preview.view.fragment.ProductFragment
import com.tokopedia.content.product.preview.view.fragment.ReviewFragment
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_PRODUCT_KEY
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_REVIEW_KEY
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.ProductReviewTab

class ProductPreviewPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val fragmentActivity: FragmentActivity,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val _fragments: MutableList<ProductReviewTab> = mutableListOf()
    private val fragments: List<ProductReviewTab>
        get() = _fragments

    fun insertFragment(listFragment: List<ProductReviewTab>) {
        _fragments.clear()
        _fragments.addAll(listFragment)
        notifyItemRangeChanged(0, fragments.size)
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        val tabs = fragments[position]
        return when (tabs.key) {
            TAB_PRODUCT_KEY -> {
                ProductFragment.getOrCreate(
                    fragmentManager = fragmentManager,
                    classLoader = fragmentActivity.classLoader,
                    bundle = Bundle()
                )
            }

            TAB_REVIEW_KEY -> {
                ReviewFragment.getOrCreate(
                    fragmentManager = fragmentManager,
                    classLoader = fragmentActivity.classLoader,
                    bundle = Bundle()
                )
            }

            else -> {
                Fragment()
            }
        }
    }
}
