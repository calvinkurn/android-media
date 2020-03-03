package com.tokopedia.reviewseller.feature.reviewlist.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment

import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory

/**
 * A simple [Fragment] subclass.
 */
class RatingProductFragment: BaseListFragment<Visitable<*>, SellerReviewListTypeFactory>() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitialData()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, SellerReviewListTypeFactory> {
        return SellerReviewListAdapter(adapterTypeFactory)
    }

    override fun getAdapterTypeFactory(): SellerReviewListTypeFactory {
        return SellerReviewListTypeFactory()
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String { return getString(R.string.title_review_rating_product) }

    override fun initInjector() {}

    override fun loadInitialData() {
        adapter.addElement(ReviewSellerConstant.filterAndSortComposition)
        adapter.addElement(ReviewSellerConstant.summaryReviewProduct)
        adapter.addElement(ReviewSellerConstant.listProductReview)
    }

    override fun loadData(page: Int) {}

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvRatingProduct)
    }

}
