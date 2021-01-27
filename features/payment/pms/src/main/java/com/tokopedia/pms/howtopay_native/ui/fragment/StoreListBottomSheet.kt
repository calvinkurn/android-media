package com.tokopedia.pms.howtopay_native.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.data.model.StoreList
import com.tokopedia.pms.howtopay_native.ui.adapter.StoreListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx

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
        bottomSheetWrapper.setPadding(0, bottomSheetWrapper.paddingTop, 0, bottomSheetWrapper.paddingBottom)
        bottomSheetAction.setMargin(marginRight = 16.toPx())
        bottomSheetClose.setMargin(marginLeft = 16.toPx(), marginTop = 4.toPx(), marginRight = 12.toPx())
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



internal fun View.setMargin(marginLeft: Int = -1, marginTop: Int = -1, marginRight: Int = -1, marginBottom: Int = -1) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    val actualMarginLeft = if (marginLeft == -1) layoutParams.leftMargin else marginLeft
    val actualMarginTop = if (marginTop == -1) layoutParams.topMargin else marginTop
    val actualMarginRight = if (marginRight == -1) layoutParams.rightMargin else marginRight
    val actualMarginBottom = if (marginBottom == -1) layoutParams.bottomMargin else marginBottom

    layoutParams.setMargins(actualMarginLeft, actualMarginTop, actualMarginRight, actualMarginBottom)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        layoutParams.marginStart = actualMarginLeft
        layoutParams.marginEnd = actualMarginRight
    }
}