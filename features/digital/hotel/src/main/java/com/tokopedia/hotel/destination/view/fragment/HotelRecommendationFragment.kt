package com.tokopedia.hotel.destination.view.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.data.model.PopularSearch
import com.tokopedia.hotel.destination.di.HotelDestinationComponent
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.adapter.PopularSearchClickListener
import com.tokopedia.hotel.destination.view.adapter.PopularSearchTypeFactory
import com.tokopedia.hotel.destination.view.viewmodel.HotelDestinationViewModel
import com.tokopedia.permissionchecker.PermissionCheckerHelper
import kotlinx.android.synthetic.main.fragment_hotel_recommendation.*
import javax.inject.Inject

/**
 * @author by jessica on 25/03/19
 */

class HotelRecommendationFragment: BaseListFragment<PopularSearch, PopularSearchTypeFactory>(), PopularSearchClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var destinationViewModel: HotelDestinationViewModel
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDestinationComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            destinationViewModel = viewModelProvider.get(HotelDestinationViewModel::class.java)
        }

        permissionCheckerHelper = PermissionCheckerHelper()
        destinationViewModel.setPermissionChecker(permissionCheckerHelper)

        initView()
    }

    fun initView() {
        initCurrentLocationTextView()
    }

    fun initCurrentLocationTextView() {
//        current_location_tv.setDrawableLeft(R.drawable.ic_system_action_currentlocation_grayscale_24)
        current_location_tv.setOnClickListener { destinationViewModel.getCurrentLocation(activity as HotelDestinationActivity) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hotel_recommendation, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapterTypeFactory(): PopularSearchTypeFactory = PopularSearchTypeFactory(this)

    override fun onItemClicked(t: PopularSearch) {

    }

    override fun loadData(page: Int) {

    }

    override fun popularSearchClicked(popularSearch: PopularSearch) {

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.onRequestPermissionsResult(activity as HotelDestinationActivity,
                    requestCode, permissions,
                    grantResults)
        }
    }

    companion object {
        fun getInstance(): HotelRecommendationFragment = HotelRecommendationFragment()
    }

}