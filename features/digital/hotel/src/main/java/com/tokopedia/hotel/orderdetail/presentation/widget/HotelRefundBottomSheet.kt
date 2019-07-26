package com.tokopedia.hotel.orderdetail.presentation.widget

import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.data.model.HotelTransportDetail
import com.tokopedia.hotel.orderdetail.presentation.adapter.CancellationAdapter

/**
 * @author by jessica on 15/05/19
 */

class HotelRefundBottomSheet: BottomSheets(){

    private lateinit var recyclerView: RecyclerView
    lateinit var cancellationPolicies: List<HotelTransportDetail.Cancellation.CancellationPolicy>

    lateinit var adapter: CancellationAdapter

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_order_detail_refund

    override fun initView(view: View) {
        view.minimumHeight = 600
        recyclerView = view.findViewById(R.id.recycler_view)

        adapter = CancellationAdapter(cancellationPolicies)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        updateHeight()
    }

    override fun title(): String = getString(R.string.hotel_order_detail_refund_sheet_title)
}