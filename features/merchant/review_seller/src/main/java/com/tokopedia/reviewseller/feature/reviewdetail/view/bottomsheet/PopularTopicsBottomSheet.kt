package com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.ReviewSellerComponentBuilder
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.DaggerReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.di.component.ReviewProductDetailComponent
import com.tokopedia.reviewseller.feature.reviewdetail.di.module.ReviewProductDetailModule
import com.tokopedia.reviewseller.feature.reviewdetail.util.TopicItemDecoration
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder.SortListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.reviewseller.feature.reviewdetail.view.viewmodel.ProductReviewDetailViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import javax.inject.Inject

/**
 * Created by Yehezkiel on 27/04/20
 */
class PopularTopicsBottomSheet(private val mActivity: FragmentActivity?, private val reviewDetailAdapter: SellerReviewDetailAdapter, val title:String, val listener: (List<String>) -> Unit):
        BottomSheetUnify(),
        HasComponent<ReviewProductDetailComponent>, TopicSortFilterListener {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter"
        const val ACTION_TITLE = "Reset"
    }

    @Inject
    lateinit var productReviewDetailViewModel: ProductReviewDetailViewModel

    private var rvTopicFilter: RecyclerView? = null
    private var rvSortFilter: RecyclerView? = null

    private val sortAdapter by lazy { SortListAdapter(this) }
    private val topicAdapter by lazy { TopicListAdapter(this) }

    init {
        val contentView = View.inflate(mActivity, R.layout.dialog_popular_topics, null)
        rvTopicFilter = contentView.findViewById(R.id.rvTopicFilter)
        rvSortFilter = contentView.findViewById(R.id.rvSortFilter)
        isDragable = true
        setTitle(BOTTOM_SHEET_TITLE)
        setChild(contentView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initLayoutManager()
        super.onCreate(savedInstanceState)
        component?.inject(this)
    }

    override fun getComponent(): ReviewProductDetailComponent? {
        return mActivity?.run {
            DaggerReviewProductDetailComponent
                    .builder()
                    .reviewSellerComponent(ReviewSellerComponentBuilder.getComponent(application))
                    .reviewProductDetailModule(ReviewProductDetailModule())
                    .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, title)
        }
    }

    private fun initLayoutManager() {
        val layoutManagerTopic = ChipsLayoutManager.newBuilder(mActivity)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        val layoutManagerSort = ChipsLayoutManager.newBuilder(mActivity)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()

        rvTopicFilter?.addItemDecoration(TopicItemDecoration())
        rvSortFilter?.addItemDecoration(TopicItemDecoration())

        rvTopicFilter?.layoutManager = layoutManagerTopic
        rvSortFilter?.layoutManager = layoutManagerSort

        rvTopicFilter?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
        rvSortFilter?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
    }

    private fun initAdapter() {
        val getTopicsFilterFromAdapter =
                reviewDetailAdapter.list.filterIsInstance<TopicUiModel>().firstOrNull()

        sortAdapter.setSortFilter(productReviewDetailViewModel.sortTopicData)
        getTopicsFilterFromAdapter?.sortFilterItemList?.let { topicAdapter.setTopicFilter(it) }
        rvTopicFilter?.adapter = topicAdapter
        rvSortFilter?.adapter = sortAdapter
    }

    override fun dismiss() {
        super.dismiss()
        listener.invoke(listOf())
    }

    override fun onTopicClicked(item: SortFilterItemWrapper, adapterPosition: Int) {
        val getTopicsFilterFromAdapter = reviewDetailAdapter.list.filterIsInstance<TopicUiModel>().firstOrNull()
        val updatedState = item.sortFilterItem?.type == ChipsUnify.TYPE_SELECTED
        reviewDetailAdapter.updateFilterTopic(adapterPosition, item.sortFilterItem?.title.toString(), updatedState, getTopicsFilterFromAdapter)
        topicAdapter.updateTopicFilter(adapterPosition, updatedState)
        productReviewDetailViewModel.setFilterTopicDataText(getTopicsFilterFromAdapter?.sortFilterItemList)
    }

    override fun onSortClicked(itemUiModel: SortItemUiModel, adapterPosition: Int) {

    }
}