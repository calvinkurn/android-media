package com.tokopedia.reviewseller.feature.reviewdetail.view.fragment

import android.app.Activity
import android.content.Intent
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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.*
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.*
import com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet.PopularTopicsBottomSheet
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.*
import com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.reviewseller.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
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
class SellerReviewDetailFragment : BaseListFragment<Visitable<*>, SellerReviewDetailAdapterTypeFactory>(), SellerReviewDetailListener,
        OverallRatingDetailListener, ProductFeedbackDetailListener, SellerRatingAndTopicListener {

    companion object {
        const val PRODUCT_ID = "EXTRA_SHOP_ID"
        const val CHIP_FILTER = "EXTRA_CHIPS_FILTER"
        private const val TAG_COACH_MARK_REVIEW_DETAIL = "coachMarkReviewDetail"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelProductReviewDetail: ProductReviewDetailViewModel? = null

    private var linearLayoutManager: LinearLayoutManager? = null
    private val reviewSellerDetailAdapter by lazy { SellerReviewDetailAdapter(sellerReviewDetailTypeFactory) }

    private var swipeToRefreshReviewDetail: SwipeToRefresh? = null

    private val sellerReviewDetailTypeFactory by lazy {
        SellerReviewDetailAdapterTypeFactory(this, this, this, this)
    }

    private var chipFilterBundle = ""

    var productID: Int = 0
    var filterBy: String = "time=all"
    var toolbarTitle = ""

    private var filterPeriodDetailUnify: ListUnify? = null
    private var optionFeedbackDetailUnify: ListUnify? = null
    private var optionMenuDetailUnify: ListUnify? = null

    private var bottomSheetPeriodDetail: BottomSheetUnify? = null
    private var bottomSheetOptionFeedback: BottomSheetUnify? = null
    private var bottomSheetMenuDetail: BottomSheetUnify? = null

    private val coachMark: CoachMark by lazy {
        initCoachMark()
    }

    private val coachMarkMenuOption: CoachMarkItem by lazy {
        CoachMarkItem(view?.findViewById(R.id.menu_option_product_detail),
                getString(R.string.change_product_label),
                getString(R.string.change_product_desc))
    }

    override fun getScreenName(): String = "SellerReviewDetail"

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                productID = getIntExtra(PRODUCT_ID, 0)
                chipFilterBundle = getStringExtra(CHIP_FILTER)
            }
        }
        super.onCreate(savedInstanceState)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModelProductReviewDetail = ViewModelProvider(this, viewModelFactory).get(ProductReviewDetailViewModel::class.java)
        iniFilterData()
    }

    private fun iniFilterData() {
        val filterDetailList: Array<String> = resources.getStringArray(R.array.filter_review_detail_array)

        viewModelProductReviewDetail?.filterPeriod = ReviewSellerConstant.mapFilterReviewDetail().getKeyByValue(chipFilterBundle)
        viewModelProductReviewDetail?.positionFilterPeriod = ReviewSellerUtil.getDateChipFilterPosition(filterDetailList, chipFilterBundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_review_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProductReviewDetail?.setChipFilterDateText(chipFilterBundle)
        initToolbar()
        initRecyclerView(view)
        initSwipeToRefRefresh(view)
        initViewBottomSheet()
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
        showLoading()

        viewModelProductReviewDetail?.getProductRatingDetail(
                productID,
                viewModelProductReviewDetail?.sortBy.orEmpty())
    }

    override fun getRecyclerView(view: View): RecyclerView {
        return view.findViewById(R.id.rvRatingDetail)
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, SellerReviewDetailAdapterTypeFactory> {
        return reviewSellerDetailAdapter
    }

    override fun createEndlessRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : DataEndlessScrollListener(linearLayoutManager, reviewSellerDetailAdapter) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                reviewSellerDetailAdapter.showLoading()
                loadNextPage(page)
            }

            override fun isDataEmpty(): Boolean {
                return reviewSellerDetailAdapter.list.isEmpty()
            }
        }
    }

    override fun onDestroy() {
        viewModelProductReviewDetail?.productFeedbackDetail?.removeObservers(this)
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

    private fun initCoachMark(): CoachMark {
        val coachMark = CoachMarkBuilder().build()
        CoachMarkBuilder().build()

        coachMark.setShowCaseStepListener(object : CoachMark.OnShowCaseStepListener {
            override fun onShowCaseGoTo(previousStep: Int, nextStep: Int, coachMarkItem: CoachMarkItem): Boolean {

                coachMark.enableSkip = false

                return false
            }
        })
        return coachMark
    }

    private fun coachMarkShow() {
        activity?.let {
            if (!coachMark.hasShown(it, TAG_COACH_MARK_REVIEW_DETAIL)) {
                coachMark.show(it, TAG_COACH_MARK_REVIEW_DETAIL, arrayListOf(coachMarkMenuOption))
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
                        0 -> {
                            RouteManager.route(context, ApplinkConst.PRODUCT_EDIT, productID.toString())
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
                sortBy = viewModelProductReviewDetail?.sortBy.orEmpty(),
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
        viewModelProductReviewDetail?.reviewInitialData?.observe(this, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    swipeToRefreshReviewDetail?.isRefreshing = false
                    viewModelProductReviewDetail?.updateRatingFilterData(it.data.first.filterIsInstance<ProductReviewFilterUiModel>().firstOrNull()?.ratingBarList
                            ?: listOf())
                    viewModelProductReviewDetail?.updateTopicsFilterData(it.data.first.filterIsInstance<TopicUiModel>().firstOrNull()?.sortFilterItemList
                            ?: arrayListOf())

                    toolbarTitle = it.data.second
                    review_detail_toolbar.title = toolbarTitle

                    renderList(it.data.first, it.data.third)

                    coachMarkShow()
                }
                is Fail -> {
                    onErrorGetReviewDetailData(it.throwable)
                }
            }
        })

        viewModelProductReviewDetail?.productFeedbackDetail?.observe(this, Observer {
            reviewSellerDetailAdapter.hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetFeedbackReviewListData(it.data)
                }
                is Fail -> {
                    onErrorGetReviewDetailData(it.throwable)
                }
            }
        })
    }

    private fun onSuccessGetFeedbackReviewListData(data: ProductFeedbackDetailUiModel) {
        if (data.page == 1 && data.productFeedbackDetailList.isEmpty()) {
            // We only want to show no data found if there is no data left loaded
            reviewSellerDetailAdapter.addReviewNotFound()
        } else {
            reviewSellerDetailAdapter.removeReviewNotFound()
            reviewSellerDetailAdapter.setFeedbackListData(data.productFeedbackDetailList, data.reviewCount)
        }
        updateScrollListenerState(data.hasNext)
    }

    private fun onErrorGetReviewDetailData(throwable: Throwable) {
        swipeToRefreshReviewDetail?.isRefreshing = false
        val feedbackReviewCount = reviewSellerDetailAdapter.list.count { it is FeedbackUiModel }
        if (feedbackReviewCount == 0) {
            globalError_reviewSeller?.setType(GlobalError.SERVER_ERROR)
            reviewSellerDetailAdapter.removeReviewNotFound()
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
            setOnDismissListener {
                view.review_period_filter_button_detail.toggle()
            }
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        filterPeriodDetailUnify?.let { it ->
            it.onLoadFinish {
                it.setSelectedFilterOrSort(filterPeriodItemUnify, viewModelProductReviewDetail?.positionFilterPeriod.orZero())

                it.setOnItemClickListener { _, _, position, _ ->
                    onItemFilterClickedBottomSheet(position, filterPeriodItemUnify, it)
                }

                filterPeriodItemUnify.forEachIndexed { position, listItemUnify ->
                    listItemUnify.listRightRadiobtn?.setOnClickListener { _ ->
                        onItemFilterClickedBottomSheet(position, filterPeriodItemUnify, it)
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetPeriodDetail?.show(it, title)
        }
    }

    private fun onItemFilterClickedBottomSheet(position: Int, filterListItemUnify: ArrayList<ListItemUnify>, filterListUnify: ListUnify) {
        try {
            if (position == viewModelProductReviewDetail?.positionFilterPeriod) return
            setIntentResultChipDate(filterListItemUnify[position].listTitleText, position)
            viewModelProductReviewDetail?.positionFilterPeriod = position
            filterListUnify.setSelectedFilterOrSort(filterListItemUnify, position)
            viewModelProductReviewDetail?.setChipFilterDateText(filterListItemUnify[position].listTitleText)
            bottomSheetPeriodDetail?.dismiss()
            loadInitialData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setIntentResultChipDate(selectedDateChip: String, position: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra("selectedDateChip", selectedDateChip)
        returnIntent.putExtra("selectedDatePosition", position)

        if (selectedDateChip == ReviewSellerConstant.ALL_VALUE) activity?.setResult(Activity.RESULT_CANCELED) else activity?.setResult(Activity.RESULT_OK, returnIntent)
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
                        0 -> {
                            if (!isEmptyReply) {

                            }
                        }
                        1 -> {
                            RouteManager.route(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetOptionFeedback?.show(it, title)
        }
    }

    override fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, position: Int) {
        context?.run {
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    context = this,
                    title = toolbarTitle,
                    imageUrls = imageUrls,
                    imageThumbnailUrls = thumbnailsUrl,
                    imagePosition = position
            ))
        }
    }

    /**
     * Listener Section
     */
    override fun onChildTopicFilterClicked(item: SortFilterItem, adapterPosition: Int) {
        val getTopicsFilterFromAdapter = reviewSellerDetailAdapter.list.filterIsInstance<TopicUiModel>().firstOrNull()
        val updatedState = item.type == ChipsUnify.TYPE_SELECTED
        reviewSellerDetailAdapter.updateFilterTopic(adapterPosition, item.title.toString(), updatedState, getTopicsFilterFromAdapter)
        viewModelProductReviewDetail?.setFilterTopicDataText(getTopicsFilterFromAdapter?.sortFilterItemList)
    }

    override fun onParentTopicFilterClicked() {
        val bottomSheet = PopularTopicsBottomSheet(activity,  ::onTopicsClicked)
        viewModelProductReviewDetail?.filterTopicData?.let { bottomSheet.setFilterTopicListData(it) }
        viewModelProductReviewDetail?.sortTopicData?.let { bottomSheet.setSortTopic(it) }
        bottomSheet.showDialog()
    }

    override fun onRatingCheckBoxClicked(ratingAndState: Pair<Int, Boolean>, adapterPosition: Int) {
        val getRatingFilterFromAdapter = reviewSellerDetailAdapter.list.filterIsInstance<ProductReviewFilterUiModel>().firstOrNull()
        val getSelectedCheckbox = getRatingFilterFromAdapter?.ratingBarList?.getOrNull(adapterPosition)
        if (getSelectedCheckbox?.ratingIsChecked != ratingAndState.second && getSelectedCheckbox != null) {
            reviewSellerDetailAdapter.updateFilterRating(adapterPosition, ratingAndState.second, getRatingFilterFromAdapter)
            viewModelProductReviewDetail?.setFilterRatingDataText(getRatingFilterFromAdapter.ratingBarList)
            reviewSellerDetailAdapter.removeReviewNotFound()
            reviewSellerDetailAdapter.showLoading()
        }
    }

    private fun onTopicsClicked(topic: List<SortFilterItemWrapper>, sort: List<SortItemUiModel>) {
        var isDifferent = false
        val sortBy = sort.firstOrNull { it.isSelected }?.title.orEmpty()
        val sortValue = ReviewSellerConstant.mapSortReviewDetail().getKeyByValue(sortBy)

        viewModelProductReviewDetail?.sortAndFilter?.first?.mapIndexed { index, data ->
            isDifferent = topic[index].isSelected == data.isSelected
        }

        if(viewModelProductReviewDetail?.sortAndFilter?.second == sortBy && isDifferent) return

        reviewSellerDetailAdapter.updateTopicFromBottomSheet(topic)
        viewModelProductReviewDetail?.setSortAndFilterTopicData(topic to sortValue)
        reviewSellerDetailAdapter.removeReviewNotFound()
        reviewSellerDetailAdapter.showLoading()
    }

}