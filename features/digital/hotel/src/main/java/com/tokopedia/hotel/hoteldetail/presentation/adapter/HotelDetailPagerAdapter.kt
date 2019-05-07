package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailAllFacilityFragment
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailFacilityFragment
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailPolicyFragment

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailPagerAdapter(fm: FragmentManager, val context: Context, var selectedTab: String)
    : FragmentStatePagerAdapter(fm) {

    private val tabCount = 4

    private lateinit var hotelFacilityFragment: HotelDetailFacilityFragment
    private lateinit var hotelPolicyFragment: HotelDetailPolicyFragment

    private lateinit var propertyData: PropertyDetailData

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> HotelDetailAllFacilityFragment.FACILITY_TITLE
                1 -> HotelDetailAllFacilityFragment.POLICY_TITLE
                2 -> HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE
                3 -> HotelDetailAllFacilityFragment.DESCRIPTION_TITLE
                else -> super.getPageTitle(position)
            }

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> {
                    if (!::hotelFacilityFragment.isInitialized) {
                        hotelFacilityFragment = HotelDetailFacilityFragment()
                    }
                    if (::propertyData.isInitialized) hotelFacilityFragment.initData(propertyData.facility)

                    hotelFacilityFragment
                }
                1 -> {
                    if (!::hotelPolicyFragment.isInitialized) {
                        hotelPolicyFragment = HotelDetailPolicyFragment()
                    }
                    if (::propertyData.isInitialized) hotelPolicyFragment.setPropertyData(propertyData.property)

                    hotelPolicyFragment
                }
                else -> HotelDetailFacilityFragment()
            }

    override fun getCount(): Int = tabCount

    fun setData(propertyData: PropertyDetailData) {
        this.propertyData = propertyData

        if (::hotelFacilityFragment.isInitialized && propertyData.facility.isNotEmpty()) {
            hotelFacilityFragment.initData(propertyData.facility)
        }

        if (::hotelPolicyFragment.isInitialized) hotelPolicyFragment.setPropertyData(propertyData.property)
    }
}