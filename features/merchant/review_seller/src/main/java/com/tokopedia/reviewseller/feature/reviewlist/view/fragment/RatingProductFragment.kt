package com.tokopedia.reviewseller.feature.reviewlist.view.fragment

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
import com.tokopedia.reviewseller.common.util.*
import com.tokopedia.reviewseller.feature.reviewdetail.view.activity.SellerReviewDetailActivity
import com.tokopedia.reviewseller.feature.reviewdetail.view.fragment.SellerReviewDetailFragment
import com.tokopedia.reviewseller.feature.reviewlist.di.component.ReviewProductListComponent
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.ReviewSellerAdapter
import com.tokopedia.reviewseller.feature.reviewlist.view.adapter.SellerReviewListTypeFactory
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductRatingOverallUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.model.ProductReviewUiModel
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.ReviewSummaryViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewholder.SellerReviewListViewHolder
import com.tokopedia.reviewseller.feature.reviewlist.view.viewmodel.SellerReviewListViewModel
import com.tokopedia.unifycomponents.*
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_rating_product.*
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
        val chipsPaddingRight = 8.toPx()
        val maxChipTextWidth = 116.toPx()
        private const val searchQuery = "search"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelListReviewList: SellerReviewListViewModel? = null

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

    private var tabReview: ViewGroup? = null
    private var firstTabItem: View? = null

    var chipsSortText: String? = ""
    var chipsFilterText: String? = ""
    var searchFilterText: String? = ""

    private val coachMark: CoachMark by lazy {
        initCoachMark()
    }

    private val coachMarkTabRatingProduct: CoachMarkItem by lazy {

        tabReview = (view?.rootView?.findViewById<TabsUnify>(R.id.tab_review)?.getUnifyTabLayout()
                ?.getChildAt(0) as ViewGroup)

        firstTabItem = tabReview?.getChildAt(0)

        CoachMarkItem(firstTabItem,
                getString(R.string.full_summary_of_product_ratings),
                getString(R.string.desc_full_summary_of_product_ratings))
    }

    private val coachMarkFilterAndSort: CoachMarkItem by lazy {
        CoachMarkItem(view?.findViewById(R.id.filter_and_sort_layout),
                getString(R.string.label_filter_and_sort),
                getString(R.string.desc_filter_and_sort))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelListReviewList = ViewModelProvider(this, viewModelFactory).get(SellerReviewListViewModel::class.java)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        prefs = context?.getSharedPreferences(prefKey, Context.MODE_PRIVATE)
        viewModelListReviewList?.sortBy = ReviewSellerConstant.mapSortReviewProduct().getKeyByValue(getString(R.string.most_review))
        viewModelListReviewList?.filterBy = ReviewSellerConstant.mapFilterReviewProduct().getKeyByValue(getString(R.string.last_week))
        viewModelListReviewList?.filterAllText = viewModelListReviewList?.filterBy
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
        clearAllData()
        rvRatingProduct?.visible()
        filter_and_sort_layout?.hide()
        search_bar_layout?.hide()
        globalError_reviewSeller?.hide()
        emptyState_reviewProduct?.hide()
        showLoading()
        viewModelListReviewList?.getProductRatingData(viewModelListReviewList?.sortBy.orEmpty(), viewModelListReviewList?.filterAllText.orEmpty())
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
            searchBarIcon.setOnClickListener {
                if (searchBarPlaceholder.isNotEmpty()) {
                    searchFilterText = "$searchQuery="
                    viewModelListReviewList?.filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(viewModelListReviewList?.filterBy.orEmpty(), searchFilterText.orEmpty())
                    searchBarTextField.text.clear()
                    searchBarPlaceholder = getString(R.string.product_search)
                }
            }
            searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchBarRatingProduct?.searchBarTextField?.text.toString()
                    searchFilterText = "$searchQuery=$query"
                    viewModelListReviewList?.filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(viewModelListReviewList?.filterBy.orEmpty(), searchFilterText.orEmpty())
                    searchBarPlaceholder = query
                    loadInitialData()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
        }

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
        swipeToRefreshReviewSeller?.isRefreshing = false
        if (reviewSellerAdapter.itemCount.isZero()) {
            if (throwable.message?.isNotEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
            } else if(throwable.message?.isEmpty() == true) {
                globalError_reviewSeller?.setType(GlobalError.NO_CONNECTION)
            }

            filter_and_sort_layout?.gone()
            rvRatingProduct?.gone()
            emptyState_reviewProduct?.gone()
            search_bar_layout?.show()
            scrollView_globalError_reviewSeller?.show()
            globalError_reviewSeller?.show()

            globalError_reviewSeller.setActionClickListener {
                loadInitialData()
            }
        } else {
            onErrorLoadMoreToaster(getString(R.string.error_message_load_more_review_product), getString(R.string.action_retry_toaster_review_product))
        }
    }

    private fun onSuccessGetReviewProductListData(hasNextPage: Boolean, reviewProductList: List<ProductReviewUiModel>) {
        reviewSellerAdapter.hideLoading()
        swipeToRefreshReviewSeller?.isRefreshing = false
        if (reviewProductList.isEmpty()) {
            scrollView_emptyState_reviewSeller?.show()
            emptyState_reviewProduct?.show()
        } else {
            reviewSellerAdapter.setProductListReviewData(reviewProductList)
            updateScrollListenerState(hasNextPage)
        }
    }

    fun loadNextPage(page: Int) {
        viewModelListReviewList?.getNextProductReviewList(
                sortBy = viewModelListReviewList?.sortBy.orEmpty(),
                filterBy = viewModelListReviewList?.filterAllText.orEmpty(),
                page = page
        )
    }

    private fun coachMarkTabRatingProduct() {
        prefs?.let {
            if (!it.getBoolean(ReviewSellerConstant.HAS_TAB_RATING_PRODUCT, false)) {
                coachMarkItems.add(coachMarkTabRatingProduct)
                it.edit().putBoolean(ReviewSellerConstant.HAS_TAB_RATING_PRODUCT, true).apply()
            }
        }
    }

    private fun coachMarkFilterAndSort() {
        prefs?.let {
            if (!it.getBoolean(ReviewSellerConstant.HAS_FILTER_AND_SORT, false)) {
                coachMarkItems.add(coachMarkFilterAndSort)
                it.edit().putBoolean(ReviewSellerConstant.HAS_FILTER_AND_SORT, true).apply()
            }
        }
    }

    private fun initCoachMark(): CoachMark {
        val coachMark = CoachMarkBuilder().build()

        coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
            override fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean {

                if (nextStep > 0) {
                    firstTabItem?.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    firstTabItem?.setBackgroundColor(Color.WHITE)
                    firstTabItem?.setBackgroundResource(R.drawable.rounded_tab_unify)
                }

                val countCoachMarkItem = coachMarkItems.size - 1
                coachMark.enableSkip = (nextStep < countCoachMarkItem)

                return false
            }
        })

        coachMark.onFinishListener = {
            viewModelListReviewList?.isCompletedCoachMark = true
            firstTabItem?.setBackgroundColor(Color.TRANSPARENT)
        }

        return coachMark
    }


    override fun onAddedCoachMarkOverallRating(view: View) {
        coachMarkTabRatingProduct()
        coachMarkFilterAndSort()

        val coachMarkSummary: CoachMarkItem by lazy {
            CoachMarkItem(view.findViewById(R.id.cardSummary),
                    getString(R.string.average_rating_title),
                    getString(R.string.average_rating_desc)
            )
        }
        prefs?.let {
            if (!it.getBoolean(ReviewSellerConstant.HAS_OVERALL_RATING_PRODUCT, false)) {
                coachMarkItems.add(coachMarkSummary)
                it.edit().putBoolean(ReviewSellerConstant.HAS_OVERALL_RATING_PRODUCT, true).apply()
            }
        }
        showCoachMark()
    }

    private fun showCoachMark() {
        viewModelListReviewList?.let {
            if (!it.isCompletedCoachMark) {
                coachMark.show(activity, TAG_COACH_MARK_RATING_PRODUCT, coachMarkItems)
            }
        }
    }

    private fun initChipsSort(view: View) {
        chipsSort = view.findViewById(R.id.review_sort_chips)
        chipsSortText = getString(R.string.most_review)
        chipsSort?.chip_text?.text = chipsSortText
        chipsSort?.chip_right_icon?.setPadding(0, 0, chipsPaddingRight, 0)
        chipsSort?.chip_text?.width = maxChipTextWidth
        chipsSort?.chip_text?.ellipsize = TextUtils.TruncateAt.END

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
        chipsFilter?.chip_text?.text = chipsFilterText

        chipsFilter?.chip_right_icon?.setPadding(0, 0, chipsPaddingRight, 0)
        chipsFilter?.chip_text?.ellipsize = TextUtils.TruncateAt.END

        val filterList: Array<String> = resources.getStringArray(R.array.filter_review_product_array)
        val filterListItemUnify = SellerReviewProductListMapper.mapToItemUnifyList(filterList)

        filterListUnify?.setData(filterListItemUnify)

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

    override fun onItemProductReviewClicked(productId: Int) {
        startActivity(Intent(context, SellerReviewDetailActivity::class.java).apply {
            putExtra(SellerReviewDetailFragment.PRODUCT_ID, productId)
        })
    }

    private fun initBottomSheetFilter(filterListItemUnify: ArrayList<ListItemUnify>, title: String) {

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
                it.setSelectedFilterOrSort(filterListItemUnify, viewModelListReviewList?.positionFilter.orZero())
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
                it.setSelectedFilterOrSort(sortListItemUnify, viewModelListReviewList?.positionFilter.orZero())
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
            viewModelListReviewList?.positionFilter = position
            chipsFilterText = filterListItemUnify[position].listTitleText
            chipsFilter?.apply {
                chip_text.text = chipsFilterText
            }
            filterListUnify.setSelectedFilterOrSort(filterListItemUnify, position)
            viewModelListReviewList?.filterBy = ReviewSellerConstant.mapFilterReviewProduct().getKeyByValue(chipsFilterText)
            viewModelListReviewList?.filterAllText = ReviewSellerUtil.setFilterJoinValueFormat(viewModelListReviewList?.filterBy.orEmpty(), searchFilterText.orEmpty())
            loadInitialData()
            bottomSheetFilter?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onItemSortClickedBottomSheet(position: Int, sortListItemUnify: ArrayList<ListItemUnify>, sortListUnify: ListUnify) {
        try {
            viewModelListReviewList?.positionSort = position
            chipsSortText = sortListItemUnify[position].listTitleText
            chipsSort?.chip_text?.text = chipsSortText
            sortListUnify.setSelectedFilterOrSort(sortListItemUnify, position)
            viewModelListReviewList?.sortBy =
                    ReviewSellerConstant.mapSortReviewProduct().getKeyByValue(chipsSortText)
            loadInitialData()
            bottomSheetSort?.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
