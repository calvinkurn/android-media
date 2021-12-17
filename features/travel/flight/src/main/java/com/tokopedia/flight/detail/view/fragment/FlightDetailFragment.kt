package com.tokopedia.flight.detail.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.databinding.FragmentFlightDetailBinding
import com.tokopedia.flight.detail.view.adapter.FlightDetailAdapterTypeFactory
import com.tokopedia.flight.detail.view.adapter.FlightDetailRouteTypeFactory
import com.tokopedia.flight.detail.view.model.FlightDetailRouteModel
import com.tokopedia.flight.detail.view.widget.FlightDetailListener
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by furqan on 21/04/2020
 */
class FlightDetailFragment : BaseListFragment<FlightDetailRouteModel, FlightDetailRouteTypeFactory>(),
        FlightDetailAdapterTypeFactory.OnFlightDetailListener {

    lateinit var listener: FlightDetailListener

    private var binding by autoClearedNullable<FragmentFlightDetailBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFlightDetailBinding.inflate(inflater, container, false)
        return binding?.root
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
        binding?.recyclerView?.isNestedScrollingEnabled = true
        binding?.recyclerView?.requestLayout()
    }

    private fun disableScrolling() {
        binding?.recyclerView?.isNestedScrollingEnabled = false
        binding?.recyclerView?.requestLayout()
    }

}