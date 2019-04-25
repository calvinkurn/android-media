package com.tokopedia.hotel.roomdetail.view.fragment

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.view.viewmodel.HotelRoomDetailViewModel
import javax.inject.Inject

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailFragment: BaseDaggerFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomDetailViewModel: HotelRoomDetailViewModel

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomDetailComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_room_detail, container, false)
        return view
    }

    companion object {
        fun getInstance(): HotelRoomDetailFragment = HotelRoomDetailFragment()
    }
}