package com.tokopedia.purchase_platform.common.feature.tokopointstnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class TokoPointsTncBottomsheet : BottomSheetUnify() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TokoPointsTncAdapter
    private lateinit var data: ArrayList<TokoPointsTncUiModel>

    fun setBottomsheetData(title: String, tncDetails: ArrayList<TokoPointsTncUiModel>) {
        setTitle(title)
        data = tncDetails
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setChild(LayoutInflater.from(context).inflate(R.layout.bottom_sheet_tokopoints_tnc, null, false))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view_tokopoint_tnc)

        adapter = TokoPointsTncAdapter()
        adapter.data = data
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        adapter.notifyDataSetChanged()
    }

}