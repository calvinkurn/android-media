package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.di.component.DaggerReviewSellerComponent
import com.tokopedia.reviewseller.common.di.module.ReviewSellerModule
import com.tokopedia.reviewseller.common.util.*
import com.tokopedia.reviewseller.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.reviewseller.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.reviewseller.feature.reviewlist.analytics.ProductReviewTracking
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewSellerAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.SellerReviewListViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_rating_product.*
import kotlinx.android.synthetic.main.item_empty_state_list_rating_product.*
import kotlinx.android.synthetic.main.item_search_rating_product.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RatingProductFragment : BaseListFragment<Visitable<*>, SellerReviewListTypeFactory>(),
        ReviewSummaryViewHolder.ReviewSummaryViewListener,
        SellerReviewListViewHolder.SellerReviewListListener {

    companion object {
        private const val TAG_COACH_MARK_RATING_PRODUCT = "coachMarkRatingProduct"
        private const val searchQuery = "search"
        private const val MAX_LENGTH_SEARCH = 3

        fun createInstance(): RatingProductFragment {
            return RatingProductFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelListReviewList: SellerReviewListViewModel? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracking: ProductReviewTracking

    private var linearLayoutManager: LinearLayoutManager? = null

    private val reviewSellerAdapter: ReviewSellerAdapter
        get() = adapter as ReviewSellerAdapter

    private val sellerReviewListTypeFactory by lazy {
        SellerReviewListTypeFactory(this, this)
    }

    private val prefKey = this.javaClass.name + ".pref"

    private var prefs: SharedPreferences? = null

    private var swipeToRefreshReviewSeller: SwipeToRefresh? = null

    private val coachMarkItems: ArrayList<CoachMarkItem> = arrayListOf()

    private var chipsSort: ChipsUnify? = null
    private var chipsFilter: ChipsUnify? = null

    private var sortListUnify: ListUnify? = null
    private var filterListUnify: ListUnify? = null

    private var bottomSheetFilter: BottomSheetUnify? = null
    private var bottomSheetSort: BottomSheetUnify? = null

    private var firstTabItem: View? = null

    private var itemViewSummary: View? = null

    private var productItemList: List<ProductReviewUiModel>? = null

    var chipsSortText: String? = ""
    var chipsFilterText: String? = ""
    var searchFilterText: String? = ""
    var isEmptyFilter = false

    var sortBy: String? = ""
    var filterBy: String? = ""

    var filterAllText: String? = ""

    var positionFilter = 0
    var positionSort = 0

    var isCompletedCoachMark = false

    private var coachMarkSummary: CoachMarkItem? = null
    private var coachMarkItemRatingProduct: CoachMarkItem? = null

    private val coachMark: CoachMark by lazy {
        initCoachMark()
    }

    private val coachMarkFilterAndSort: CoachMarkItem by lazy {
        CoachMarkItem(view?.findViewById(R.id.filter_and_sort_layout),
                getString(R.string.label_filter_and_sort),
                getString(R.string.desc_filter_and_sort))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracking.sendScreen(userSession.shopId.orEmpty())
        tracking.eventClickTabRatingProduct(userSession.shopId.orEmpty())
        viewModelListReviewList = ViewModelProvider(this, viewModelFactory).get(SellerReviewListViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        prefs = context?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        sortBy = ReviewSellerConstant.mapSortReviewProduct().getKeyByValue(getString(R.string.most_review))
        filterBy = ReviewSellerConstant.mapFilterReviewProduct().getKeyByValue(getString(R.string.last_week))
        filterAllText = filterBy
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(Color.WHITE)
        initRecyclerView(view)
        initSwipeToRefRefresh(view)
        initSearchBar()
        initViewBottomSheet()
        initChipsSort(view)
        initChipsFilter(view)
    }

    override fun onDestroy() {
        viewModelListReviewList?.reviewProductList?.removeObservers(this)
        viewModelListReviewList?.productRatingOverall?.removeObservers(this)
        viewModelListReviewList?.flush()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ReviewSellerConstant.RESULT_INTENT_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    val stringData = data?.getStringExtra(SellerReviewDetailFragment.SELECTED_DATE_CHIP)
                    val updatedPosition = data?.getIntExtra(SellerReviewDetailFragment.SELECTED_DATE_POSITION, 0)
                            ?: 0

                    if (chipsFilterText == stringData) return

                    positionFilter = updatedPosition
                    val filterListItemUnify = populateFilterDate()
                    filterListUnify?.apply {
                        setSelectedFilterOrSort(filterListItemUnify, positionFilter.orZero())
                        onItemFilterClickedBottomSheet(updatedPosition, filterListItemUnify, this)
                    }
                }
            }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
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
        inject()
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        endlessRecyclerViewScrollListener?.resetState()
        reviewSellerAdapter.clearAllElements()
        rvRatingProduct?.show()
        filter_and_sort_layout?.hide()
        search_bar_layout?.hide()
        globalError_reviewSeller?.hide()
        emptyState_reviewProduct?.hide()
        showLoading()
        viewModelListReviewList?.getProductRatingData(sortBy.orEmpty(), filterAllText.orEmpty())
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(linearLayoutManager, reviewSellerAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                reviewSellerAdapter.showLoading()
                loadNextPage(page)
            }
            override fun isDataEmpty(): Boolean {
                return reviewSellerAdapter.list.isEmpty()
            }
        }
    }

    private fun inject() {
        if (activity != null) {
            val appComponent = (activity?.application as? BaseMainApplication)?.baseAppComponent
            DaggerReviewSellerComponent.builder()
                    .reviewSellerModule(ReviewSellerModule())
                    .baseAppComponent(appComponent)
                    .build()
                    .inject(this)
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
            endlessRecyclerViewScrollListener = createEndlessRecyclerViewListener()
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
        searchBarRatingProduct?.apply {
            isClearable = true
            iconListener = {
                if (searchBarPlaceholder.isNotEmpty()) {
                    onSearchKeywordEmpty()
                }
            }
            searchBarTextField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) tracking.eventClickSearchBar(userSession.shopId.orEmpty())
            }
            searchBarTextField.afterTextChanged {
                if (it.isEmpty()) {
                    onSearchKeywordEmpty()
                }
            }
            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchBarRatingProduct?.searchBarTextField?.text.toString()

                    tracking.eventSubmitSearchBar(userSession.shopId.orEmpty(), query)

                    if (query.length < MAX_LENGTH_SEARCH) {
                        showEmptyState()
                        tvContentNoReviewsYet?.text = getString(R.string.empty_state_message_wrong_keyword)
                    } else {
                        searchFilterText = "$searchQuery=$query"
                        filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(filterBy.orEmpty(), searchFilterText.orEmpty())
                        searchBarPlaceholder = query
                        loadInitialData()
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }
    }

    private fun onSearchKeywordEmpty() {
        searchFilterText = "$searchQuery="
        filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(filterBy.orEmpty(), searchFilterText.orEmpty())
        searchBarRatingProduct.searchBarTextField.text.clear()
        searchBarRatingProduct.searchBarPlaceholder = getString(R.string.product_search)
        loadInitialData()
    }

    private fun observeLiveData() {
        viewModelListReviewList?.productRatingOverall?.observe(this, Observer {
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

        viewModelListReviewList?.reviewProductList?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    productItemList = it.data.second
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
        filter_and_sort_layout?.show()
        search_bar_layout?.show()
        swipeToRefreshReviewSeller?.isRefreshing = false
        reviewSellerAdapter.setProductRatingOverallData(data)
    }

    private fun onErrorGetReviewSellerData(throwable: Throwable) {
        tracking.eventViewErrorIris(throwable.message.orEmpty())
        swipeToRefreshReviewSeller?.isRefreshing = false
        if (reviewSellerAdapter.itemCount.isZero()) {
            if (throwable.message?.isNotEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
            } else if (throwable.message?.isEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.NO_CONNECTION)
            }

            showErrorState()

            globalError_reviewSeller.setActionClickListener {
                tracking.eventClickRetryError(userSession.shopId.orEmpty(), throwable.message.orEmpty())
                loadInitialData()
            }
        } else {
            onErrorLoadMoreToaster(getString(R.string.error_message_load_more_review_product), getString(R.string.action_retry_toaster_review_product))
        }
    }

    private fun showErrorState() {
        filter_and_sort_layout?.gone()
        rvRatingProduct?.gone()
        emptyState_reviewProduct?.gone()
        search_bar_layout?.show()
        scrollView_globalError_reviewSeller?.show()
        globalError_reviewSeller?.show()
    }

    private fun onSuccessGetReviewProductListData(hasNextPage: Boolean, reviewProductList: List<ProductReviewUiModel>) {
        reviewSellerAdapter.hideLoading()
        swipeToRefreshReviewSeller?.isRefreshing = false
        if ((reviewProductList.isEmpty() && reviewSellerAdapter.itemCount.isZero()) && isEmptyFilter) {
            showEmptyState()
            tvContentNoReviewsYet?.text = getString(R.string.empty_state_message_wrong_filter)
            isEmptyFilter = false
        } else if ((reviewProductList.isEmpty() && reviewSellerAdapter.itemCount.isZero()) && !isEmptyFilter) {
            showEmptyState()
            tvContentNoReviewsYet?.text = getString(R.string.content_no_reviews_yet)
        } else {
            reviewSellerAdapter.setProductListReviewData(reviewProductList)
            updateScrollListenerState(hasNextPage)
        }
    }

    private fun showEmptyState() {
        rvRatingProduct?.hide()
        scrollView_emptyState_reviewSeller?.show()
        emptyState_reviewProduct?.show()
    }

    fun loadNextPage(page: Int) {
        tracking.eventScrollRatingProduct(userSession.shopId.orEmpty())
        viewModelListReviewList?.getNextProductReviewList(
                sortBy = sortBy.orEmpty(),
                filterBy = filterAllText.orEmpty(),
                page = page
        )
    }

    private fun coachMarkFilterAndSort() {
        prefs?.let {
            if (!it.getBoolean(ReviewSellerConstant.HAS_FILTER_AND_SORT, false)) {
                coachMarkItems.add(coachMarkFilterAndSort)
                it.edit().putBoolean(ReviewSellerConstant.HAS_FILTER_AND_SORT, true).apply()
            }
        }
    }

    private fun coachMarkSummary() {
        prefs?.let {
            coachMarkSummary?.also { coachMark ->
                if (!it.getBoolean(ReviewSellerConstant.HAS_OVERALL_RATING_PRODUCT, false)) {
                    coachMarkItems.add(coachMark)
                    it.edit().putBoolean(ReviewSellerConstant.HAS_OVERALL_RATING_PRODUCT, true).apply()
                }
            }
        }
    }

    private fun coachMarkItemRatingProduct() {
        prefs?.let {
            if (!it.getBoolean(ReviewSellerConstant.HAS_TAB_RATING_PRODUCT, false)) {
                coachMarkItemRatingProduct?.let { it1 -> coachMarkItems.add(it1) }
                it.edit().putBoolean(ReviewSellerConstant.HAS_TAB_RATING_PRODUCT, true).apply()
            }
        }
    }

    private fun initCoachMark(): CoachMark {
        val coachMark = CoachMarkBuilder().build()

        coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
            override fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean {
                val countCoachMarkItem = coachMarkItems.size - 1
                coachMark.enableSkip = (nextStep < countCoachMarkItem)
                return false
            }
        })

        coachMark.onFinishListener = {
            isCompletedCoachMark = true
        }

        return coachMark
    }


    override fun onAddedCoachMarkOverallRating(view: View) {
        itemViewSummary = view
        coachMarkSummary = CoachMarkItem(itemViewSummary?.findViewById(R.id.cardSummary),
                getString(R.string.average_rating_title),
                getString(R.string.average_rating_desc))

        if (productItemList?.isEmpty() == true) {
            coachMarkFilterAndSort()
            coachMarkSummary()
            showCoachMark()
        }
    }

    override fun onAddedCoachMarkItemProduct(view: View) {

        coachMarkItemRatingProduct = CoachMarkItem(view.findViewById(R.id.itemRatingProduct),
                getString(R.string.full_summary_of_product_ratings),
                getString(R.string.desc_full_summary_of_product_ratings))

        if (productItemList?.isNotEmpty() == true) {
            coachMarkItemRatingProduct()
            coachMarkFilterAndSort()
            coachMarkSummary()
            showCoachMark()
        }
    }

    private fun showCoachMark() {
        if (!isCompletedCoachMark) {
            coachMark.show(activity, TAG_COACH_MARK_RATING_PRODUCT, coachMarkItems)
        }
    }

    private fun initChipsSort(view: View) {
        chipsSort = view.findViewById(R.id.review_sort_chips)
        chipsSortText = getString(R.string.most_review)
        chipsSort?.apply {
            chip_text.ellipsize = TextUtils.TruncateAt.END
            centerText = true
            chip_text.text = chipsSortText
        }

        val sortList: Array<String> = resources.getStringArray(R.array.sort_review_product_array)

        val sortListItemUnify = SellerReviewProductListMapper.mapToItemUnifyList(sortList)

        sortListUnify?.setData(sortListItemUnify)

        chipsSort?.apply {
            setOnClickListener {
                chipsSort?.toggle()
                initBottomSheetSort(sortListItemUnify, getString(R.string.title_bottom_sheet_sort))
            }
            setChevronClickListener {
                chipsSort?.toggle()
                initBottomSheetSort(sortListItemUnify, getString(R.string.title_bottom_sheet_sort))
            }
        }
    }

    private fun initChipsFilter(view: View) {
        chipsFilter = view.findViewById(R.id.review_period_filter_chips)
        chipsFilterText = getString(R.string.last_week)
        chipsFilter?.apply {
            chip_text.ellipsize = TextUtils.TruncateAt.END
            centerText = true
            chip_text.text = chipsFilterText
        }

        val filterListItemUnify = populateFilterDate()

        chipsFilter?.apply {
            setOnClickListener {
                chipsFilter?.toggle()
                initBottomSheetFilter(filterListItemUnify, getString(R.string.title_bottom_sheet_filter))
            }
            setChevronClickListener {
                chipsFilter?.toggle()
                initBottomSheetFilter(filterListItemUnify, getString(R.string.title_bottom_sheet_filter))
            }
        }
    }

    private fun populateFilterDate(): ArrayList<ListItemUnify> {
        val filterList: Array<String> = resources.getStringArray(R.array.filter_review_product_array)
        val filterListItemUnify = SellerReviewProductListMapper.mapToItemUnifyList(filterList)

        filterListUnify?.setData(filterListItemUnify)
        return filterListItemUnify
    }

    private fun initViewBottomSheet() {
        val view = View.inflate(context, R.layout.bottom_sheet_sort_rating_product, null)
        bottomSheetSort = BottomSheetUnify()
        sortListUnify = view.findViewById(R.id.listSortRatingProduct)

        val viewFilter = View.inflate(context, R.layout.bottom_sheet_filter_rating_product, null)
        bottomSheetFilter = BottomSheetUnify()
        filterListUnify = viewFilter.findViewById(R.id.listFilterRatingProduct)

        bottomSheetSort?.setChild(view)
        bottomSheetFilter?.setChild(viewFilter)
    }

    private fun onErrorLoadMoreToaster(message: String, action: String) {
        view?.let {
            Toaster.make(it, message, actionText = action, type = Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
                loadInitialData()
            })
        }
    }

    override fun onItemProductReviewClicked(productId: Int, position: Int, imageUrl: String) {
        tracking.eventClickItemRatingProduct(
                shopId = userSession.shopId.orEmpty(),
                productId = productId.toString(),
                productPosition = position.toString())
        startActivityForResult(Intent(context, SellerReviewDetailActivity::class.java).apply {
            putExtra(SellerReviewDetailFragment.PRODUCT_ID, productId)
            putExtra(SellerReviewDetailFragment.CHIP_FILTER, chipsFilterText)
            putExtra(SellerReviewDetailFragment.PRODUCT_IMAGE, imageUrl)
        }, ReviewSellerConstant.RESULT_INTENT_DETAIL)
    }

    private fun initBottomSheetFilter(filterListItemUnify: ArrayList<ListItemUnify>, title: String) {
        tracking.eventClickFilterRatingProduct(userSession.shopId.orEmpty())

        bottomSheetFilter?.apply {
            setOnDismissListener {
                chipsFilter?.toggle()
            }
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        filterListUnify?.let { it ->
            it.onLoadFinish {
                it.setSelectedFilterOrSort(filterListItemUnify, positionFilter.orZero())
                it.setOnItemClickListener { _, _, position, _ ->
                    onItemFilterClickedBottomSheet(position, filterListItemUnify, it)
                }

                filterListItemUnify.forEachIndexed { position, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemFilterClickedBottomSheet(position, filterListItemUnify, it)
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetFilter?.show(it, title)
        }
    }

    private fun initBottomSheetSort(sortListItemUnify: ArrayList<ListItemUnify>, title: String) {
        tracking.eventClickSortRatingProduct(userSession.shopId.orEmpty())

        bottomSheetSort?.apply {
            setOnDismissListener {
                chipsSort?.toggle()
            }
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        sortListUnify?.let { it ->
            it.onLoadFinish {
                it.setSelectedFilterOrSort(sortListItemUnify, positionSort.orZero())
                it.setOnItemClickListener { _, _, position, _ ->
                    onItemSortClickedBottomSheet(position, sortListItemUnify, it)
                }

                sortListItemUnify.forEachIndexed { position, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemSortClickedBottomSheet(position, sortListItemUnify, it)
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetSort?.show(it, title)
        }
    }

    private fun onItemFilterClickedBottomSheet(position: Int, filterListItemUnify: ArrayList<ListItemUnify>,
                                               filterListUnify: ListUnify) {
        try {
            isEmptyFilter = true
            positionFilter = position
            chipsFilterText = filterListItemUnify[position].listTitleText
            tracking.eventClickFilterBottomSheet(userSession.shopId.orEmpty(), chipsFilterText.orEmpty())
            reviewSellerAdapter.updateDatePeriod(ReviewSellerConstant.mapFilterReviewProduct().getKeyByValue(chipsFilterText))
            chipsFilter?.chip_text?.text = chipsFilterText
            filterListUnify.setSelectedFilterOrSort(filterListItemUnify, position)
            filterBy = ReviewSellerConstant.mapFilterReviewProduct().getKeyByValue(chipsFilterText)
            filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(filterBy.orEmpty(), searchFilterText.orEmpty())
            loadInitialData()
            bottomSheetFilter?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onItemSortClickedBottomSheet(position: Int, sortListItemUnify: ArrayList<ListItemUnify>, sortListUnify: ListUnify) {
        try {
            isEmptyFilter = true
            positionSort = position
            chipsSortText = sortListItemUnify[position].listTitleText
            tracking.eventClickSortBottomSheet(userSession.shopId.orEmpty(), chipsSortText.orEmpty())
            chipsSort?.chip_text?.text = chipsSortText
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            sortBy = ReviewSellerConstant.mapSortReviewProduct().getKeyByValue(chipsSortText)
            loadInitialData()
            bottomSheetSort?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
