package com.tokopedia.flight.bookingV3.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.booking.di.FlightBookingComponent
import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.presentation.adapter.FlightJourneyAdapter
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import kotlinx.android.synthetic.main.fragment_flight_booking_v3.*
import kotlinx.android.synthetic.main.layout_flight_booking_v3_loading.*
import kotlinx.coroutines.*

/**
 * @author by jessica on 2019-10-24
 */

class FlightBookingFragment: BaseDaggerFragment() {

    val uiScope = CoroutineScope(Dispatchers.Main)
    var data: FlightCart = FlightCart()

    lateinit var flightRouteAdapter: FlightJourneyAdapter

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    lateinit var bookingViewModel: FlightBookingViewModel

    override fun getScreenName(): String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
//            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
//            bookingViewModel = viewModelProvider.get(FlightBookingViewModel::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        bookingViewModel.flightCartResult.observe(this, android.arch.lifecycle.Observer {
//            when (it) {
//                is Success -> {
//
//                }
//                is Fail -> {
//
//                }
//            }
//        })
    }

    fun renderData(cart: FlightCart) {
        hideShimmering()

        flightRouteAdapter = FlightJourneyAdapter()
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        rv_flight_booking_route_summary.layoutManager = layoutManager
        rv_flight_booking_route_summary.setHasFixedSize(true)
        rv_flight_booking_route_summary.adapter = flightRouteAdapter
        flightRouteAdapter.updateRoutes(cart.cartData.flight.journeys)
    }

    override fun initInjector() {
        getComponent(FlightBookingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_booking_v3, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gson = Gson()
        data = gson.fromJson(GraphqlHelper.loadRawString(resources, R.raw.dummy_get_cart),
                FlightCart.Response::class.java).flightCart

//        bookingViewModel.getCart(GraphqlHelper.loadRawString(resources, R.raw.dummy_get_cart), "")
        launchLoadingPage(randomLoadingSubtitle())

    }

    private fun randomLoadingSubtitle(): List<String> {
        var list = listOf(getString(R.string.flight_booking_loading_text_1),
                getString(R.string.flight_booking_loading_text_2),
                getString(R.string.flight_booking_loading_text_3),
                getString(R.string.flight_booking_loading_text_4))
        return list.shuffled()
    }

    private fun launchLoadingPage(list: List<String>) = uiScope.launch {
        layout_loading.visibility = View.VISIBLE
        tv_loading_subtitle.text = list[0]
        delay(2000L)
        tv_loading_subtitle.text = list[1]
        delay(2000L)
        tv_loading_subtitle.text = list[2]
        delay(2000L)
        layout_loading.visibility = View.GONE
        layout_shimmering.visibility = View.VISIBLE
        delay(2000L)
        renderData(data)
    }

    private fun hideShimmering() {
        if (layout_loading.isVisible) layout_loading.hide()
        if (layout_shimmering.isVisible) layout_shimmering.hide()
    }

    companion object {
        fun newInstance(): FlightBookingFragment {
            return FlightBookingFragment()
        }
    }

}