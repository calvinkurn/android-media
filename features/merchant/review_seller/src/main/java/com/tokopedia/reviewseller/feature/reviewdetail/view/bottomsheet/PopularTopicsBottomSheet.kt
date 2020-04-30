package com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.util.TopicItemDecoration
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by Yehezkiel on 27/04/20
 */
class PopularTopicsBottomSheet(private val mActivity: FragmentActivity?,
                               val listener: (List<SortFilterItemWrapper>, List<SortItemUiModel>) -> Unit) :
        BottomSheetUnify(), TopicSortFilterListener {

    companion object {
        const val BOTTOM_SHEET_TITLE = "Filter"
        private val ACTION_TITLE = "Reset"
    }

    private var rvTopicFilter: RecyclerView? = null
    private var rvSortFilter: RecyclerView? = null

    private val sortAdapter by lazy { SortListAdapter(this) }
    private val topicAdapter by lazy { TopicListAdapter(this) }

    var filterTopicData: List<SortFilterItemWrapper> = mutableListOf()
    var sortTopicData: List<SortItemUiModel> = mutableListOf()

    init {
        val contentView = View.inflate(mActivity, R.layout.dialog_popular_topics, null)
        rvTopicFilter = contentView.findViewById(R.id.rvTopicFilter)
        rvSortFilter = contentView.findViewById(R.id.rvSortFilter)
        isDragable = true
        isFullpage = true
        isHideable = true
        setTitle(BOTTOM_SHEET_TITLE)
        setChild(contentView)
        setAction(ACTION_TITLE) {
            resetFilterClicked()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initLayoutManager()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    fun setFilterTopicListData(data: List<SortFilterItemWrapper>) {
        this.filterTopicData = data
    }

    fun setSortTopic(data: List<SortItemUiModel>) {
        this.sortTopicData = data
    }

    fun showDialog() {
        mActivity?.supportFragmentManager?.run {
            show(this, BOTTOM_SHEET_TITLE)
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

        sortAdapter.setSortFilter(sortTopicData)
        topicAdapter.setTopicFilter(filterTopicData)
        rvTopicFilter?.adapter = topicAdapter
        rvSortFilter?.adapter = sortAdapter
    }

    override fun dismiss() {
        super.dismiss()
        topicAdapter.sortFilterList?.toList()?.let { topic ->
            sortAdapter.sortFilterListUiModel?.let { sort ->
                listener.invoke(topic, sort)
            }
        }

    }

    private fun resetFilterClicked() {
        topicAdapter.resetSortFilter()
        sortAdapter.resetSortFilter()
    }

    override fun onTopicClicked(chipType: String, adapterPosition: Int) {
        val isSelected = chipType == ChipsUnify.TYPE_SELECTED
        topicAdapter.updateTopicFilter(isSelected, adapterPosition)
    }

    override fun onSortClicked(chipType: String, adapterPosition: Int) {
        val isSelected = chipType == ChipsUnify.TYPE_SELECTED
        sortAdapter.updatedSortFilter(adapterPosition)
    }
}