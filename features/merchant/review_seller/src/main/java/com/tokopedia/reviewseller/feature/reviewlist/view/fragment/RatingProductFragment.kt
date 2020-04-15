package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerConstant
import com.tokopedia.reviewseller.feature.reviewlist.di.component.ReviewProductListComponent
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.ReviewSellerMapper
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.DataEndlessScrollListener
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewSellerAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.SearchRatingProductUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.FilterAndSortViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.ReviewSellerViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_rating_product.*
import kotlinx.android.synthetic.main.bottom_sheet_rating_product.view.*
import kotlinx.android.synthetic.main.fragment_rating_product.*
import kotlinx.android.synthetic.main.item_quick_reply.view.*
import kotlinx.android.synthetic.main.item_review_list_filter_and_sort.view.*
import kotlinx.android.synthetic.main.item_search_rating_product.*
import kotlinx.android.synthetic.main.widget_label_view_radio_button.view.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RatingProductFragment : BaseListFragment<Visitable<*>, SellerReviewListTypeFactory>(), FilterAndSortViewHolder.FilterAndSortListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ReviewSellerViewModel? = null

    private var linearLayoutManager: LinearLayoutManager? = null

    private val reviewSellerAdapter: ReviewSellerAdapter
        get() = adapter as ReviewSellerAdapter

    private val sellerReviewListTypeFactory by lazy {
        SellerReviewListTypeFactory(this)
    }

    private var sortBy: String? = ""
    private var filterBy: String? = ""

    private var chipsSort: String? = ""
    private var chipsFilter: String? = ""

    private var swipeToRefreshReviewSeller: SwipeToRefresh? = null

    private var bottomSheet: BottomSheetUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReviewSellerViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        sortBy = ReviewSellerConstant.mapSortReviewProduct.filterValues { it == getString(R.string.most_review) }.keys.firstOrNull().orEmpty()
        filterBy = ReviewSellerConstant.mapRatingReviewProduct.filterValues { it == getString(R.string.last_week) }.keys.firstOrNull().orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
        initSwipeToRefRefresh(view)
        initSearchBar()
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

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String {
        return getString(R.string.title_review_rating_product)
    }

    override fun initInjector() {
        getComponent(ReviewProductListComponent::class.java).inject(this)
    }

    override fun loadInitialData() {
        reviewSellerAdapter.clearAllElements()
        rvRatingProduct?.visible()
        globalError_reviewSeller?.hide()
        emptyState_reviewProduct?.hide()
        showLoading()
        viewModel?.getProductRatingData(sortBy.toString(), filterBy.toString())
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

    private fun initRecyclerView(view: View) {
        getRecyclerView(view).let {
            it.clearOnScrollListeners()
            it.layoutManager = linearLayoutManager
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
    }

    private fun initSwipeToRefRefresh(view: View) {
        swipeToRefreshReviewSeller = view.findViewById(R.id.swipeToRefreshLayout)

        swipeToRefreshReviewSeller?.setOnRefreshListener {
            swipeToRefreshReviewSeller?.isRefreshing = true
            loadInitialData()
        }

        observeLiveData()
    }

    private fun initSearchBar() {
        searchBarRatingProduct?.searchBarTextField?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchBarRatingProduct?.searchBarTextField?.text.toString()
                if(query.isNotEmpty()) { }
            }
            false
        }

    }

    private fun addElementSearchFilter() {
        chipsFilter = getString(R.string.last_week)
        chipsSort = getString(R.string.most_review)
        adapter.addElement(SearchRatingProductUiModel())
        adapter.addElement(FilterAndSortModel(chipsFilter, chipsSort))
    }

    private fun observeLiveData() {
        viewModel?.productRatingOverall?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetProductRatingOverallData(it.data)
                }
                is Fail -> {
                    onErrorGetReviewSellerData(it.throwable)
                }
            }
        })

        viewModel?.reviewProductList?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetReviewProductListData(it.data.first, it.data.second)
                }
                is Fail -> {
                    onErrorGetReviewSellerData(it.throwable)
                }
            }
        })
    }

    private fun onSuccessGetProductRatingOverallData(data: ProductRatingOverallUiModel) {
        reviewSellerAdapter.hideLoading()
        addElementSearchFilter()
        swipeToRefreshReviewSeller?.isRefreshing = false
        reviewSellerAdapter.setProductRatingOverallData(data)
    }

    private fun onErrorGetReviewSellerData(throwable: Throwable) {
        swipeToRefreshReviewSeller?.isRefreshing = false
        if(reviewSellerAdapter.endlessDataSize == 0) {
            if (throwable is Exception) {
                globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
            } else {
                globalError_reviewSeller?.setType(GlobalError.NO_CONNECTION)
            }
        } else {
            onErrorLoadMoreToaster(getString(R.string.error_message_load_more_review_product), getString(R.string.action_retry_toaster_review_product))
        }

        globalError_reviewSeller?.show()
        emptyState_reviewProduct?.hide()
        rvRatingProduct?.hide()

        globalError_reviewSeller.setActionClickListener {
            loadInitialData()
        }
    }

    private fun onSuccessGetReviewProductListData(hasNextPage: Boolean, reviewProductList: List<ProductReviewUiModel>) {
        reviewSellerAdapter.hideLoading()
        swipeToRefreshReviewSeller?.isRefreshing = false
        if (reviewProductList.isEmpty()) {
            emptyState_reviewProduct?.show()
        } else {
            reviewSellerAdapter.setProductListReviewData(reviewProductList)
        }
        updateScrollListenerState(hasNextPage)
    }

    fun loadNextPage(page: Int) {
        viewModel?.getNextProductReviewList(
                sortBy = sortBy.toString(),
                filterBy = filterBy.toString(),
                page = page
        )

    }

    override fun onFilterActionClicked(view: View, filter: String) {
        val list: Array<String> = resources.getStringArray(R.array.filter_review_product_array)
        initBottomSheet(list, getString(R.string.title_bottom_sheet_filter), view, true)
    }

    override fun onSortActionClicked(view: View, sort: String) {
        val list: Array<String> = resources.getStringArray(R.array.sort_review_product_array)
        initBottomSheet(list, getString(R.string.title_bottom_sheet_sort), view, false)
    }

    private fun initBottomSheet(list: Array<String>, title: String, itemView: View, isFilter: Boolean) {
        val view = View.inflate(context, R.layout.bottom_sheet_rating_product, null)
        view.listRatingProduct.setData(ReviewSellerMapper.mapToItemUnifyList(list))

        bottomSheet?.apply {
            setChild(view)
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        view.listRatingProduct?.onLoadFinish {

            (listRatingProduct?.adapter?.getItem(0) as? ListItemUnify)?.let {
                it.listRightRadiobtn?.isChecked = true
            }



            listRatingProduct?.radio_button?.setOnClickListener { radioButton ->
                if(isFilter) {
                    itemView.review_period_filter_button.chip_text.text = radioButton.text.toString()
                } else {
                    itemView.review_sort_button.chip_text.text = it.text.toString()
                }
            }
        }

        fragmentManager?.let { bottomSheet?.show(it, title) }
    }

    private fun onErrorLoadMoreToaster(message: String, action: String) {
        view?.let { Toaster.make(it, message, actionText = action, type = Toaster.TYPE_ERROR) }
    }
}
