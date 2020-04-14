package com.tokopedia.talk.feature.reading.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.talk.common.di.TalkComponent
import com.tokopedia.talk.feature.reading.data.mapper.TalkReadingMapper
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.di.DaggerTalkReadingComponent
import com.tokopedia.talk.feature.reading.di.TalkReadingComponent
import com.tokopedia.talk.feature.reading.presentation.adapter.TalkReadingAdapterTypeFactory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.viewmodel.TalkReadingViewModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnFinishedListener
import com.tokopedia.talk.feature.reading.presentation.widget.TalkReadingSortBottomSheet
import com.tokopedia.talk_old.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_reading.*
import javax.inject.Inject

class TalkReadingFragment : BaseListFragment<TalkReadingUiModel,
        TalkReadingAdapterTypeFactory>(), HasComponent<TalkReadingComponent>,
        OnFinishedListener {

    companion object {

        const val PRODUCT_ID = "product_id"

        @JvmStatic
        fun createNewInstance(productId: Int): TalkReadingFragment =
            TalkReadingFragment().apply {
                arguments?.putInt(PRODUCT_ID, productId)
            }
    }

    @Inject
    lateinit var viewModel: TalkReadingViewModel

    private var productId: Int = 0
    private var sortOptions: List<SortOption> = listOf()
    private var sortOptionsBottomSheet: BottomSheetUnify? = null

    override fun getAdapterTypeFactory(): TalkReadingAdapterTypeFactory {
        return TalkReadingAdapterTypeFactory()
    }

    override fun getRecyclerView(view: View?): RecyclerView {
        return talkReadingRecyclerView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getProductId()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_reading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeProductHeader()
        showPageLoading()
        initSortOptionsBottomSheet()
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
        //Get Data

    }

    private fun getProductId() {
        arguments?.let {
            productId = it.getInt(PRODUCT_ID)
        }
    }

    private fun showPageLoading() {
        pageLoading.visibility = View.VISIBLE
        pageLoading.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showPageError() {
        pageError.visibility = View.VISIBLE
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
                            TalkReadingMapper.mapDiscussionAggregateResponseToTalkReadingHeaderModel(it.data) {
                                showBottomSheet()
                            }
                    )
                }
                is Fail -> {
                    showPageError()
                }
            }
        })
    }

    private fun showBottomSheet() {
        this.childFragmentManager.let { sortOptionsBottomSheet?.show(it,"BottomSheetTag") }
    }

    private fun initHeader(talkReadingHeaderModel: TalkReadingHeaderModel) {

    }

    private fun initSortOptionsBottomSheet() {
        sortOptions = listOf(SortOption.SortByInformativeness(), SortOption.SortByTime(), SortOption.SortByLike())
        sortOptionsBottomSheet = context?.let { TalkReadingSortBottomSheet.createInstance(it, sortOptions, this) }
    }
}