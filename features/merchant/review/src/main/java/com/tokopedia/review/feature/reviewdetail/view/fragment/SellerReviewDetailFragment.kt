package com.tokopedia.review.feature.reviewdetail.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.coachmark.CoachMark
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringContract
import com.tokopedia.review.common.analytics.ReviewSellerPerformanceMonitoringListener
import com.tokopedia.review.common.util.*
import com.tokopedia.review.feature.reviewdetail.analytics.ProductReviewDetailTracking
import com.tokopedia.review.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.review.feature.reviewdetail.util.mapper.SellerReviewProductDetailMapper
import com.tokopedia.review.feature.reviewdetail.view.adapter.*
import com.tokopedia.review.feature.reviewdetail.view.bottomsheet.PopularTopicsBottomSheet
import com.tokopedia.review.feature.reviewdetail.view.model.*
import com.tokopedia.review.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.review.feature.reviewlist.util.mapper.SellerReviewProductListMapper
import com.tokopedia.review.feature.reviewreply.view.activity.SellerReviewReplyActivity
import com.tokopedia.review.feature.reviewreply.view.fragment.SellerReviewReplyFragment
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_seller_review_detail.*
import kotlinx.android.synthetic.main.item_overall_review_detail.view.*
import javax.inject.Inject

/**
 * @author by milhamj on 2020-02-14.
 */
class SellerReviewDetailFragment : BaseListFragment<Visitable<*>, SellerReviewDetailAdapterTypeFactory>(), SellerReviewDetailListener,
        OverallRatingDetailListener, ProductFeedbackDetailListener, SellerRatingAndTopicListener, ReviewSellerPerformanceMonitoringContract {

    companion object {
        const val PRODUCT_ID = "EXTRA_PRODUCT_ID"
        const val CHIP_FILTER = "EXTRA_CHIPS_FILTER"
        const val PRODUCT_IMAGE = "EXTRA_PRODUCT_IMAGE"
        const val SELECTED_DATE_CHIP = "selectedDateChip"
        const val SELECTED_DATE_POSITION = "selectedDatePosition"
        const val TAG_COACH_MARK_REVIEW_DETAIL = "coachMarkReviewDetail"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelProductReviewDetail: ProductReviewDetailViewModel? = null

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracking: ProductReviewDetailTracking

    private var cacheManager: SaveInstanceCacheManager? = null

    private var linearLayoutManager: LinearLayoutManager? = null
    private val reviewSellerDetailAdapter by lazy { SellerReviewDetailAdapter(sellerReviewDetailTypeFactory) }

    private val sellerReviewDetailTypeFactory by lazy {
        SellerReviewDetailAdapterTypeFactory(this, this, this, this)
    }

    private val coachMark: CoachMark by lazy {
        initCoachMark()
    }

    private val coachMarkMenuOption: CoachMarkItem by lazy {
        CoachMarkItem(view?.findViewById(R.id.menu_option_product_detail),
                getString(R.string.change_product_label),
                getString(R.string.change_product_desc))
    }

    private var chipFilterBundle = ""

    private var productID: Int = 0
    private var productName = ""
    private var variantName = ""
    private var productImageUrl = ""
    private var toolbarTitle = ""

    private var positionFilterPeriod = 1

    private var filterPeriodDetailUnify: ListUnify? = null
    private var optionFeedbackDetailUnify: ListUnify? = null
    private var optionMenuDetailUnify: ListUnify? = null

    private var bottomSheetPeriodDetail: BottomSheetUnify? = null
    private var bottomSheetOptionFeedback: BottomSheetUnify? = null
    private var bottomSheetMenuDetail: BottomSheetUnify? = null

    private var reviewSellerPerformanceMonitoringListener: ReviewSellerPerformanceMonitoringListener? = null

    override fun getScreenName(): String = context?.getString(R.string.title_review_detail_page).orEmpty()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reviewSellerPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                productID = getIntExtra(PRODUCT_ID, 0)
                chipFilterBundle = getStringExtra(CHIP_FILTER) ?: ReviewConstants.ALL_VALUE
                productImageUrl = getStringExtra(PRODUCT_IMAGE) ?: ""
            }
        }
        super.onCreate(savedInstanceState)
        tracking.sendScreenDetail(userSession.shopId.orEmpty(), productID.toString())
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModelProductReviewDetail = ViewModelProvider(this, viewModelFactory).get(ProductReviewDetailViewModel::class.java)
        initFilterData()
    }

    private fun initFilterData() {
        val filterDetailList: Array<String> = resources.getStringArray(R.array.filter_review_detail_array)
        viewModelProductReviewDetail?.filterPeriod = ReviewConstants.mapFilterReviewDetail().getKeyByValue(chipFilterBundle)
        positionFilterPeriod = ReviewUtil.getDateChipFilterPosition(filterDetailList, chipFilterBundle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_review_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0))
        viewModelProductReviewDetail?.setChipFilterDateText(chipFilterBundle)
        initToolbar()
        initViewBottomSheet()
        startNetworkRequestPerformanceMonitoring()
        stopPreparePerformancePageMonitoring()
        observeLiveData()
    }

    override fun initInjector() {
        getComponent(ReviewProductDetailComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): SellerReviewDetailAdapterTypeFactory = sellerReviewDetailTypeFactory

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        loadNextPage(page)
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        reviewSellerDetailAdapter.clearAllElements()
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

    override fun onDestroy() {
        viewModelProductReviewDetail?.productFeedbackDetail?.removeObservers(this)
        viewModelProductReviewDetail?.reviewInitialData?.removeObservers(this)
        viewModelProductReviewDetail?.flush()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option_review_product_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_product_detail -> {
                clickOptionMenuDetail()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun stopPreparePerformancePageMonitoring() {
        reviewSellerPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        reviewSellerPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        rvRatingDetail?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                reviewSellerPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                reviewSellerPerformanceMonitoringListener?.stopPerformanceMonitoring()
                rvRatingDetail.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): ReviewSellerPerformanceMonitoringListener? {
        return if(context is ReviewSellerPerformanceMonitoringListener) {
            context
        } else {
            null
        }
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

    private fun clickOptionMenuDetail() {
        tracking.eventClickOptionMenuDetail(userSession.shopId.orEmpty(), productID.toString())

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
                            tracking.eventClickOptionEditProduct(userSession.shopId.orEmpty(), productID.toString())
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

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return swipeToRefreshLayoutDetail
    }

    override fun onSwipeRefresh() {
        swipeToRefresh?.isRefreshing = false
        clearAllData()
        loadInitialData()
    }

    private fun observeLiveData() {
        viewModelProductReviewDetail?.reviewInitialData?.observe(viewLifecycleOwner, Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    stopNetworkRequestPerformanceMonitoring()
                    startRenderPerformanceMonitoring()
                    swipeToRefresh?.isRefreshing = false
                    productName = it.data.first.filterIsInstance<OverallRatingDetailUiModel>().firstOrNull()?.productName.orEmpty()
                    viewModelProductReviewDetail?.updateRatingFilterData(it.data.first.filterIsInstance<ProductReviewFilterUiModel>().firstOrNull()?.ratingBarList
                            ?: listOf())
                    viewModelProductReviewDetail?.updateTopicsFilterData(it.data.first.filterIsInstance<TopicUiModel>().firstOrNull()?.sortFilterItemList
                            ?: arrayListOf())

                    toolbarTitle = it.data.second
                    review_detail_toolbar?.title = toolbarTitle

                    renderList(it.data.first, it.data.third)
                    coachMarkShow()
                }
                is Fail -> {
                    onErrorGetReviewDetailData()
                }
            }
        })

        viewModelProductReviewDetail?.productFeedbackDetail?.observe(viewLifecycleOwner, Observer {
            reviewSellerDetailAdapter.hideLoading()
            when (it) {
                is Success -> {
                    onSuccessGetFeedbackReviewListData(it.data)
                }
                is Fail -> {
                    onErrorGetReviewDetailData()
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

    private fun onErrorGetReviewDetailData() {
        swipeToRefresh?.isRefreshing = false
        val feedbackReviewCount = reviewSellerDetailAdapter.list.count { it is FeedbackUiModel }
        if (feedbackReviewCount == 0) {
            globalError_reviewDetail?.setType(GlobalError.SERVER_ERROR)
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
            Toaster.build(it, message, actionText = action, type = Toaster.TYPE_ERROR, clickListener = View.OnClickListener {
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
        tracking.eventClickTimeFilter(userSession.shopId.orEmpty(), productID.toString())
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
                it.setSelectedFilterOrSort(filterPeriodItemUnify, positionFilterPeriod.orZero())

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
            tracking.eventClickApplyTimeFilter(
                    userSession.shopId.orEmpty(),
                    productID.toString(),
                    filterListItemUnify[position].listTitleText)

            setIntentResultChipDate(filterListItemUnify[position].listTitleText, position)
            positionFilterPeriod = position
            filterListUnify.setSelectedFilterOrSort(filterListItemUnify, position)
            viewModelProductReviewDetail?.setChipFilterDateText(filterListItemUnify[position].listTitleText)
            endlessRecyclerViewScrollListener?.resetState()
            bottomSheetPeriodDetail?.dismiss()
            loadInitialData()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setIntentResultChipDate(selectedDateChip: String, position: Int) {
        val returnIntent = Intent()
        returnIntent.putExtra(SELECTED_DATE_CHIP, selectedDateChip)
        returnIntent.putExtra(SELECTED_DATE_POSITION, position)

        if (selectedDateChip == ReviewConstants.ALL_VALUE) activity?.setResult(Activity.RESULT_CANCELED) else activity?.setResult(Activity.RESULT_OK, returnIntent)
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

    override fun onOptionFeedbackClicked(view: View, title: String, data: FeedbackUiModel,
                                         optionDetailListItemUnify: ArrayList<ListItemUnify>, isEmptyReply: Boolean) {
        this.variantName = data.variantName.orEmpty()
        val feedbackReplyUiModel = ProductReplyUiModel(productID, productImageUrl, productName, variantName)

        cacheManager = context?.let {
            SaveInstanceCacheManager(it, true).apply {
                put(SellerReviewReplyFragment.EXTRA_FEEDBACK_DATA, data)
                put(SellerReviewReplyFragment.EXTRA_PRODUCT_DATA, feedbackReplyUiModel)
            }
        }

        tracking.eventClickOptionFeedbackReview(
                userSession.shopId.orEmpty(),
                productID.toString(),
                data.feedbackID.toString()
        )
        optionFeedbackDetailUnify?.setData(optionDetailListItemUnify)

        bottomSheetOptionFeedback?.apply {
            setTitle(title)
            showCloseIcon = true
            setCloseClickListener {
                tracking.eventClickCloseFeedbackOptionBottomSheet(
                        userSession.shopId.orEmpty(),
                        productID.toString(),
                        data.feedbackID.toString()
                )
                dismiss()
            }
        }

        onOptionFeedbackItemClicked(isEmptyReply, data)

        fragmentManager?.let {
            bottomSheetOptionFeedback?.show(it, title)
        }
    }

    private fun onOptionFeedbackItemClicked(isEmptyReply: Boolean, data: FeedbackUiModel) {
        optionFeedbackDetailUnify?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            startActivity(Intent(context, SellerReviewReplyActivity::class.java).apply {
                                putExtra(SellerReviewReplyFragment.CACHE_OBJECT_ID, cacheManager?.id)
                                putExtra(SellerReviewReplyFragment.EXTRA_SHOP_ID, userSession.shopId.orEmpty())
                                putExtra(SellerReviewReplyFragment.IS_EMPTY_REPLY_REVIEW, isEmptyReply)
                            })
                            bottomSheetOptionFeedback?.dismiss()
                        }
                        1 -> {
                            tracking.eventClickReportOnBottomSheet(userSession.shopId.orEmpty(),
                                    productID.toString(),
                                    data.feedbackID.toString())
                            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
                            intent.putExtra(ApplinkConstInternalMarketplace.ARGS_SHOP_ID, userSession.shopId.toInt())
                            intent.putExtra(ApplinkConstInternalMarketplace.ARGS_REVIEW_ID, data.feedbackID.toString())
                            startActivity(intent)
                            bottomSheetOptionFeedback?.dismiss()
                        }
                    }
                }
            }
        }
    }

    override fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int) {
        context?.run {
            tracking.eventClickImagePreviewSlider(
                    feedbackId,
                    thumbnailsUrl.getOrNull(position).orEmpty(),
                    position.toString())
            startActivity(ImagePreviewSliderActivity.getCallingIntent(
                    context = this,
                    title = toolbarTitle,
                    imageUrls = imageUrls,
                    imageThumbnailUrls = thumbnailsUrl,
                    imagePosition = position
            ))
        }
    }

    override fun onFeedbackMoreReplyClicked(feedbackId: String) {
        tracking.eventClickReadMoreFeedback(userSession.shopId.orEmpty(), productID.toString(), feedbackId)
    }

    /**
     * Listener Section
     */
    override fun onChildTopicFilterClicked(item: SortFilterItem, adapterPosition: Int) {
        val updatedState = item.type == ChipsUnify.TYPE_SELECTED
        tracking.eventClickFilterTopicSelected(
                userSession.shopId.orEmpty(),
                productID.toString(),
                item.title.toString(),
                updatedState.toString())
        val getTopicsFilterFromAdapter = reviewSellerDetailAdapter.list.filterIsInstance<TopicUiModel>().firstOrNull()
        reviewSellerDetailAdapter.updateFilterTopic(adapterPosition, item.title.toString(), updatedState, getTopicsFilterFromAdapter)
        getTopicsFilterFromAdapter?.sortFilterItemList?.let { viewModelProductReviewDetail?.setFilterTopicDataText(it) }
        endlessRecyclerViewScrollListener?.resetState()
    }

    override fun onParentTopicFilterClicked() {
        tracking.eventClickSortOrFilterTopics(
                userSession.shopId.orEmpty(),
                productID.toString())

        val bottomSheet = PopularTopicsBottomSheet(activity, tracking, userSession, productID, ::onTopicsClicked)
        viewModelProductReviewDetail?.getFilterTopicData()?.let { bottomSheet.setTopicListData(it) }
        viewModelProductReviewDetail?.getSortTopicData()?.let { bottomSheet.setSortListData(it) }
        bottomSheet.showDialog()
    }

    override fun onRatingCheckBoxClicked(ratingAndState: Pair<Int, Boolean>, ratingSelected: Int, adapterPosition: Int) {
        tracking.eventClickStarFilter(
                shopId = userSession.shopId.orEmpty(),
                productId = productID.toString(),
                starSelected = ratingSelected.toString(),
                isActive = ratingAndState.second.toString()
        )
        val getRatingFilterFromAdapter = reviewSellerDetailAdapter.list.filterIsInstance<ProductReviewFilterUiModel>().firstOrNull()
        val getSelectedCheckbox = getRatingFilterFromAdapter?.ratingBarList?.getOrNull(adapterPosition)
        if (getSelectedCheckbox?.ratingIsChecked != ratingAndState.second && getSelectedCheckbox != null) {
            reviewSellerDetailAdapter.updateFilterRating(adapterPosition, ratingAndState.second, getRatingFilterFromAdapter)
            viewModelProductReviewDetail?.setFilterRatingDataText(getRatingFilterFromAdapter.ratingBarList)
            endlessRecyclerViewScrollListener?.resetState()
            reviewSellerDetailAdapter.removeReviewNotFound()
            reviewSellerDetailAdapter.showLoading()
        }
    }

    private fun onTopicsClicked(topic: List<SortFilterItemWrapper>, sort: List<SortItemUiModel>) {
        var isDifferent = false
        val sortBy = sort.firstOrNull { it.isSelected }?.title.orEmpty()
        val sortValue = ReviewConstants.mapSortReviewDetail().getKeyByValue(sortBy)

        viewModelProductReviewDetail?.getSortAndFilter()?.first?.mapIndexed { index, data ->
            if (topic.isNotEmpty()) {
                isDifferent = topic.getOrNull(index)?.isSelected == data.isSelected
            }
        }

        if (viewModelProductReviewDetail?.getSortAndFilter()?.second == sortBy && isDifferent) return

        reviewSellerDetailAdapter.updateTopicFromBottomSheet(topic)
        viewModelProductReviewDetail?.setSortAndFilterTopicData(topic to sortValue)
        viewModelProductReviewDetail?.updateSortAndFilterTopicData(topic to sortValue)
        endlessRecyclerViewScrollListener?.resetState()
        reviewSellerDetailAdapter.removeReviewNotFound()
        reviewSellerDetailAdapter.showLoading()
    }

}