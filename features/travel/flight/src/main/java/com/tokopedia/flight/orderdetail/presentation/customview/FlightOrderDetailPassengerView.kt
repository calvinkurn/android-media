package com.tokopedia.flight.orderdetail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailPassengerAdapter
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel
import kotlinx.android.synthetic.main.view_flight_order_detail_passenger.view.*

/**
 * @author by furqan on 12/11/2020
 */
class FlightOrderDetailPassengerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private var isPassengerDetailVisible: Boolean = false

    private var passengerList: List<FlightOrderDetailPassengerModel> = arrayListOf()

    init {
        View.inflate(context, R.layout.view_flight_order_detail_passenger, this)
    }

    fun setData(passengerList: List<FlightOrderDetailPassengerModel>) {
        this.passengerList = passengerList
    }

    fun buildView() {
        renderPassengerDetail()
        setupToggleButton()
    }

    private fun renderPassengerDetail() {
        val adapter = FlightOrderDetailPassengerAdapter(passengerList)
        rvFlightOrderPassengerDetail.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFlightOrderPassengerDetail.setHasFixedSize(true)
        rvFlightOrderPassengerDetail.adapter = adapter

        setupPassengerDetailView()
    }

    private fun setupToggleButton() {
        tgFlightOrderPassengerDetailTitle.setOnClickListener {
            togglePassengerDetail()
        }
        ivFlightOrderTogglePassengerDetail.setOnClickListener {
            togglePassengerDetail()
        }
    }

    private fun togglePassengerDetail() {
        isPassengerDetailVisible = !isPassengerDetailVisible
        setupPassengerDetailView()
    }

    private fun setupPassengerDetailView() {
        if (isPassengerDetailVisible) {
            ivFlightOrderTogglePassengerDetail.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24))
            rvFlightOrderPassengerDetail.visibility = View.VISIBLE
        } else {
            ivFlightOrderTogglePassengerDetail.setImageDrawable(MethodChecker.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24))
            rvFlightOrderPassengerDetail.visibility = View.GONE
        }
    }

}