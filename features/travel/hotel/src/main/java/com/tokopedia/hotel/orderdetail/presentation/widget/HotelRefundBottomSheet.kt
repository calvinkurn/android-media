package com.tokopedia.hotel.orderdetail.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.presentation.adapter.CancellationAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by jessica on 15/05/19
 */

class HotelRefundBottomSheet : BottomSheetUnify() {

    private lateinit var recyclerView: RecyclerView
    lateinit var cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>

    lateinit var adapter: CancellationAdapter

    init {
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_order_detail_refund, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        view.minimumHeight = 600
        recyclerView = view.findViewById(R.id.recycler_view)

        adapter = CancellationAdapter(cancellationPolicies)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        setTitle(getString(R.string.hotel_order_detail_refund_sheet_title))
    }
}