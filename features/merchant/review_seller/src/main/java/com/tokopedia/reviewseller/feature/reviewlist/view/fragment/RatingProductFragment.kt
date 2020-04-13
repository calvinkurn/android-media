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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.DataEndlessScrollListener
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewSellerAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.ReviewSellerViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_rating_product.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RatingProductFragment: BaseListFragment<Visitable<*>, SellerReviewListTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ReviewSellerViewModel? = null

    private var linearLayoutManager: LinearLayoutManager? = null

    private val reviewSellerAdapter: ReviewSellerAdapter
        get() = adapter as ReviewSellerAdapter

    private val sellerReviewListTypeFactory by lazy {
        SellerReviewListTypeFactory()
    }

    private var sortBy: String? = ""
    private var filterBy: String? = ""


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
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
        observeLiveData()
    }

    override fun onDestroy() {
        viewModel?.reviewProductList?.removeObservers(this)
        viewModel?.productRatingOverall?.removeObservers(this)
        viewModel?.flush()
        super.onDestroy()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, SellerReviewListTypeFactory> {
        return ReviewSellerAdapter(sellerReviewListTypeFactory)
    }

    override fun getAdapterTypeFactory(): SellerReviewListTypeFactory = sellerReviewListTypeFactory

    override fun callInitialLoadAutomatically(): Boolean {
        return false
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String { return getString(R.string.title_review_rating_product) }

    override fun initInjector() {}

    override fun loadInitialData() {
        reviewSellerAdapter.clearAllElements()
        rvRatingProduct?.visible()
        globalError_reviewSeller?.hide()
        showLoading()
        adapter.addElement(ReviewSellerConstant.filterAndSortComposition)
        viewModel?.getProductRatingData(sortBy.toString(), filterBy.toString())
//        adapter.addElement(ReviewSellerConstant.summaryReviewProduct)
//        adapter.addElement(ReviewSellerConstant.listProductReview)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(linearLayoutManager, reviewSellerAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                reviewSellerAdapter.showLoading()
                loadNextPage(page)
            }
        }
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

    private fun onSuccessGetProductRatingOverallData(data: ProductRatingOverallUiModel) {
        reviewSellerAdapter.hideLoading()
        reviewSellerAdapter.setProductRatingOverallData(data)
    }

    private fun onErrorGetProductRatingOverallData(throwable: Throwable) {
        if(throwable is MessageErrorException) {
            globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
        } else {
            globalError_reviewSeller?.setType(GlobalError.NO_CONNECTION)
        }

        globalError_reviewSeller?.show()
        rvRatingProduct?.hide()

        globalError_reviewSeller.setOnClickListener {
            loadInitialData()
        }
    }

    private fun onSuccessGetReviewProductListData(hasNextPage: Boolean, reviewProductList: List<ProductReviewUiModel>) {
        reviewSellerAdapter.hideLoading()
        reviewSellerAdapter.setProductListReviewData(reviewProductList)
        updateScrollListenerState(hasNextPage)
    }

    fun loadNextPage(page: Int) {
        viewModel?.getProductRatingData(
                sortBy = sortBy.toString(),
                filterBy = filterBy.toString(),
                page = page
        )
    }
}
