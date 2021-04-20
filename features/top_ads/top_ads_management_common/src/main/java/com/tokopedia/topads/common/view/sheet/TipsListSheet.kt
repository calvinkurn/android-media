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
    private var tipsAdapter: TipsListAdapter = TipsListAdapter()
    private lateinit var itemDecoration: RecyclerView.ItemDecoration

    init {
        clearContentPadding = true
        showKnob = true
        isDragable = true
        showHeader = false
        isHideable = true
    }

    companion object {
        @JvmStatic
        fun newInstance(context: Context, tipsList: ArrayList<TipsUiModel>, itemDecoration: RecyclerView.ItemDecoration = SpaceItemDecoration(LinearLayoutManager.VERTICAL)): TipsListSheet {
            return TipsListSheet().apply {
                this.tipsList = tipsList
                this.itemDecoration = itemDecoration
                val childView = LayoutInflater.from(context).inflate(R.layout.topads_common_tips_sheet_layout, null)
                setChild(childView)
            }
        }
    }

    fun setOnUiSortItemClickListener(sortItemClick: TipsUiSortViewHolder.OnUiSortItemClick) {
        this.tipsAdapter.setOnUiSortItemClickListener(sortItemClick)
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
            addItemDecoration(itemDecoration)
        }
    }

    fun getTipsList(): ArrayList<TipsUiModel> = tipsList
}