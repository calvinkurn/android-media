package com.tokopedia.dilayanitokopedia.home.presentation.viewholder.recomendation

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.dilayanitokopedia.R
import com.tokopedia.dilayanitokopedia.databinding.DtHomeRecommendationViewholderBinding
import com.tokopedia.dilayanitokopedia.home.presentation.activity.DtHomeActivity
import com.tokopedia.dilayanitokopedia.home.presentation.datamodel.HomeRecommendationFeedDataModel
import com.tokopedia.dilayanitokopedia.home.presentation.fragment.DtHomeRecommendationForYouFragment
import com.tokopedia.dilayanitokopedia.home.presentation.listener.DtHomeCategoryListener
import com.tokopedia.discovery.common.utils.toDpInt
import com.tokopedia.utils.view.binding.viewBinding

class HomeRecommendationFeedViewHolder(
    itemView: View,
    private val listener: DtHomeCategoryListener
) : AbstractViewHolder<HomeRecommendationFeedDataModel>(itemView) {

    private val context: Context = itemView.context
    private var binding: DtHomeRecommendationViewholderBinding? by viewBinding()

    override fun bind(homeRecommendationFeedDataModel: HomeRecommendationFeedDataModel) {
        initSinglePageForYou()
    }

    /**
     * its p0 Dev
     * will changed or delete in p1
     */

    private fun initSinglePageForYou() {
        val viewPager = binding?.viewPagerHomeFeeds
        val container = binding?.homeRecommendationFeedContainer
        val layoutParams = container?.layoutParams

        val paddingTopOutsideSticky = 16f.toDpInt()

        /**
         * sticky Effect & scroll inside recommendation for you
         * specify height for viewpager, so it can't scroll up anymore and create sticky Effect
         */
        layoutParams?.height = listener.windowHeight + paddingTopOutsideSticky - listener.homeMainAnchorTabHeight
        container?.layoutParams = layoutParams

        val adapter = ViewPagerAdapter((context as DtHomeActivity).supportFragmentManager)
        viewPager?.adapter = adapter
        viewPager?.currentItem = 0
        adapter.notifyDataSetChanged()
    }

    private class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return DtHomeRecommendationForYouFragment.newInstance()
        }

        override fun getCount() = 1

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dt_home_recommendation_viewholder
    }
}
