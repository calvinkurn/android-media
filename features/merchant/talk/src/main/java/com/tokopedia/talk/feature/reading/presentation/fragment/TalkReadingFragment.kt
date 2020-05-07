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
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GENERAL_SETTING
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringContract
import com.tokopedia.talk.common.analytics.TalkPerformanceMonitoringListener
import com.tokopedia.talk.common.constants.TalkConstants.PRODUCT_ID
import com.tokopedia.talk.common.constants.TalkConstants.SHOP_ID
import com.tokopedia.talk.feature.reading.analytics.TalkReadingTracking
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.*
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapter
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
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
import com.tokopedia.user.session.UserSessionInterface
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
        const val LOGIN_ACTIVITY_REQUEST_CODE = 204
        const val TALK_READING_EMPTY_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/talk_reading_empty_state.png"

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
    @Inject
    lateinit var userSession: UserSessionInterface

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
        TalkReadingTracking.eventClickSort(sortOption.id.name.toLowerCase(), userSession.userId, productId)
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
        val isSelected = chipType == ChipsUnify.TYPE_SELECTED
        if(isSelected) {
            TalkReadingTracking.eventClickFilter(categoryName, userSession.userId, productId)
        }
        selectUnselectCategory(categoryName, isSelected)
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

    override fun onThreadClicked(questionID: String) {
        if(userSession.isLoggedIn) {
            goToReplyActivity(questionID)
            return
        }
        updateLastAction(TalkGoToReply(questionID))
        goToLoginActivity()
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
            LOGIN_ACTIVITY_REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                when (viewModel.talkLastAction.value) {
                    is TalkGoToReply -> {
                        (viewModel.talkLastAction.value as? TalkGoToReply)?.questionId?.let {
                            goToReplyActivity(it)
                        }
                    }
                    is TalkGoToWrite -> {
                        goToWriteActivity()
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
        reading_image_error.loadImage(TALK_READING_EMPTY_IMAGE_URL)
        addFloatingActionButton.hide()
        pageEmpty.visibility = View.VISIBLE
        readingEmptyAskButton.setOnClickListener {
            if(userSession.isLoggedIn) {
                goToWriteActivity()
            } else {
                updateLastAction(TalkGoToWrite)
                goToLoginActivity()
            }
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
                        isLoadingInitialData = false
                    } else {
                        hidePageEmpty()
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
                showPageLoading()
            }
        })
    }

    private fun observeFilterCategories() {
        viewModel.filterCategories.observe(this, Observer {
            if(!isLoadingInitialData) {
                loadInitialData()
                showPageLoading()
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
        if(userSession.isLoggedIn) {

        }
        val intent = context?.let { AddTalkActivity.createIntent(it, productId) }
        startActivityForResult(intent, TALK_WRITE_ACTIVITY_REQUEST_CODE)
    }

    private fun goToReplyActivity(questionID: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.TALK_REPLY, questionID, productId, shopId)
        startActivityForResult(intent, TALK_REPLY_ACTIVITY_REQUEST_CODE)
    }

    private fun goToProfileActivity(userId: String) {
        val intent = RouteManager.getIntent(context, ApplinkConst.PROFILE, userId)
        startActivity(intent)
    }

    private fun goToLoginActivity() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST_CODE)
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

    private fun updateLastAction(talkLastAction: TalkLastAction) {
        viewModel.updateLastAction(talkLastAction)
    }

    private fun initFab() {
        addFloatingActionButton.setOnClickListener {
            if(userSession.isLoggedIn) {
                goToWriteActivity()
            } else {
                updateLastAction(TalkGoToWrite)
                goToLoginActivity()
            }
        }
    }

    private fun unselectCategories() {
        viewModel.unselectAllCategories()
    }

    private fun resetSortOptions() {
        viewModel.resetSortOptions()
    }

}