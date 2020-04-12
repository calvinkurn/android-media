package com.tokopedia.reviewseller.feature.reviewlist.view.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.ReviewSellerViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RatingProductFragment: BaseListFragment<Visitable<*>, SellerReviewListTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ReviewSellerViewModel? = null

    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReviewSellerViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecyclerView(view).let {
            it.clearOnScrollListeners()
            it.layoutManager = linearLayoutManager
        }
        loadInitialData()
    }

    override fun onDestroy() {
        viewModel?.reviewProductList?.removeObservers(this)
        viewModel?.productRatingOverall?.removeObservers(this)
        viewModel?.flush()
        super.onDestroy()
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
//        adapter.addElement(ReviewSellerConstant.summaryReviewProduct)
//        adapter.addElement(ReviewSellerConstant.listProductReview)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return super.createEndlessRecyclerViewListener()
    }

    override fun loadData(page: Int) {}

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvRatingProduct)
    }


    private fun observeLiveData() {
        viewModel?.productRatingOverall?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetProductRatingOverallData(it.data)
                }
                is Fail -> {
                    onErrorGetProductRatingOverallData(it.throwable)
                }
            }
        })

        viewModel?.reviewProductList?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetReviewProductListData(it.data.first, it.data.second)
                }
            }
        })
    }

    private fun onSuccessGetProductRatingOverallData(data: ProductRatingOverallUiModel) {}

    private fun onErrorGetProductRatingOverallData(throwable: Throwable) {}

    private fun onSuccessGetReviewProductListData(hasNextPage: Boolean, reviewProductList: List<ProductReviewUiModel>) {

    }
}
