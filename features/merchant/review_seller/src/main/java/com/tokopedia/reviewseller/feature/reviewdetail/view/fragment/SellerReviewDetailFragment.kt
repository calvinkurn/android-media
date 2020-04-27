package com.tokopedia.reviewseller.feature.reviewdetail.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.*
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.OverallRatingDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.ProductFeedbackDetailViewHolder
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.OverallRatingDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ProductFeedbackDetailUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_rating_product.*
import kotlinx.android.synthetic.main.fragment_seller_review_detail.*
import kotlinx.android.synthetic.main.item_overall_review_detail.view.*
import javax.inject.Inject

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailFragment : BaseListFragment<Visitable<*>, SellerReviewDetailAdapterTypeFactory>(),
        OverallRatingDetailViewHolder.OverallRatingDetailListener, ProductFeedbackDetailViewHolder.ProductFeedbackDetailListener {

    companion object {
        const val PRODUCT_ID = "EXTRA_SHOP_ID"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelProductReviewDetail: ProductReviewDetailViewModel? = null

    private var linearLayoutManager: LinearLayoutManager? = null

    private val reviewSellerDetailAdapter: SellerReviewDetailAdapter
        get() = adapter as SellerReviewDetailAdapter

    private var swipeToRefreshReviewDetail: SwipeToRefresh? = null

    private val sellerReviewDetailTypeFactory by lazy {
        SellerReviewDetailAdapterTypeFactory(this, this)
    }

    var productID: Int = 0
    var sortBy: String = ""
    var filterBy: String = "time=all"
    var chipsFilterText = ""
    var itemView: View? = null

    private var filterPeriodDetailUnify: ListUnify? = null
    private var optionFeedbackDetailUnify: ListUnify? = null
    private var optionMenuDetailUnify: ListUnify? = null

    private var bottomSheetPeriodDetail: BottomSheetUnify? = null
    private var bottomSheetOptionFeedback: BottomSheetUnify? = null
    private var bottomSheetMenuDetail: BottomSheetUnify? = null

    override fun getScreenName(): String = "SellerReviewDetail"

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                productID = getIntExtra(PRODUCT_ID, 0)
            }
        }
        super.onCreate(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModelProductReviewDetail = ViewModelProvider(this, viewModelFactory).get(ProductReviewDetailViewModel::class.java)
        viewModelProductReviewDetail?.filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(getString(R.string.default_filter_detail))
        viewModelProductReviewDetail?.filterAllText = viewModelProductReviewDetail?.filterPeriod.orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_review_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initRecyclerView(view)
        initSwipeToRefRefresh(view)
        initViewBottomSheet()
        initChipsView()
    }

    override fun initInjector() {
        getComponent(ReviewProductDetailComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): SellerReviewDetailAdapterTypeFactory = sellerReviewDetailTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun loadInitialData() {
        clearAllData()
        rvRatingDetail?.show()
        globalError_reviewDetail?.hide()
        emptyState_reviewDetail?.hide()
        showLoading()

        viewModelProductReviewDetail?.getProductRatingDetail(
                productID,
                sortBy,
                viewModelProductReviewDetail?.filterAllText.orEmpty())
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvRatingDetail)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, SellerReviewDetailAdapterTypeFactory> {
        return SellerReviewDetailAdapter(sellerReviewDetailTypeFactory)
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(linearLayoutManager, reviewSellerDetailAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                reviewSellerDetailAdapter.showLoading()
                loadNextPage(page)
            }
        }
    }

    override fun onDestroy() {
        viewModelProductReviewDetail?.productFeedbackDetail?.removeObservers(this)
        viewModelProductReviewDetail?.reviewDetailOverallRating?.removeObservers(this)
        viewModelProductReviewDetail?.flush()
        super.onDestroy()
    }

    private fun initRecyclerView(view: View) {
        getRecyclerView(view).let {
            it.clearOnScrollListeners()
            it.layoutManager = linearLayoutManager
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
            it.addOnScrollListener(endlessRecyclerViewScrollListener)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option_review_product_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_product_detail -> {
                clickOptionFeedbackDetail()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(review_detail_toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    private fun clickOptionFeedbackDetail() {
        val optionMenuList = context?.let { SellerReviewProductDetailMapper.mapToItemUnifyMenuOption(it) }
        optionMenuList?.let { optionFeedbackDetailUnify?.setData(it) }

        bottomSheetOptionFeedback?.apply {
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        optionFeedbackDetailUnify?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        1 -> {

                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetOptionFeedback?.show(it, getString(R.string.change_product_label))
        }
    }

    fun loadNextPage(page: Int) {
        viewModelProductReviewDetail?.getFeedbackDetailListNext(
                productID = productID,
                sortBy = sortBy,
                filterBy = filterBy,
                page = page)
    }

    private fun initSwipeToRefRefresh(view: View) {
        swipeToRefreshReviewDetail = view.findViewById(R.id.swipeToRefreshLayoutDetail)

        swipeToRefreshReviewDetail?.setOnRefreshListener {
            swipeToRefreshReviewDetail?.isRefreshing = true
            loadInitialData()
        }

        observeLiveData()
    }

    private fun observeLiveData() {
        viewModelProductReviewDetail?.reviewDetailOverallRating?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetProductReviewDetailOverallData(it.data)
                }
                is Fail -> {
                    onErrorGetReviewDetailData(it.throwable)
                }
            }
        })

        viewModelProductReviewDetail?.productFeedbackDetail?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetFeedbackReviewListData(it.data.first, it.data.second)
                }
                is Fail -> {
                    onErrorGetReviewDetailData(it.throwable)
                }
            }
        })
    }

    private fun onSuccessGetProductReviewDetailOverallData(data: OverallRatingDetailUiModel) {
        reviewSellerDetailAdapter.hideLoading()
        swipeToRefreshReviewDetail?.isRefreshing = false
        review_detail_toolbar.apply {
            title = data.productName.toString()
        }

        reviewSellerDetailAdapter.setOverallRatingDetailData(data.apply {
            chipFilter = chipsFilterText
        })
    }

    private fun onSuccessGetFeedbackReviewListData(hasNextPage: Boolean, reviewProductDetail: ProductFeedbackDetailUiModel) {
        reviewSellerDetailAdapter.hideLoading()
        swipeToRefreshReviewDetail?.isRefreshing = false
        if (reviewSellerDetailAdapter.itemCount == 1) {
            if (reviewProductDetail.productFeedbackDetailList.isEmpty()) {
                reviewSellerDetailAdapter.apply {
                    setRatingBarDetailData(reviewProductDetail.ratingBarList)
                    setTopicDetailData(reviewProductDetail.topicList)
                }
                emptyState_reviewDetail?.show()
            } else {
                reviewSellerDetailAdapter.apply {
                    setRatingBarDetailData(reviewProductDetail.ratingBarList)
                    setTopicDetailData(reviewProductDetail.topicList)
                    setFeedbackListData(reviewProductDetail.productFeedbackDetailList)
                }
            }
        } else {
            reviewSellerDetailAdapter.setFeedbackListData(reviewProductDetail.productFeedbackDetailList)
        }
        updateScrollListenerState(hasNextPage)
    }

    private fun onErrorGetReviewDetailData(throwable: Throwable) {
        swipeToRefreshReviewDetail?.isRefreshing = false
        if (reviewSellerDetailAdapter.itemCount == 0) {
            if (throwable.message?.isNotEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
            } else if (throwable.message?.isEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.NO_CONNECTION)
            }
            emptyState_reviewDetail?.hide()
            rvRatingDetail?.hide()
            globalError_reviewDetail?.show()

            globalError_reviewDetail?.setActionClickListener {
                loadInitialData()
            }
        } else {
            onErrorLoadMoreToaster(getString(R.string.error_message_load_more_review_product), getString(R.string.action_retry_toaster_review_product))
        }
    }

    private fun onErrorLoadMoreToaster(message: String, action: String) {
        view?.let {
            Toaster.make(it, message, actionText = action, type = Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
                loadInitialData()
            })
        }
    }

    override fun onFilterPeriodClicked(view: View, title: String) {
        val filterDetailList: Array<String> = resources.getStringArray(R.array.filter_review_detail_array)
        val filterDetailItemUnify = SellerReviewProductListMapper.mapToItemUnifyList(filterDetailList)
        filterPeriodDetailUnify?.setData(filterDetailItemUnify)
        initBottomSheetFilterPeriod(view, title, filterDetailItemUnify)
    }

    private fun initBottomSheetFilterPeriod(view: View, title: String, filterPeriodItemUnify: ArrayList<ListItemUnify>) {
        bottomSheetPeriodDetail?.apply {
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        filterPeriodDetailUnify?.let { it ->
            it.onLoadFinish {
                it.setSelectedFilterOrSort(filterPeriodItemUnify, viewModelProductReviewDetail?.positionFilterPeriod.orZero())

                it.setOnItemClickListener { _, _, position, _ ->
                    onItemFilterClickedBottomSheet(view, position, filterPeriodItemUnify, it)
                }

                filterPeriodItemUnify.forEachIndexed { position, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemFilterClickedBottomSheet(view, position, filterPeriodItemUnify, it)
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetPeriodDetail?.show(it, title)
        }
    }

    private fun onItemFilterClickedBottomSheet(view: View, position: Int, filterListItemUnify: ArrayList<ListItemUnify>,
                                               filterListUnify: ListUnify) {
        try {
            itemView = view
            viewModelProductReviewDetail?.positionFilterPeriod = position
            chipsFilterText = filterListItemUnify[position].listTitleText
            itemView?.review_period_filter_button_detail?.chip_text?.text = chipsFilterText
            filterListUnify.setSelectedFilterOrSort(filterListItemUnify, position)
            viewModelProductReviewDetail?.filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(chipsFilterText)
            viewModelProductReviewDetail?.filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(viewModelProductReviewDetail?.filterPeriod.orEmpty())
            loadInitialData()
            bottomSheetPeriodDetail?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initViewBottomSheet() {
        val view = View.inflate(context, R.layout.bottom_sheet_period_filter_detail, null)
        bottomSheetPeriodDetail = BottomSheetUnify()
        filterPeriodDetailUnify = view.findViewById(R.id.listFilterReviewDetail)
        bottomSheetPeriodDetail?.setChild(view)

        val viewOption = View.inflate(context, R.layout.bottom_sheet_option_feedback, null)
        bottomSheetOptionFeedback = BottomSheetUnify()
        optionFeedbackDetailUnify = viewOption.findViewById(R.id.optionFeedbackList)
        bottomSheetOptionFeedback?.setChild(viewOption)

        val viewMenu = View.inflate(context, R.layout.bottom_sheet_menu_option_product_detail, null)
        bottomSheetMenuDetail = BottomSheetUnify()
        optionMenuDetailUnify = viewMenu.findViewById(R.id.optionMenuDetail)
        bottomSheetMenuDetail?.setChild(viewMenu)
    }

    private fun initChipsView() {
        chipsFilterText = getString(R.string.default_filter_detail)
    }

    override fun onOptionFeedbackClicked(view: View, title: String, optionDetailListItemUnify: ArrayList<ListItemUnify>, isEmptyReply: Boolean) {
        optionFeedbackDetailUnify?.setData(optionDetailListItemUnify)

        bottomSheetOptionFeedback?.apply {
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        optionFeedbackDetailUnify?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        1 -> {
                            if (!isEmptyReply) {

                            }
                        }
                        2 -> {

                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetOptionFeedback?.show(it, title)
        }
    }

}