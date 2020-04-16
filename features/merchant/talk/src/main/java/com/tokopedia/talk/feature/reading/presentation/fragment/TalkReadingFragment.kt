package com.tokopedia.talk.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.GENERAL_SETTING
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnFinishedSelectSortListener
import com.tokopedia.talk.feature.reading.presentation.widget.TalkReadingSortBottomSheet
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import kotlinx.android.synthetic.main.partial_talk_connection_error.view.*
import javax.inject.Inject

class TalkReadingFragment : BaseListFragment<TalkReadingUiModel,
        TalkReadingAdapterTypeFactory>(), HasComponent<TalkReadingComponent>,
        OnFinishedSelectSortListener {

    companion object {

        const val PARAM_PRODUCT_ID = "productID"
        @JvmStatic
        fun createNewInstance(productId: Int): TalkReadingFragment =
            TalkReadingFragment().apply {
                arguments = Bundle()
                arguments?.putInt(PARAM_PRODUCT_ID, productId)
            }
    }

    @Inject
    lateinit var viewModel: TalkReadingViewModel

    private var productId: Int = 0
    private var sortOptionsBottomSheet: BottomSheetUnify? = null

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        return TalkReadingAdapterTypeFactory()
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkReadingRecyclerView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getProductIdFromBundle()
        observeProductHeader()
        observeSortOptions()
        showPageLoading()
        getHeaderData()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component.inject(this)
    }

    override fun onItemClicked(t: TalkReadingUiModel?) {
        // No Op
    }

    override fun loadData(page: Int) {
        // get data
    }

    override fun getComponent(): TalkReadingComponent {
        return DaggerTalkReadingComponent.builder().talkComponent(
                getComponent(TalkComponent::class.java))
                .build()
    }


    override fun onFinishChooseSort(sortOption: SortOption) {
        viewModel.updateSelectedSort(sortOption)
    }

    private fun showPageLoading() {
        pageLoading.visibility = View.VISIBLE
    }

    private fun hidePageLoading() {
        pageLoading.visibility = View.GONE
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
    }

    private fun bindHeader(talkReadingHeaderModel: TalkReadingHeaderModel) {
        talkReadingHeader.bind(talkReadingHeaderModel)
    }

    private fun observeProductHeader() {
        viewModel.discussionAggregate.observe(this,  Observer {
            when (it) {
                is Success -> {
                    bindHeader(
                            TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(it.data.discussionAggregateResponse) {
                                showBottomSheet()
                            }
                    )
                    initSortOptions()
                    hidePageLoading()
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
            when(it) {
                is Success -> {

                }
                is Fail -> {

                }
            }
        })
    }

    private fun observeSortOptions() {
        viewModel.sortOptions.observe(this, Observer { sortOptions ->
            val selectedSort = sortOptions.filter { it.isSelected }
            updateSortHeader(selectedSort.first())
            // get talk with selected sort
        })
    }

    private fun showBottomSheet() {
        sortOptionsBottomSheet = context?.let { context ->
            viewModel.sortOptions.value?.let {
                TalkReadingSortBottomSheet.createInstance(context,it , this) }
        }
        this.childFragmentManager.let { sortOptionsBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun initSortOptions() {
        viewModel.updateSortOptions(listOf(SortOption.SortByInformativeness(), SortOption.SortByTime(), SortOption.SortByLike()))
    }

    private fun getHeaderData() {
        viewModel.getDiscussionAggregate(productId)
    }

    private fun showErrorToaster() {
        view?.let { Toaster.make(it, getString(R.string.reading_connection_error_toaster_message), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_retry)) }
    }

    private fun updateSortHeader(sortOption: SortOption) {
        talkReadingHeader.updateSelectedSort(TalkReadingMapper.mapSelectedSortToSortFilterItem(sortOption))
    }

    private fun showContainer() {
        talkReadingContainer.visibility = View.VISIBLE
    }

    private fun getProductIdFromBundle() {
        arguments?.let {
            productId = it.getInt(PARAM_PRODUCT_ID)
        }
    }
}