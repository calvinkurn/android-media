package com.tokopedia.hotel.orderdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.orderdetail.di.HotelOrderDetailComponent

/**
 * @author by jessica on 10/05/19
 */

class HotelOrderDetailFragment: BaseDaggerFragment() {

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(HotelOrderDetailComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activity?.run {
//            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
//            homepageViewModel = viewModelProvider.get(HotelHomepageViewModel::class.java)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_order_detail, container, false)


    companion object {
        fun getInstance(): HotelOrderDetailFragment = HotelOrderDetailFragment()
    }
}