package com.tokopedia.talk.feature.reading.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GENERAL_SETTING
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants.PRODUCT_ID
import com.tokopedia.talk.common.constants.TalkConstants.SHOP_ID
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.data.model.TalkReadingCategory
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapter
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingShimmerModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnCategoryModifiedListener
import com.tokopedia.talk.feature.reading.presentation.widget.OnFinishedSelectSortListener
import com.tokopedia.talk.feature.reading.presentation.widget.ThreadListener
import com.tokopedia.talk.feature.reading.presentation.widget.TalkReadingSortBottomSheet
import com.tokopedia.talk_old.R
import com.tokopedia.talk_old.addtalk.view.activity.AddTalkActivity
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import kotlinx.android.synthetic.main.partial_talk_reading_empty.*
import javax.inject.Inject

class TalkReadingFragment : BaseListFragment<TalkReadingUiModel,
        TalkReadingAdapterTypeFactory>(), HasComponent<TalkReadingComponent>,
        OnFinishedSelectSortListener, OnCategoryModifiedListener,
        ThreadListener, TalkPerformanceMonitoringContract {

    companion object {
        const val DEFAULT_DISCUSSION_DATA_LIMIT = 10
        const val DEFAULT_INITIAL_PAGE = 1
        const val DONT_LOAD_INITAL_DATA = false
        const val TALK_REPLY_ACTIVITY_REQUEST_CODE = 202
        const val TALK_WRITE_ACTIVITY_REQUEST_CODE = 203

        @JvmStatic
        fun createNewInstance(productId: String, shopId: String): TalkReadingFragment =
            TalkReadingFragment().apply {
                arguments = Bundle()
                arguments?.putString(PRODUCT_ID, productId)
                arguments?.putString(SHOP_ID, shopId)
            }
    }

    @Inject
    lateinit var viewModel: TalkReadingViewModel

    private var productId: String = ""
    private var shopId: String = ""
    private var talkPerformanceMonitoringListener: TalkPerformanceMonitoringListener? = null

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        return TalkReadingAdapterTypeFactory(this)
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkReadingRecyclerView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        stopPreparePerfomancePageMonitoring()
        startNetworkRequestPerformanceMonitoring()
        getDataFromArguments()
        observeProductHeader()
        observeSortOptions()
        observeDiscussionData()
        observeFilterCategories()
        showPageLoading()
        getHeaderData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenName(): String {
        return ""
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
        viewModel.updateSelectedSort(sortOption)
    }

    override fun callInitialLoadAutomatically(): Boolean {
        return DONT_LOAD_INITAL_DATA
    }

    override fun onSwipeRefresh() {
        isLoadingInitialData = true
        swipeToRefresh.isRefreshing = true
        clearAllData()
        showPageLoading()
        loadInitialData()
    }

    override fun onCategorySelected(categoryName: String, chipType: String) {
        selectUnselectCategory(categoryName, chipType == ChipsUnify.TYPE_SELECTED)
    }

    override fun onCategoriesCleared() {
        unselectCategories()
        resetSortOptions()
    }

    override fun getSwipeRefreshLayout(view: View?): SwipeRefreshLayout? {
        return readingSwipeToRefresh
    }

    override fun createAdapterInstance(): BaseListAdapter<TalkReadingUiModel, TalkReadingAdapterTypeFactory> {
        return TalkReadingAdapter(adapterTypeFactory)
    }

    override fun getLoadingModel(): LoadingModel {
        return TalkReadingShimmerModel()
    }

    override fun onThreadClicked(questionID: String) {
        goToReplyActivity(questionID)
    }

    override fun onUserDetailsClicked(userId: String) {
        goToProfileActivity(userId)
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
            TALK_REPLY_ACTIVITY_REQUEST_CODE -> if(resultCode == Activity.RESULT_OK) {
                onSuccessDeleteQuestion()
            }
            TALK_WRITE_ACTIVITY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                onSuccessCreateQuestion()
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
        talkReadingRecyclerView.post {
            talkPerformanceMonitoringListener?.let {
                it.stopRenderPerformanceMonitoring()
                it.stopPerformanceMonitoring()
            }
        }
    }

    override fun castContextToTalkPerformanceMonitoringListener(context: Context): TalkPerformanceMonitoringListener? {
        return if(context is TalkPerformanceMonitoringListener) {
            context
        } else {
            null
        }
    }

    private fun showPageLoading() {
        pageLoading.visibility = View.VISIBLE
        hidePageEmpty()
    }

    private fun hidePageLoading() {
        pageLoading.visibility = View.GONE
    }

    private fun showPageEmpty() {
        addFloatingActionButton.hide()
        pageEmpty.visibility = View.VISIBLE
        readingEmptyAskButton.setOnClickListener {
            goToWriteActivity()
        }
    }

    private fun hidePageEmpty() {
        pageEmpty.visibility = View.GONE
    }

    private fun showPageError() {
        pageError.visibility = View.VISIBLE
        pageError.readingConnectionErrorRetryButton.setOnClickListener {
            getHeaderData()
            hidePageError()
            showPageLoading()
        }
        pageError.readingConnectionErrorGoToSettingsButton.setOnClickListener {
            RouteManager.route(context, GENERAL_SETTING)
        }
    }

    private fun hidePageError() {
        pageError.visibility = View.GONE
        addFloatingActionButton.show()
    }

    private fun bindHeader(talkReadingHeaderModel: TalkReadingHeaderModel) {
        talkReadingHeader.bind(talkReadingHeaderModel, this)
    }

    private fun observeProductHeader() {
        viewModel.discussionAggregate.observe(this,  Observer {
            when (it) {
                is Success -> {
                    bindHeader(
                            TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(
                                    it.data.discussionAggregateResponse,
                                    { showBottomSheet() },
                                    this
                            ))
                    initFilterCategories(TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingCategories(it.data))
                    initSortOptions()
                    showContainer()
                }
                is Fail -> {
                    showPageError()
                    hidePageLoading()
                }
            }
        })
    }

    private fun observeDiscussionData() {
        viewModel.discussionData.observe(this, Observer {
            when (it) {
                is Success -> {
                    if(it.data.discussionData.question.isEmpty()) {
                        showPageEmpty()
                    } else {
                        stopNetworkRequestPerformanceMonitoring()
                        startRenderPerformanceMonitoring()
                        renderDiscussionData(
                                TalkReadingMapper.mapDiscussionDataResponseToTalkReadingUiModel(it.data.discussionData),
                                it.data.discussionData.hasNext)
                    }

                }
                is Fail -> {
                    hidePageLoading()
                    if(currentPage > 0) {
                        showErrorToaster()
                    } else {
                        showPageError()
                    }
                }
            }
            hidePageLoading()
        })
    }

    private fun observeSortOptions() {
        viewModel.sortOptions.observe(this, Observer { sortOptions ->
            updateSortHeader(sortOptions.first { it.isSelected })
            if(!isLoadingInitialData) {
                loadInitialData()
            }
        })
    }

    private fun observeFilterCategories() {
        viewModel.filterCategories.observe(this, Observer {
            if(!isLoadingInitialData) {
                loadInitialData()
            }
        })
    }

    private fun showBottomSheet() {
        val sortOptionsBottomSheet = context?.let { context ->
            viewModel.sortOptions.value?.let {
                TalkReadingSortBottomSheet.createInstance(context,it , this) }
        }
        this.childFragmentManager.let { sortOptionsBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun initSortOptions() {
        viewModel.updateSortOptions(listOf(SortOption.SortByInformativeness(), SortOption.SortByTime(), SortOption.SortByLike()))
    }

    private fun getHeaderData() {
        viewModel.getDiscussionAggregate(productId, shopId)
    }

    private fun onSuccessCreateQuestion() {
        showSuccessToaster(getString(R.string.reading_create_question_toaster_success))
        loadInitialData()
    }

    private fun onSuccessDeleteQuestion() {
        showSuccessToaster(getString(R.string.delete_question_toaster_success))
        loadInitialData()
    }

    private fun showSuccessToaster(message: String) {
        view?.let { Toaster.make(it, message, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, getString(R.string.talk_ok)) }
    }

    private fun showErrorToaster() {
        view?.let { Toaster.make(it, getString(R.string.reading_connection_error_toaster_message), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry)) }
    }

    private fun updateSortHeader(sortOption: SortOption) {
        talkReadingHeader.updateSelectedSort(TalkReadingMapper.mapSelectedSortToSortFilterItem(sortOption))
    }

    private fun showContainer() {
        talkReadingContainer.visibility = View.VISIBLE
        initFab()
    }

    private fun getDataFromArguments() {
        arguments?.let {
            productId = it.getString(PRODUCT_ID, "")
            shopId = it.getString(SHOP_ID, "")
        }
    }

    private fun goToWriteActivity() {
        val intent = context?.let { AddTalkActivity.createIntent(it, productId) }
        startActivity(intent)
    }

    private fun goToReplyActivity(questionID: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.TALK_REPLY, questionID, productId, shopId)
        startActivityForResult(intent, TALK_REPLY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToProfileActivity(userId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, userId)
        startActivity(intent)
    }

    private fun getDiscussionData(page: Int = DEFAULT_INITIAL_PAGE) {
        val selectedSort = TalkReadingMapper.mapSelectedSortToString(viewModel.sortOptions.value?.first { it.isSelected })
        val selectedCategories = viewModel.filterCategories.value?.filter { it.isSelected }?.joinToString { it.categoryName } ?: ""
        viewModel.getDiscussionData(productId, shopId, page, DEFAULT_DISCUSSION_DATA_LIMIT, selectedSort, selectedCategories)
    }

    private fun renderDiscussionData(discussionData: List<TalkReadingUiModel>, hasNextPage: Boolean) {
        renderList(discussionData, hasNextPage)
    }

    private fun selectUnselectCategory(categoryName: String, isSelected: Boolean) {
        viewModel.updateSelectedCategory(categoryName, isSelected)
    }

    private fun initFilterCategories(filterCategories: List<TalkReadingCategory>) {
        viewModel.updateCategories(filterCategories)
    }

    private fun initFab() {
        addFloatingActionButton.setOnClickListener {
            goToWriteActivity()
        }
    }

    private fun unselectCategories() {
        viewModel.unselectAllCategories()
    }

    private fun resetSortOptions() {
        viewModel.resetSortOptions()
    }

}