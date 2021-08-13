package com.tokopedia.flight.orderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.flight.orderdetail.presentation.adapter.FlightOrderDetailWebCheckInAdapter
import com.tokopedia.flight.orderdetail.presentation.adapter.viewholder.FlightOrderDetailWebCheckInViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailPassengerModel
import com.tokopedia.flight.orderdetail.presentation.viewmodel.FlightOrderDetailWebCheckInViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_flight_order_detail_web_checkin.*
import javax.inject.Inject

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailWebCheckInFragment : BaseDaggerFragment(),
        FlightOrderDetailWebCheckInViewHolder.Listener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightOrderDetailWebCheckInViewModel: FlightOrderDetailWebCheckInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        flightOrderDetailWebCheckInViewModel = viewModelProvider.get(FlightOrderDetailWebCheckInViewModel::class.java)
        flightOrderDetailWebCheckInViewModel.orderId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
        flightOrderDetailWebCheckInViewModel.fetchOrderDetailData()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_order_detail_web_checkin, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightOrderDetailWebCheckInViewModel.orderDetailData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderView(it.data.journeys, it.data.passengers)
                }
                is Fail -> {

                }
            }
        })
    }

    override fun onCheckInClicked(journey: FlightOrderDetailJourneyModel, isDeparture: Boolean) {
        context?.let {
            flightOrderDetailWebCheckInViewModel.trackOnCheckInDeparture(journey, isDeparture)
            RouteManager.route(it, journey.webCheckIn.webUrl)
        }
    }

    private fun renderView(journeyList: List<FlightOrderDetailJourneyModel>, passengerList: List<FlightOrderDetailPassengerModel>) {
        context?.let {
            val adapter = FlightOrderDetailWebCheckInAdapter(journeyList, passengerList, this)
            rvFlightOrderDetailWebCheckIn.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
            rvFlightOrderDetailWebCheckIn.setHasFixedSize(true)
            rvFlightOrderDetailWebCheckIn.adapter = adapter
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java)
                .inject(this)
    }

    companion object {
        private const val EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID"

        fun getInstance(invoiceId: String): FlightOrderDetailWebCheckInFragment =
                FlightOrderDetailWebCheckInFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}