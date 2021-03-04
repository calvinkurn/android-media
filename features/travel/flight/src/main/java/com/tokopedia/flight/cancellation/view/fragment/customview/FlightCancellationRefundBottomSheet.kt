package com.tokopedia.flight.cancellation.view.fragment.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationRefundAdapter
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationRefund
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.*

/**
 * @author by jessica on 12/11/20
 */

class FlightCancellationRefundBottomSheet : BottomSheetUnify() {

    init {
        isFullpage = false
        setTitle(getString(com.tokopedia.flight.orderlist.R.string.flight_order_status_refund_label))
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.partial_flight_cancellation_refund_description, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View) {
        with(view) {
            val detailsRecyclerView = view.findViewById<RecyclerView>(R.id.rv_details)
            val titles = resources.getStringArray(R.array.flight_cancellation_refund_title)
            val subtitles = resources.getStringArray(R.array.flight_cancellation_refund_subtitle)
            val descriptions: MutableList<FlightCancellationRefund> = ArrayList()
            for (i in titles.indices) {
                val description = FlightCancellationRefund()
                description.title = titles[i]
                description.subtitle = subtitles[i]
                descriptions.add(description)
            }
            val adapter = FlightCancellationRefundAdapter()
            adapter.setDescriptions(descriptions)
            detailsRecyclerView.layoutManager = LinearLayoutManager(context)
            detailsRecyclerView.adapter = adapter
        }
    }
}