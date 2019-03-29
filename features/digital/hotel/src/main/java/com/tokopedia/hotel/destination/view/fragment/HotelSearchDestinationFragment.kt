package com.tokopedia.hotel.destination.view.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationTypeFactory

/**
 * @author by jessica on 27/03/19
 */

class HotelSearchDestinationFragment: BaseListFragment<SearchDestination, SearchDestinationTypeFactory>() {
    override fun getAdapterTypeFactory(): SearchDestinationTypeFactory {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemClicked(t: SearchDestination?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getScreenName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(page: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}