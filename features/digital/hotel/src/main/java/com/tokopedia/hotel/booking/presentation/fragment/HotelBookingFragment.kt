package com.tokopedia.hotel.booking.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class HotelBookingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    lateinit var hotelCart: HotelCart
    lateinit var cartId: String

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(HotelBookingViewModel::class.java)
        }

        arguments?.let {
            cartId = it.getString(ARG_CART_ID, "")
        }

        saveInstanceCacheManager = SaveInstanceCacheManager(context!!, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.hotelCartResult.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    hotelCart = it.data
                }
                is Fail -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_booking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingViewModel.getcartData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_cart),
                cartId, false)

        initView()
    }

    fun initView() {

    }

    override fun getScreenName(): String = "Pembayaran"

    override fun initInjector() {
        getComponent(HotelBookingComponent::class.java).inject(this)
    }

    companion object {
        const val ARG_CART_ID = "arg_cart_id"

        fun getInstance(cartId: String): HotelBookingFragment =
                HotelBookingFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CART_ID, cartId)
                    }
                }
    }
}