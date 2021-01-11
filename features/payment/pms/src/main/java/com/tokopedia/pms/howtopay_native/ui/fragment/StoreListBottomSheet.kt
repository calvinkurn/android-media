package com.tokopedia.pms.howtopay_native.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.data.model.StoreList
import com.tokopedia.pms.howtopay_native.ui.adapter.StoreListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

class StoreListBottomSheet : BottomSheetUnify() {

    private var childView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showCloseIcon = true
        isDragable= true
        setTitle("Daftar gerai")
        childView = LayoutInflater.from(context).inflate(R.layout.pms_hwp_store_list,
                null, false)
        setChild(childView)
        addStoreData()
    }

    private fun addStoreData() {
        val recyclerView = childView?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = StoreListAdapter(StoreList.getStoreList())
        recyclerView?.adapter?.notifyDataSetChanged()
    }
}