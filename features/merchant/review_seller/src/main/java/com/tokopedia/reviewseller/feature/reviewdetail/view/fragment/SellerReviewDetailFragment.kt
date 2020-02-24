package com.tokopedia.reviewseller.feature.reviewdetail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.RatingBarAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.HeaderModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarModel
import kotlinx.android.synthetic.main.fragment_seller_review_detail.view.*

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailFragment : BaseDaggerFragment() {

    private val headerAdapter by lazy {
        BaseListAdapter<HeaderModel, SellerReviewDetailAdapterTypeFactory>(SellerReviewDetailAdapterTypeFactory())
    }

    private val ratingBarAdapter by lazy {
        RatingBarAdapter(mutableListOf(), context)
    }

    override fun getScreenName(): String = "SellerReviewDetail"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seller_review_detail, container, false)
        prepareView(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitialData()
    }

    override fun initInjector() {}

    private fun loadInitialData() {
        ratingBarAdapter.list = ReviewSellerConstant.listRatingBarData
        headerAdapter.addElement(ReviewSellerConstant.headerRatingData)
        headerAdapter.addElement(RatingBarModel(ratingBarAdapter))
    }

    private fun prepareView(view: View) {
        view.review_detail_rv.adapter = headerAdapter
    }

}