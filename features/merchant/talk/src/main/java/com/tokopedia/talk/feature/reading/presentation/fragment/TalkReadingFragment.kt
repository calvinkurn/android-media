package com.tokopedia.talk.feature.reading.presentation.fragment

import com.tokopedia.imageassets.TokopediaImageUrl

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.talk.R
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.analytics.TalkTrackingConstants
import com.tokopedia.talk.common.constants.TalkConstants
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_PRODUCT_ID
import com.tokopedia.talk.common.constants.TalkConstants.PARAM_SHOP_ID
import com.tokopedia.talk.common.constants.TalkConstants.QUESTION_ID
import com.tokopedia.talk.databinding.FragmentTalkReadingBinding
import com.tokopedia.talk.feature.reading.analytics.TalkReadingTracking
import com.tokopedia.talk.feature.reading.analytics.TalkReadingTrackingConstants.EVENT_ACTION_CREATE_NEW_QUESTION
import com.tokopedia.talk.feature.reading.analytics.TalkReadingTrackingConstants.EVENT_ACTION_SEND_QUESTION_AT_EMPTY_TALK
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkGoToReply
import com.tokopedia.talk.feature.reading.data.model.TalkGoToWrite
import com.tokopedia.talk.feature.reading.data.model.TalkLastAction
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import com.tokopedia.talk.feature.reading.data.model.ViewState
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapter
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnCategoryModifiedListener
import com.tokopedia.talk.feature.reading.presentation.widget.OnFinishedSelectSortListener
import com.tokopedia.talk.feature.reading.presentation.widget.TalkReadingSortBottomSheet
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

open class TalkReadingFragment : BaseListFragment<TalkReadingUiModel,
        TalkReadingAdapterTypeFactory>(), HasComponent<TalkReadingComponent>,
        OnFinishedSelectSortListener, OnCategoryModifiedListener,
        ThreadListener, TalkPerformanceMonitoringContract {

    companion object {
        const val TOASTER_CTA_WIDTH = 300
        const val DEFAULT_DISCUSSION_DATA_LIMIT = 10
        const val DEFAULT_INITIAL_PAGE = 1
        const val TALK_REPLY_ACTIVITY_REQUEST_CODE = 202
        const val TALK_WRITE_ACTIVITY_REQUEST_CODE = 203
        const val LOGIN_ACTIVITY_REQUEST_CODE = 204
        const val TALK_READING_EMPTY_IMAGE_URL = TokopediaImageUrl.TALK_READING_EMPTY_IMAGE_URL

        @JvmStatic
        fun createNewInstance(productId: String, shopId: String, isVariantSelected: Boolean, availableVariants: String): TalkReadingFragment =
            TalkReadingFragment().apply {
                arguments = Bundle()
                arguments?.putString(PARAM_PRODUCT_ID, productId)
                arguments?.putString(PARAM_SHOP_ID, shopId)
                arguments?.putBoolean(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected)
                arguments?.putString(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, availableVariants)
            }
    }

    @Inject
    lateinit var viewModel: TalkReadingViewModel

    private var productId: String = ""
    private var shopId: String = ""
    private var isVariantSelected: Boolean = false
    private var availableVariants: String = ""
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null

    private var talkReadingFragmentBinding by autoClearedNullable<FragmentTalkReadingBinding>()

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        return TalkReadingAdapterTypeFactory(this)
    }

    override fun getRecyclerView(view: View?): RecyclerView? {
        talkReadingFragmentBinding?.talkReadingRecyclerView?.adapter = adapter
        return talkReadingFragmentBinding?.talkReadingRecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        talkReadingFragmentBinding = FragmentTalkReadingBinding.inflate(inflater, container, false)
        return talkReadingFragmentBinding?.root
    }

    override fun createAdapterInstance(): BaseListAdapter<TalkReadingUiModel, TalkReadingAdapterTypeFactory> {
        return TalkReadingAdapter(adapterTypeFactory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        getDataFromArguments()
        observeViewState()
        observeProductHeader()
        observeSortOptions()
        observeDiscussionData()
        observeFilterCategories()
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
    }

    override fun getScreenName(): String {
        return TalkTrackingConstants.SCREEN_NAME_TALK
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onItemClicked(t: TalkReadingUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {
        getDiscussionData(page)
    }

    override fun getComponent(): TalkReadingComponent? {
        return activity?.run {
            DaggerTalkReadingComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onFinishChooseSort(sortOption: SortOption) {
        TalkReadingTracking.eventClickSort(sortOption.displayName, viewModel.getUserId(), productId)
        viewModel.updateSelectedSort(sortOption)
    }

    override fun onSwipeRefresh() {
        isLoadingInitialData = true
        swipeToRefresh.isRefreshing = true
        clearAllData()
        loadInitialData()
    }

    override fun onCategorySelected(categoryName: String, chipType: String) {
        val isSelected = chipType == ChipsUnify.TYPE_SELECTED
        if(isSelected) {
            TalkReadingTracking.eventClickFilter(categoryName, viewModel.getUserId(), productId)
        }
        selectUnselectCategory(categoryName, isSelected)
    }

    override fun onCategoriesCleared() {
        resetSortOptions()
        unselectCategories()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return talkReadingFragmentBinding?.readingSwipeToRefresh
    }

    override fun onThreadClicked(questionID: String) {
        TalkReadingTracking.eventClickThread(
                getSelectedCategoryDisplayName(),
                viewModel.getUserId(),
                productId,
                questionID
        )
        if(viewModel.isUserLoggedIn()) {
            goToReplyActivity(questionID)
            return
        }
        updateLastAction(TalkGoToReply(questionID))
        goToLoginActivity()
    }

    override fun onLinkClicked(link: String): Boolean {
        return RouteManager.route(context, link)
    }

    override fun onDestroy() {
        removeObservers(viewModel.discussionData)
        removeObservers(viewModel.discussionAggregate)
        removeObservers(viewModel.filterCategories)
        removeObservers(viewModel.sortOptions)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            TALK_REPLY_ACTIVITY_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_FIRST_USER -> {
                    data?.let {
                        onSuccessDeleteQuestion(it.getStringExtra(QUESTION_ID) ?: "")
                    }
                }
                Activity.RESULT_OK -> {
                    resetSortOptions()
                    unselectCategories()
                    (viewModel.discussionAggregate.value as? Success)?.let {
                        talkReadingFragmentBinding?.talkReadingHeader?.clearAllSort(
                                TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(
                                        it.data.discussionAggregateResponse,
                                        { showBottomSheet() },
                                        this
                                ), this
                        ) { showBottomSheet() }
                    }
                }
            }
            TALK_WRITE_ACTIVITY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateQuestion()
            }
            LOGIN_ACTIVITY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                when (viewModel.talkLastAction) {
                    is TalkGoToReply -> {
                        goToReplyActivity((viewModel.talkLastAction as TalkGoToReply).questionId)
                    }
                    is TalkGoToWrite -> {
                        goToWriteActivity(EVENT_ACTION_CREATE_NEW_QUESTION)
                    }
                    else -> {
                        //no-op
                    }
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        talkPerformanceMonitoringListener = castContextToTalkPerformanceMonitoringListener(context)
    }

    override fun stopPreparePerfomancePageMonitoring() {
        talkPerformanceMonitoringListener?.stopPreparePagePerformanceMonitoring()
    }

    override fun startNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startNetworkRequestPerformanceMonitoring()
    }

    override fun stopNetworkRequestPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.stopNetworkRequestPerformanceMonitoring()
    }

    override fun startRenderPerformanceMonitoring() {
        talkPerformanceMonitoringListener?.startRenderPerformanceMonitoring()
        talkReadingFragmentBinding?.talkReadingRecyclerView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                talkPerformanceMonitoringListener?.stopRenderPerformanceMonitoring()
                talkPerformanceMonitoringListener?.stopPerformanceMonitoring()
                talkReadingFragmentBinding?.talkReadingRecyclerView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
            }
        })
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if(context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    override fun loadInitialData() {
        isLoadingInitialData = true
        getHeaderData()
    }

    override fun onStart() {
        super.onStart()
        activity?.run {
            TalkReadingTracking.sendScreen(screenName, productId, viewModel.isUserLoggedIn(), viewModel.getUserId())
        }
    }

    private fun showPageEmpty() {
        talkReadingFragmentBinding?.pageEmpty?.readingEmptyError?.loadImage(TALK_READING_EMPTY_IMAGE_URL)
        talkReadingFragmentBinding?.addFloatingActionButton?.circleMainMenu?.hide()
        talkReadingFragmentBinding?.pageEmpty?.root?.show()
        talkReadingFragmentBinding?.pageEmpty?.readingEmptyAskButton?.setOnClickListener {
            if(viewModel.isUserLoggedIn()) {
                goToWriteActivity(EVENT_ACTION_SEND_QUESTION_AT_EMPTY_TALK)
            } else {
                updateLastAction(TalkGoToWrite)
                goToLoginActivity()
            }
        }
    }

    private fun showPageError() {
        talkReadingFragmentBinding?.addFloatingActionButton?.circleMainMenu?.hide()
        talkReadingFragmentBinding?.pageError?.root?.show()
        talkReadingFragmentBinding?.pageError?.readingImageError?.loadImage(com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection)
        talkReadingFragmentBinding?.pageError?.talkConnectionErrorRetryButton?.setOnClickListener {
            loadInitialData()
        }
    }

    private fun hidePageError() {
        talkReadingFragmentBinding?.pageError?.root?.hide()
    }

    private fun bindHeader(talkReadingHeaderModel: TalkReadingHeaderModel, showBottomSheet: () -> Unit) {
        talkReadingFragmentBinding?.talkReadingHeader?.bind(talkReadingHeaderModel, this, showBottomSheet)
    }

    private fun observeProductHeader() {
        viewModel.discussionAggregate.observe(viewLifecycleOwner,
            {
            when (it) {
                is Success -> {
                    bindHeader(
                            TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(
                                    it.data.discussionAggregateResponse,
                                    { showBottomSheet() },
                                    this
                            )) { showBottomSheet() }
                    initSortOptions()
                    initFilterCategories(TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingCategories(it.data))
                    showContainer()
                }
                else -> {
                    //no-op
                }
            }

        })
    }

    private fun observeDiscussionData() {
        viewModel.discussionData.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> {
                    it.data.discussionData.let { data ->
                        TalkReadingTracking.eventLoadData((viewModel.viewState.value as? ViewState.Success)?.page.toString(), it.data.discussionData.question.size.toString(), viewModel.getUserId(), productId)
                        if (data.question.isNotEmpty()) {
                            stopNetworkRequestPerformanceMonitoring()
                            startRenderPerformanceMonitoring()
                            renderDiscussionData(TalkReadingMapper.mapDiscussionDataResponseToTalkReadingUiModel(data), data.hasNext)
                        }
                    }
                }
                else -> {
                    //no-op
                }
            }
        })
    }

    private fun observeSortOptions() {
        viewModel.sortOptions.observe(viewLifecycleOwner, { sortOptions ->
            updateSortHeader(sortOptions.first { it.isSelected })
            if(!isLoadingInitialData) {
                isLoadingInitialData = true
                adapter.clearAllElements()
                getDiscussionData(withDelay = true)
            }
        })
    }

    private fun observeFilterCategories() {
        viewModel.filterCategories.observe(viewLifecycleOwner, {
            if(!isLoadingInitialData) {
                isLoadingInitialData = true
                adapter.clearAllElements()
                getDiscussionData()
            }
        })
    }

    private fun observeViewState() {
        viewModel.viewState.observe(viewLifecycleOwner, {
            when(it) {
                is ViewState.Loading -> {
                    if(it.isRefreshing) {
                        clearAllData()
                        talkReadingFragmentBinding?.pageLoading?.root?.show()
                    } else {
                        showLoading()
                    }
                    talkReadingFragmentBinding?.pageEmpty?.root?.hide()
                    hidePageError()
                }
                is ViewState.Error -> {
                    isLoadingInitialData = false
                    if(it.page > DEFAULT_INITIAL_PAGE) {
                        showErrorToaster()
                    } else {
                        showPageError()
                    }
                    hideLoading()
                    talkReadingFragmentBinding?.pageEmpty?.root?.hide()
                    talkReadingFragmentBinding?.pageLoading?.root?.hide()
                }
                is ViewState.Success -> {
                    if(it.isEmpty && it.page == DEFAULT_INITIAL_PAGE) {
                        isLoadingInitialData = false
                        showPageEmpty()
                        talkReadingFragmentBinding?.talkReadingRecyclerView?.hide()
                    } else {
                        talkReadingFragmentBinding?.talkReadingRecyclerView?.show()
                        talkReadingFragmentBinding?.pageEmpty?.root?.hide()
                        talkReadingFragmentBinding?.addFloatingActionButton?.circleMainMenu?.show()
                    }
                    talkReadingFragmentBinding?.pageLoading?.root?.hide()
                    hideLoading()
                    hidePageError()
                }
            }
        })
    }

    private fun showBottomSheet() {
        context?.let { context ->
            val sortOptionsBottomSheet = viewModel.sortOptions.value?.let { TalkReadingSortBottomSheet.createInstance(context,it , this) }
            sortOptionsBottomSheet?.setShowListener {
                val headerMargin = 16.toPx()
                sortOptionsBottomSheet.bottomSheetWrapper.setPadding(0,0,0,0)
                (sortOptionsBottomSheet.bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(headerMargin,headerMargin,headerMargin,headerMargin)
            }
            this.childFragmentManager.let { sortOptionsBottomSheet?.show(it,"") }
        }
    }

    private fun initSortOptions() {
        viewModel.updateSortOptions(listOf(SortOption.SortByTime(), SortOption.SortByInformativeness()))
    }

    private fun getHeaderData() {
        viewModel.getDiscussionAggregate(productId, shopId)
    }

    private fun onSuccessCreateQuestion() {
        showSuccessToaster(getString(R.string.reading_create_question_toaster_success))
        resetSortOptions()
        unselectCategories()
        (viewModel.discussionAggregate.value as? Success)?.let {
            talkReadingFragmentBinding?.talkReadingHeader?.clearAllSort(
                    TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(
                            it.data.discussionAggregateResponse,
                            { showBottomSheet() },
                            this
                    ), this
            ) { showBottomSheet() }
        }
    }

    private fun onSuccessDeleteQuestion(questionID: String) {
        if(questionID.isNotEmpty()) {
            (adapter as? TalkReadingAdapter)?.removeQuestion(questionID)
        }
        showSuccessToaster(getString(R.string.delete_question_toaster_success))
        if(adapter.data.isEmpty()) {
            viewModel.setSuccess(true, DEFAULT_INITIAL_PAGE)
        }
    }

    private fun showSuccessToaster(message: String) {
        view?.let {
            Toaster.build(it,message,Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun showErrorToaster() {
        Toaster.toasterCustomCtaWidth = TOASTER_CTA_WIDTH
        view?.let {
            Toaster.build(talkReadingFragmentBinding?.talkReadingContainer ?: it, getString(R.string.reading_connection_error_toaster_message), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry)
            ) { loadData(currentPage) }. show() }
    }

    private fun updateSortHeader(sortOption: SortOption) {
        talkReadingFragmentBinding?.talkReadingHeader?.updateSelectedSort(TalkReadingMapper.mapSelectedSortToSortFilterItem(sortOption))
    }

    private fun showContainer() {
        talkReadingFragmentBinding?.talkReadingContainer?.show()
        initFab()
    }

    private fun getDataFromArguments() {
        arguments?.let {
            productId = it.getString(PARAM_PRODUCT_ID, "")
            shopId = it.getString(PARAM_SHOP_ID, "")
            isVariantSelected = it.getBoolean(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED)
            availableVariants = it.getString(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, "0")
        }
    }

    private fun goToWriteActivity(eventAction: String) {
        TalkReadingTracking.eventClickWrite(viewModel.getUserId(), productId, eventAction, isVariantSelected, availableVariants)
        val intent = RouteManager.getIntent(context, Uri.parse(
                ApplinkConstInternalGlobal.ADD_TALK)
                .buildUpon()
                .appendQueryParameter(PARAM_PRODUCT_ID, productId)
                .appendQueryParameter(TalkConstants.PARAM_APPLINK_IS_VARIANT_SELECTED, isVariantSelected.toString())
                .appendQueryParameter(TalkConstants.PARAM_APPLINK_AVAILABLE_VARIANT, availableVariants)
                .build().toString())
        startActivity(intent)
    }

    private fun goToReplyActivity(questionID: String) {
        val intent = RouteManager.getIntent(
                context,
                Uri.parse(UriUtil.buildUri(ApplinkConstInternalGlobal.TALK_REPLY, questionID))
                        .buildUpon()
                        .appendQueryParameter(PARAM_SHOP_ID, shopId)
                        .build().toString()
        )
        startActivityForResult(intent, TALK_REPLY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToLoginActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE)
    }

    private fun getDiscussionData(page: Int = DEFAULT_INITIAL_PAGE, withDelay: Boolean = false, isRefresh: Boolean = false) {
        val selectedSort = TalkReadingMapper.mapSelectedSortToString(viewModel.sortOptions.value?.first { it.isSelected })
        val selectedCategories = getSelectedCategories()
        viewModel.getDiscussionData(productId, shopId, page, DEFAULT_DISCUSSION_DATA_LIMIT, selectedSort, selectedCategories, withDelay, isRefresh)
    }

    private fun renderDiscussionData(discussionData: List<TalkReadingUiModel>, hasNextPage: Boolean) {
        renderList(discussionData, hasNextPage)
        if(!hasNextPage) {
            (adapter as? TalkReadingAdapter)?.addEmptySpace()
        }
    }

    private fun selectUnselectCategory(categoryName: String, isSelected: Boolean) {
        viewModel.updateSelectedCategory(categoryName, isSelected)
    }

    private fun initFilterCategories(filterCategories: List<TalkReadingCategory>) {
        viewModel.updateCategories(filterCategories)
    }

    private fun updateLastAction(talkLastAction: TalkLastAction) {
        viewModel.updateLastAction(talkLastAction)
    }

    private fun initFab() {
        talkReadingFragmentBinding?.addFloatingActionButton?.apply {
            type = FloatingButtonUnify.BASIC
            color = FloatingButtonUnify.COLOR_GREEN
            circleMainMenu.setOnClickListener {
                if(viewModel.isUserLoggedIn()) {
                    goToWriteActivity(EVENT_ACTION_CREATE_NEW_QUESTION)
                } else {
                    updateLastAction(TalkGoToWrite)
                    goToLoginActivity()
                }
            }
        }
        talkReadingFragmentBinding?.addFloatingActionButton?.addItem(ArrayList())
    }

    private fun unselectCategories() {
        isLoadingInitialData = false
        viewModel.unselectAllCategories()
    }

    private fun resetSortOptions() {
        isLoadingInitialData = true
        viewModel.resetSortOptions()
    }

    private fun getSelectedCategories(): String {
        return viewModel.filterCategories.value?.filter { it.isSelected }?.joinToString { it.categoryName } ?: ""
    }

    private fun getSelectedCategoryDisplayName(): String {
        return viewModel.filterCategories.value?.filter { it.isSelected }?.joinToString(separator = ",") { it.displayName } ?: ""
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                supportActionBar?.hide()
                setSupportActionBar(talkReadingFragmentBinding?.headerTalkReading)
                talkReadingFragmentBinding?.headerTalkReading?.title = getString(R.string.title_talk_discuss)
            }
        }
    }

}
