package com.tokopedia.flight.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.detail.view.widget.FlightDetailListener
import kotlinx.android.synthetic.main.fragment_flight_detail.*

/**
 * @author by furqan on 21/04/2020
 */
class FlightDetailFragment : BaseListFragment<FlightDetailRouteModel, FlightDetailRouteTypeFactory>(),
        FlightDetailAdapterTypeFactory.OnFlightDetailListener {

    lateinit var listener: FlightDetailListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_flight_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        enableScrolling()
    }

    override fun onPause() {
        disableScrolling()
        super.onPause()
    }

    override fun getAdapterTypeFactory(): FlightDetailRouteTypeFactory =
            FlightDetailAdapterTypeFactory(this, false)

    override fun getScreenName(): String = ""

    override fun initInjector() {}

    override fun onItemClicked(t: FlightDetailRouteModel) {}

    override fun loadData(page: Int) {
        clearAllData()
        if (::listener.isInitialized) renderList(listener.getDetailModel().routeList)
    }

    override fun getItemCount(): Int = adapter.itemCount

    override fun getRecyclerViewResourceId(): Int = R.id.recycler_view

    private fun enableScrolling() {
        recycler_view.isNestedScrollingEnabled = true
        recycler_view.requestLayout()
    }

    private fun disableScrolling() {
        recycler_view.isNestedScrollingEnabled = false
        recycler_view.requestLayout()
    }

}