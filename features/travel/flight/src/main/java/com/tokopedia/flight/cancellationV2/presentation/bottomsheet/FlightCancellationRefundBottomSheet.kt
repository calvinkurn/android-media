package com.tokopedia.flight.cancellationV2.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationV2.presentation.adapter.FlightCancellationRefundAdapter
import com.tokopedia.flight.cancellationV2.presentation.model.FlightCancellationRefundModel
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
            val descriptions: MutableList<FlightCancellationRefundModel> = ArrayList()
            for (i in titles.indices) {
                descriptions.add(FlightCancellationRefundModel(
                        title = titles[i],
                        subtitle = subtitles[i]
                ))
            }
            val adapter = FlightCancellationRefundAdapter()
            adapter.setDescriptions(descriptions)
            detailsRecyclerView.layoutManager = LinearLayoutManager(context)
            detailsRecyclerView.adapter = adapter
        }
    }
}