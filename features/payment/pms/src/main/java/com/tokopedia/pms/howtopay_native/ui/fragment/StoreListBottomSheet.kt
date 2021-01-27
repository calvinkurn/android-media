package com.tokopedia.pms.howtopay_native.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.data.model.StoreList
import com.tokopedia.pms.howtopay_native.ui.adapter.StoreListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp

class StoreListBottomSheet : BottomSheetUnify() {

    private var childView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.pms_hwp_store_list))
        childView = LayoutInflater.from(context).inflate(R.layout.pms_hwp_store_list,
                null, false)
        setChild(childView)
        addStoreData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isDragable = true
        isHideable = true
        customPeekHeight = getScreenHeight().toDp()
    }

    private fun addStoreData() {
        val recyclerView = childView?.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = StoreListAdapter(StoreList.getStoreList())
        recyclerView?.adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "StoreListBottomSheet"
        fun showStoreList(supportFragmentManager: FragmentManager) {
            StoreListBottomSheet().show(supportFragmentManager, TAG)
        }
    }
}
