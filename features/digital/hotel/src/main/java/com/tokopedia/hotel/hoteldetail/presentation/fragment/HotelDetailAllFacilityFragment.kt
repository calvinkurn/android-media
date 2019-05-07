package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailPagerAdapter
import kotlinx.android.synthetic.main.fragment_hotel_detail_all_facility.*

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailAllFacilityFragment : TkpdBaseV4Fragment() {

    var propertyName: String = ""
    private var propertyData: PropertyDetailData? = null

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager
    lateinit var hotelDetailPagerAdapter: HotelDetailPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail_all_facility, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveInstanceCacheManager = SaveInstanceCacheManager(activity!!, savedInstanceState)
        val manager = if (savedInstanceState == null) SaveInstanceCacheManager(activity!!,
                arguments!!.getString(EXTRA_SAVED_OBJECT_ID)) else saveInstanceCacheManager

        propertyData = if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PROPERTY_DETAIL)) {
            savedInstanceState.getParcelable(EXTRA_PROPERTY_DETAIL)
        } else {
            manager.get(EXTRA_PROPERTY_DETAIL, PropertyDetailData::class.java)
        }

        renderTabAndViewPager()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_PROPERTY_NAME, propertyName)
        outState.putParcelable(EXTRA_PROPERTY_DETAIL, propertyData)
    }

    override fun getScreenName(): String = ""

    private fun renderTabAndViewPager() {
        tab_layout.setupWithViewPager(view_pager)
        view_pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        view_pager.adapter = getViewPagerAdapter()
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        if (!::hotelDetailPagerAdapter.isInitialized) {
            hotelDetailPagerAdapter = HotelDetailPagerAdapter(childFragmentManager, context!!,
                    arguments!!.getString(EXTRA_TAB_TITLE, FACILITY_TITLE))
        }
        if (propertyData != null) {
            hotelDetailPagerAdapter.setData(propertyData!!)
        }
        return hotelDetailPagerAdapter
    }

    companion object {

        const val EXTRA_SAVED_OBJECT_ID = "EXTRA_SAVED_OBJECT_ID"
        const val EXTRA_TAB_TITLE = "EXTRA_TAB_TITLE"
        const val EXTRA_PROPERTY_DETAIL = "EXTRA_PROPERTY_DETAIL"
        const val EXTRA_PROPERTY_NAME = "EXTRA_PROPERTY_DETAIL"

        const val FACILITY_TITLE = "Fasilitas"
        const val POLICY_TITLE = "Kebijakan"
        const val IMPORTANT_INFO_TITLE = "Informasi Penting"
        const val DESCRIPTION_TITLE = "Deskripsi"

        fun getInstance(propertyName: String, objectId: String, tabTitle: String): HotelDetailAllFacilityFragment =
                HotelDetailAllFacilityFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_PROPERTY_NAME, propertyName)
                        putString(EXTRA_SAVED_OBJECT_ID, objectId)
                        putString(EXTRA_TAB_TITLE, tabTitle)
                    }
                }

    }

}