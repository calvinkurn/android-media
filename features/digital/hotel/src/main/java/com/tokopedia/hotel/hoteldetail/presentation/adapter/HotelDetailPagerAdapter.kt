package com.tokopedia.hotel.hoteldetail.presentation.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.presentation.fragment.*
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailPolicyModel

/**
 * @author by furqan on 07/05/19
 */
class HotelDetailPagerAdapter(fm: FragmentManager, val context: Context, var selectedTab: String)
    : FragmentStatePagerAdapter(fm), HotelDetailPolicyFragment.Connector, HotelDetailFacilityFragment.Connector,
        HotelDetailImportantInfoFragment.Connector, HotelDetailDescriptionFragment.Connector {

    private lateinit var hotelFacilityFragment: HotelDetailFacilityFragment
    private lateinit var hotelPolicyFragment: HotelDetailPolicyFragment
    private lateinit var hotelImportantInfoFragment: HotelDetailImportantInfoFragment
    private lateinit var hotelDescriptionFragment: HotelDetailDescriptionFragment

    private lateinit var propertyData: HotelDetailAllFacilityModel

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> if (isFacilityTabExists()) {
                    HotelDetailAllFacilityFragment.FACILITY_TITLE
                } else {
                    HotelDetailAllFacilityFragment.POLICY_TITLE
                }
                1 -> if (isFacilityTabExists()) {
                    HotelDetailAllFacilityFragment.POLICY_TITLE
                } else {
                    HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE
                }
                2 -> if (isImportantInfoTabExists()) {
                    HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE
                } else {
                    HotelDetailAllFacilityFragment.DESCRIPTION_TITLE
                }
                3 -> HotelDetailAllFacilityFragment.DESCRIPTION_TITLE
                else -> super.getPageTitle(position)
            }

    override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> if (isFacilityTabExists()) {
                    returnFacilityFragment()
                } else {
                    returnPolicyFragment()
                }
                1 -> if (isFacilityTabExists()) {
                    returnPolicyFragment()
                } else {
                    returnImportantInfoFragment()
                }
                2 -> if (isFacilityTabExists() && isImportantInfoTabExists()) {
                    returnImportantInfoFragment()
                } else {
                    returnDescriptionFragment()
                }
                3 -> {
                    returnDescriptionFragment()
                }
                else -> HotelDetailFacilityFragment()
            }

    override fun getCount(): Int = tabCount()

    fun setData(propertyData: HotelDetailAllFacilityModel) {
        this.propertyData = propertyData
    }

    override fun getPolicyData(): HotelDetailPolicyModel = propertyData.policyData

    override fun getFacilityData(): List<FacilityData> = propertyData.facilityListData

    override fun getImportantInfo(): String = propertyData.importantInfo

    override fun getDescription(): String = propertyData.description

    private fun returnFacilityFragment(): HotelDetailFacilityFragment {
        if (!::hotelFacilityFragment.isInitialized) {
            hotelFacilityFragment = HotelDetailFacilityFragment()
            hotelFacilityFragment.connector = this
        }

        return hotelFacilityFragment
    }

    private fun returnPolicyFragment(): HotelDetailPolicyFragment {
        if (!::hotelPolicyFragment.isInitialized) {
            hotelPolicyFragment = HotelDetailPolicyFragment()
            hotelPolicyFragment.connector = this
        }

        return hotelPolicyFragment
    }

    private fun returnImportantInfoFragment(): HotelDetailImportantInfoFragment {
        if (!::hotelImportantInfoFragment.isInitialized) {
            hotelImportantInfoFragment = HotelDetailImportantInfoFragment()
            hotelImportantInfoFragment.connector = this
        }

        return hotelImportantInfoFragment
    }

    private fun returnDescriptionFragment(): HotelDetailDescriptionFragment {
        if (!::hotelDescriptionFragment.isInitialized) {
            hotelDescriptionFragment = HotelDetailDescriptionFragment()
            hotelDescriptionFragment.connector = this
        }

        return hotelDescriptionFragment
    }

    private fun isFacilityTabExists(): Boolean = propertyData.facilityListData.isNotEmpty()

    private fun isImportantInfoTabExists(): Boolean = propertyData.importantInfo.isNotEmpty()

    private fun isDescriptionTabExists(): Boolean = propertyData.description.isNotEmpty()

    private fun tabCount(): Int {
        var count = 1

        if (isFacilityTabExists()) count++
        if (isImportantInfoTabExists()) count++
        if (isDescriptionTabExists()) count++

        return count
    }
}