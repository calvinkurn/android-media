package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.statistic.PostStatisticAdapter
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel
import com.tokopedia.kotlin.extensions.view.warn
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 2019-11-21
 */
class PostStatisticBottomSheet : BottomSheetUnify() {

    companion object {

        fun newInstance(context: Context): PostStatisticBottomSheet {
            return PostStatisticBottomSheet().apply {
                arguments = Bundle.EMPTY
                val view = LayoutInflater.from(context).inflate(R.layout.bottomsheet_post_statistic, null)
                setChild(view)
                initView(view)
            }
        }
    }

    private lateinit var rvStatistic: RecyclerView
    private val statisticAdapter = PostStatisticAdapter()

    fun setStatisticModelList(modelList: List<PostStatisticUiModel>) {
        statisticAdapter.setItemsAndAnimateChanges(modelList)
    }

    fun show(fragmentManager: FragmentManager,
             activityId: String,
             title: String,
             productIds: List<String>,
             listener: Listener) {
        if (tag != activityId) resetData()
        listener.onGetPostStatisticModelList(activityId, productIds)
        setTitle(title)
        show(fragmentManager, activityId)
    }

    private fun initView(view: View) {
        rvStatistic = view.findViewById(R.id.rv_statistic)

        setupList()
    }

    private fun setupList() {
        rvStatistic.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = statisticAdapter
        }
    }

    private fun resetData() {

    }

    interface Listener {
        fun onGetPostStatisticModelList(activityId: String, productIds: List<String>)
    }
}