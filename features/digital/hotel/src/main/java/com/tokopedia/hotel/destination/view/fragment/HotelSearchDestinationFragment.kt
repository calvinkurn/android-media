package com.tokopedia.hotel.destination.view.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.destination.data.model.SearchDestination
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.adapter.SearchDestinationTypeFactory
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import javax.inject.Inject

/**
 * @author by jessica on 27/03/19
 */

class HotelSearchDestinationFragment: BaseListFragment<SearchDestination, SearchDestinationTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_search_destination, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): SearchDestinationTypeFactory {
        return SearchDestinationTypeFactory()
    }

    override fun onItemClicked(t: SearchDestination?) {
    }

    override fun getScreenName(): String = ""


    override fun initInjector() {
        getComponent(HotelComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
    }


}