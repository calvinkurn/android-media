package com.tokopedia.topads.common.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.common.view.adapter.tips.TipsListAdapter
import com.tokopedia.topads.common.view.adapter.tips.viewholder.TipsUiSortViewHolder
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

class TipsListSheet : BottomSheetUnify() {
    private var tipsList: ArrayList<TipsUiModel> = ArrayList()
    private lateinit var tipsRecyclerView: RecyclerView
    private lateinit var tipsAdapter: TipsListAdapter

    init {
        clearContentPadding = true
        showKnob = true
        isDragable = true
        showHeader = false
        isHideable = true
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context, tipsList: ArrayList<TipsUiModel>, sortItemClick: TipsUiSortViewHolder.OnUiSortItemClick? = null): TipsListSheet {
            return TipsListSheet().apply {
                this.tipsList = tipsList
                this.tipsAdapter = TipsListAdapter(sortItemClick)
                val childView = LayoutInflater.from(context).inflate(R.layout.topads_common_tips_sheet_layout, null)
                setChild(childView)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        tipsRecyclerView = view.findViewById(R.id.tipsRecyclerView)
        tipsRecyclerView.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = tipsAdapter
            tipsAdapter.setTipsItems(tipsList)
            addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        }
    }

    fun notifyDataSetChanged() {
        tipsAdapter.setTipsItems(tipsList)
    }

    fun getTipsList(): ArrayList<TipsUiModel> = tipsList
}