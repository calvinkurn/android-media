package com.tokopedia.review.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.util.TopicItemDecoration
import com.tokopedia.review.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.review.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.Typography

abstract class BaseTopicsBottomSheet(private val mActivity: FragmentActivity?,
                                     val listener: (List<SortFilterItemWrapper>, List<SortItemUiModel>) -> Unit): BottomSheetUnify() {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter"
    }

    protected val ACTION_TITLE = "Reset"
    protected var rvTopicFilter: RecyclerView? = null
    protected var rvSortFilter: RecyclerView? = null
    protected var tvTopicTitle: Typography? = null

    protected var sortAdapter: SortListAdapter? = null
    protected var topicAdapter: TopicListAdapter? = null

    init {
        val contentView = View.inflate(mActivity, R.layout.dialog_popular_topics, null)
        rvTopicFilter = contentView.findViewById(R.id.rvTopicFilter)
        rvSortFilter = contentView.findViewById(R.id.rvSortFilter)
        tvTopicTitle = contentView.findViewById(R.id.tvTopicTitle)
        isDragable = true
        isHideable = true
        showKnob = true
        setTitle(BOTTOM_SHEET_TITLE)
        setChild(contentView)
    }

    protected var filterTopicData: List<SortFilterItemWrapper> = mutableListOf()
    protected var sortTopicData: List<SortItemUiModel> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        initLayoutManager()
        super.onCreate(savedInstanceState)
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

    open fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, BOTTOM_SHEET_TITLE)
        }
    }

    override fun dismiss() {
        super.dismiss()
        topicAdapter?.sortFilterList?.toList()?.let { topic ->
            sortAdapter?.sortFilterListUiModel?.let { sort ->
                listener.invoke(topic, sort)
            }
        }
    }

    abstract fun setAdapter()

    open fun setTopicListData(items: List<SortFilterItemWrapper>) {
        filterTopicData = items
    }

    open fun setSortListData(items: List<SortItemUiModel>) {
        sortTopicData = items
    }
}