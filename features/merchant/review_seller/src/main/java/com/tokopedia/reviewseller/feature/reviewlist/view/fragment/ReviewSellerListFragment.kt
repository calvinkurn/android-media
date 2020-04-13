package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewFragmentAdapter
import kotlinx.android.synthetic.main.fragment_seller_review_list.*

/**
 * @author by milhamj on 2020-02-14.
 */
class ReviewSellerListFragment: BaseDaggerFragment() {

    private val tabList = ArrayList<ReviewFragmentAdapter.ReviewFragmentItem>()
    private var fragmentAdapter: ReviewFragmentAdapter? = null
    private var viewPager: ViewPager? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_seller_review_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initTabReview(view, tab_review.getUnifyTabLayout())
    }

    private fun setupToolbar() {
        review_toolbar.headerView?.textSize = resources.getDimension(R.dimen.toolbar_font_size)
    }

    private fun initTabReview(view: View, tabLayout: TabLayout) {
        viewPager = view.findViewById(R.id.pager_review)
        tabList.add(ReviewFragmentAdapter.ReviewFragmentItem(getString(R.string.title_review_rating_product), RatingProductFragment()))
        tabList.add(ReviewFragmentAdapter.ReviewFragmentItem(getString(R.string.title_review_inbox), InboxReviewFragment()))

        fragmentAdapter = ReviewFragmentAdapter(childFragmentManager)
        fragmentAdapter?.setItemList(tabList)
        viewPager?.adapter = fragmentAdapter
        tabLayout.setupWithViewPager(viewPager)
        fragmentAdapter?.setupLayout(context, tab_review.getUnifyTabLayout())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {
                fragmentAdapter?.handleSelectedTab(context, tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                fragmentAdapter?.handleSelectedTab(context, tab, false)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                fragmentAdapter?.handleSelectedTab(context, tab, true)
            }
        })
    }
}