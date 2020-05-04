package com.tokopedia.reviewseller.feature.reviewdetail.view.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SortListAdapter
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.TopicSortFilterListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortItemUiModel

class SortBottomSheet(private val mActivity: FragmentActivity?,
                      listenerSort: (List<SortFilterItemWrapper>, List<SortItemUiModel>) -> Unit) :
        BaseTopicsBottomSheet(mActivity, listenerSort), TopicSortFilterListener.Sort {

    init {
        setAction(ACTION_TITLE) {
            sortAdapter?.resetSortFilter()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortAdapter = SortListAdapter(this)
        setAdapter()
    }

    override fun setAdapter() {
        tvTopicTitle?.hide()
        rvTopicFilter?.hide()
        sortAdapter?.setSortFilter(sortTopicData)
        rvSortFilter?.adapter = sortAdapter
    }


    override fun setSortListData(items: List<SortItemUiModel>) {
        super.sortTopicData = items
    }

    override fun onSortClicked(chipType: String, adapterPosition: Int) {
        sortAdapter?.updatedSortFilter(adapterPosition)
    }
}