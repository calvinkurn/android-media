package com.tokopedia.feedcomponent.view.widget

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.view.adapter.statistic.PostStatisticAdapter
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by jegul on 2019-11-21
 */
class PostStatisticBottomSheet : BottomSheetUnify() {

    companion object {

        private const val ARGS_PRODUCT_ID_LIST = "product_ids"
        private const val ARGS_LIKE_COUNT = "like_count"
        private const val ARGS_COMMENT_COUNT = "comment_count"

        fun newInstance(context: Context,
                        title: String,
                        productIds: List<String>,
                        likeCount: Int,
                        commentCount: Int): PostStatisticBottomSheet {
            return PostStatisticBottomSheet().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARGS_PRODUCT_ID_LIST, ArrayList(productIds))
                    putInt(ARGS_LIKE_COUNT, likeCount)
                    putInt(ARGS_COMMENT_COUNT, commentCount)
                }

                setTitle(title)
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
}