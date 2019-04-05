package com.tokopedia.hotel.search.presentation.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.common.di.component.HotelComponent
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.presentation.adapter.PropertyAdapterTypeFactory

class HotelSearchResultFragment: BaseListFragment<Property, PropertyAdapterTypeFactory>() {

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

        fun createInstance(destinationID: Int = 0, type: String = "", latitude: Float = 0f,
                           longitude: Float = 0f, checkIn: String = "", checkOut: String = "",
                           totalRoom: Int = 1, totalAdult: Int = 0): HotelSearchResultFragment {

            return HotelSearchResultFragment().apply {
                arguments = Bundle().also {

                }
            }
        }
    }

    override fun getAdapterTypeFactory(): PropertyAdapterTypeFactory = PropertyAdapterTypeFactory()

    override fun onItemClicked(t: Property?) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(HotelComponent::class.java).inject(this)
    }

    override fun loadData(page: Int) {}
}