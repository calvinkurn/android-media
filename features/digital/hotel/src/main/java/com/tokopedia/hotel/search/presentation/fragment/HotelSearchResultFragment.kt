package com.tokopedia.hotel.search.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.design.list.decoration.SpaceItemDecoration
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory
import com.tokopedia.hotel.search.presentation.viewmodel.HotelSearchResultViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class HotelSearchResultFragment: BaseListFragment<Property, PropertyAdapterTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var searchResultviewModel: HotelSearchResultViewModel

    companion object {
        private const val ARG_DESTINATION_ID = "arg_destination"
        private const val ARG_TYPE = "arg_type"
        private const val ARG_LAT = "arg_lat"
        private const val ARG_LONG = "arg_long"
        private const val ARG_CHECK_IN = "arg_check_in"
        private const val ARG_CHECK_OUT = "arg_check_out"
        private const val ARG_TOTAL_ROOM = "arg_total_room"
        private const val ARG_TOTAL_ADULT = "arg_total_adult"
        private const val ARG_TOTAL_CHILDERN = "arg_total_children"
        private const val ARG_DESTINATION_NAME = "arg_destination_name"

        fun createInstance(destinationName: String = "", destinationID: Int = 0, type: String = "",
                           latitude: Float = 0f, longitude: Float = 0f, checkIn: String = "",
                           checkOut: String = "", totalRoom: Int = 1, totalAdult: Int = 0,
                           totalChildren: Int = 0): HotelSearchResultFragment {

            return HotelSearchResultFragment().also {
                it.arguments = Bundle().apply {
                    putString(ARG_DESTINATION_NAME, destinationName)
                    putInt(ARG_DESTINATION_ID, destinationID)
                    putString(ARG_TYPE, type)
                    putFloat(ARG_LAT, latitude)
                    putFloat(ARG_LONG, longitude)
                    putString(ARG_CHECK_IN, checkIn)
                    putString(ARG_CHECK_OUT, checkOut)
                    putInt(ARG_TOTAL_ROOM, totalRoom)
                    putInt(ARG_TOTAL_ADULT, totalAdult)
                    putInt(ARG_TOTAL_CHILDERN, totalChildren)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        searchResultviewModel = viewModelProvider.get(HotelSearchResultViewModel::class.java)
        arguments?.let {
            searchResultviewModel.initSearchParam(it.getInt(ARG_DESTINATION_ID),
                    it.getString(ARG_TYPE, ""),
                    it.getFloat(ARG_LAT, 0f),
                    it.getFloat(ARG_LONG, 0f),
                    it.getString(ARG_CHECK_IN, ""),
                    it.getString(ARG_CHECK_OUT, ""),
                    it.getInt(ARG_TOTAL_ROOM, 1),
                    it.getInt(ARG_TOTAL_ADULT, 0))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchResultviewModel.liveSearchResult.observe(this, Observer {
            when(it){
                is Success -> onSuccessGetResult(it.data)
                is Fail -> onErrorGetResult(it.throwable)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            val recyclerView = getRecyclerView(view)
            recyclerView.removeItemDecorationAt(0)
            recyclerView.addItemDecoration(SpaceItemDecoration(it.resources.getDimensionPixelSize(R.dimen.dp_12),
                    LinearLayoutManager.VERTICAL))
        }
    }

    private fun onErrorGetResult(throwable: Throwable) {
        super.showGetListError(throwable)
    }

    private fun onSuccessGetResult(data: PropertySearch) {
        super.renderList(data.properties)
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory()

    override fun onItemClicked(t: Property?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(HotelSearchPropertyComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {
        searchResultviewModel.searchProperty(page)
    }
}