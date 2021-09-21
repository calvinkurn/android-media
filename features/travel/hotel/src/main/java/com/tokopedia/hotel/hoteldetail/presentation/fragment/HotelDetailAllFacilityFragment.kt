package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.hotel.databinding.FragmentHotelDetailAllFacilityBinding
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailPagerAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * @author by furqan on 06/05/19
 */
class HotelDetailAllFacilityFragment : Fragment() {

    private var propertyName: String = ""
    private lateinit var propertyData: HotelDetailAllFacilityModel
    private var binding by autoClearedNullable<FragmentHotelDetailAllFacilityBinding>()

    lateinit var hotelDetailPagerAdapter: HotelDetailPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelDetailAllFacilityBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: Bundle? = savedInstanceState ?: arguments
        args?.let {
            propertyData = it.getParcelable(EXTRA_PROPERTY_DETAIL) ?: HotelDetailAllFacilityModel()
        }
        renderTabAndViewPager()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_PROPERTY_NAME, propertyName)
        outState.putParcelable(EXTRA_PROPERTY_DETAIL, propertyData)
    }

    private fun renderTabAndViewPager() {
        binding?.let {
            it.tabLayout.setupWithViewPager(it.viewPager)
            it.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(it.tabLayout))
            it.viewPager.adapter = getViewPagerAdapter()

            for (i in 0 until hotelDetailPagerAdapter.count) {
                if (it.tabLayout.getTabAt(i)?.text == arguments?.getString(EXTRA_TAB_TITLE, FACILITY_TITLE)) {
                    it.tabLayout.getTabAt(i)?.select()
                    break
                }
            }
        }
    }

    private fun getViewPagerAdapter(): PagerAdapter {
        if (!::hotelDetailPagerAdapter.isInitialized) {
            context?.run {
                arguments?.let {
                    hotelDetailPagerAdapter = HotelDetailPagerAdapter(childFragmentManager, this,
                            it.getString(EXTRA_TAB_TITLE, FACILITY_TITLE))
                }
            }
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