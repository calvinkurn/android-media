package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewFragmentAdapter
import kotlinx.android.synthetic.main.fragment_seller_review_list.*

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewListFragment : BaseDaggerFragment() {

    private val tabList = ArrayList<ReviewFragmentAdapter.ReviewFragmentItem>()
    private var fragmentAdapter: ReviewFragmentAdapter? = null
    private var viewPager: ViewPager? = null

    override fun getScreenName(): String = ""
    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_seller_review_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initTabReview(view, tab_review.getUnifyTabLayout())
    }

    private fun setupToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(review_toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    private fun initTabReview(view: View, tabLayout: TabLayout) {
        viewPager = view.findViewById(R.id.pager_review)
        tabList.add(ReviewFragmentAdapter.ReviewFragmentItem(RatingProductFragment()))

        fragmentAdapter = ReviewFragmentAdapter(childFragmentManager)
        fragmentAdapter?.setItemList(tabList)
        viewPager?.adapter = fragmentAdapter
        tabLayout.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(viewPager))
        viewPager?.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
    }
}