package com.tokopedia.flight.searchV4.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.search.presentation.adapter.FlightSearchAdapterTypeFactory
import com.tokopedia.flight.search.presentation.model.FlightJourneyModel

/**
 * @author by furqan on 06/04/2020
 */
class FlightSearchFragment : BaseListFragment<FlightJourneyModel, FlightSearchAdapterTypeFactory>() {

    override fun getAdapterTypeFactory(): FlightSearchAdapterTypeFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(t: FlightJourneyModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance(): FlightSearchFragment = FlightSearchFragment()
    }
}