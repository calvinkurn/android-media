package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailPagerAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import kotlinx.android.synthetic.main.fragment_hotel_detail_all_facility.*

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailAllFacilityFragment : TkpdBaseV4Fragment() {

    var propertyName: String = ""
    private lateinit var propertyData: HotelDetailAllFacilityModel

    lateinit var hotelDetailPagerAdapter: HotelDetailPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail_all_facility, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle = savedInstanceState ?: arguments!!
        propertyData = args.getParcelable(EXTRA_PROPERTY_DETAIL)
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
        hotelDetailPagerAdapter.setData(propertyData)
        return hotelDetailPagerAdapter
    }

    companion object {

        const val EXTRA_TAB_TITLE = "EXTRA_TAB_TITLE"
        const val EXTRA_PROPERTY_DETAIL = "EXTRA_PROPERTY_DETAIL"
        const val EXTRA_PROPERTY_NAME = "EXTRA_PROPERTY_NAME"

        const val FACILITY_TITLE = "Fasilitas"
        const val POLICY_TITLE = "Kebijakan"
        const val IMPORTANT_INFO_TITLE = "Informasi Penting"
        const val DESCRIPTION_TITLE = "Deskripsi"

        fun getInstance(propertyName: String, data: HotelDetailAllFacilityModel, tabTitle: String): HotelDetailAllFacilityFragment =
                HotelDetailAllFacilityFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_PROPERTY_NAME, propertyName)
                        putParcelable(EXTRA_PROPERTY_DETAIL, data)
                        putString(EXTRA_TAB_TITLE, tabTitle)
                    }
                }

    }

}