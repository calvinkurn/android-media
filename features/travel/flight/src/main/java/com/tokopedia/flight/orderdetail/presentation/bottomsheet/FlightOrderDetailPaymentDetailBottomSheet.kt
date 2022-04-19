package com.tokopedia.flight.orderdetail.presentation.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailSimpleAdapter
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailSimpleModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_flight_order_detail_payment_detail.view.*

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailPaymentDetailBottomSheet : BottomSheetUnify() {

    private lateinit var mChildView: View

    var paymentDetailData: List<FlightOrderDetailSimpleModel> = arrayListOf()
    var amenitiesDetailData: List<FlightOrderDetailSimpleModel> = arrayListOf()
    var totalAmount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderView()
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        showKnob = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_order_detail_payment_detail_title_label))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_order_detail_payment_detail, null)
        setChild(mChildView)
    }

    private fun renderView() {
        renderPaymentDetailView()
        renderAmenitiesDetailView()

        mChildView.tgFlightOrderPaymentDetailTotal.text = totalAmount
    }

    private fun renderPaymentDetailView() {
        if (paymentDetailData.isNotEmpty()) {
            mChildView.rvFlightOrderPaymentDetailTicket.visibility = View.VISIBLE
            val adapter = FlightOrderDetailSimpleAdapter(paymentDetailData)
            mChildView.rvFlightOrderPaymentDetailTicket.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            mChildView.rvFlightOrderPaymentDetailTicket.setHasFixedSize(true)
            mChildView.rvFlightOrderPaymentDetailTicket.adapter = adapter
        } else {
            mChildView.rvFlightOrderPaymentDetailTicket.visibility = View.GONE
        }
    }

    private fun renderAmenitiesDetailView() {
        if (amenitiesDetailData.isNotEmpty()) {
            mChildView.rvFlightOrderPaymentDetailAmenity.visibility = View.VISIBLE
            val adapter = FlightOrderDetailSimpleAdapter(amenitiesDetailData)
            mChildView.rvFlightOrderPaymentDetailAmenity.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            mChildView.rvFlightOrderPaymentDetailAmenity.setHasFixedSize(true)
            mChildView.rvFlightOrderPaymentDetailAmenity.adapter = adapter
        } else {
            mChildView.rvFlightOrderPaymentDetailAmenity.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "TagFlightOrderDetailPaymentdetailBottomSheet"

        fun getInstance(paymentDetailData: List<FlightOrderDetailSimpleModel>,
                        amenitiesDetailData: List<FlightOrderDetailSimpleModel>,
                        totalAmount: String): FlightOrderDetailPaymentDetailBottomSheet =
                FlightOrderDetailPaymentDetailBottomSheet().also {
                    it.paymentDetailData = paymentDetailData
                    it.amenitiesDetailData = amenitiesDetailData
                    it.totalAmount = totalAmount
                }
    }

}