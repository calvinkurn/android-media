package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewFragmentAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import kotlinx.android.synthetic.main.fragment_seller_review_list.*

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewListFragment: BaseDaggerFragment() {

    private var fragmentAdapter: ReviewFragmentAdapter? = null
    private val tabList = ArrayList<ReviewFragmentAdapter.ReviewFragmentItem>()
    private var viewPager: ViewPager? = null
    private var toolbar: Toolbar? = null


    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_seller_review_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.pager_review)
        toolbar = view.findViewById(R.id.toolbar_review)

        initActionBar()
        initTabReview()
    }


    private fun initTabReview() {

        tabList.add(ReviewFragmentAdapter.ReviewFragmentItem(getString(R.string.title_review_rating_product), RatingProductFragment()))
        tabList.add(ReviewFragmentAdapter.ReviewFragmentItem(getString(R.string.title_review_inbox), InboxReviewFragment()))

        fragmentAdapter = ReviewFragmentAdapter(childFragmentManager)
        fragmentAdapter?.setItemList(tabList)

        viewPager?.adapter = fragmentAdapter
        tab_review.setupWithViewPager(viewPager)

    }

    private fun initActionBar() {
        activity?.let {
            (it as AppCompatActivity).run {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                supportActionBar?.title = getString(R.string.title_review_rating_product)
            }
        }
    }
}