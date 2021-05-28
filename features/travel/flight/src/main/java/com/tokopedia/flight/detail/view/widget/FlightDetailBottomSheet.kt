package com.tokopedia.flight.detail.view.widget

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.flight.R
import com.tokopedia.flight.common.util.FlightAnalytics
import com.tokopedia.flight.common.util.FlightDateUtil
import com.tokopedia.flight.detail.view.fragmentnew.FlightDetailFacilityFragment
import com.tokopedia.flight.detail.view.fragmentnew.FlightDetailFragment
import com.tokopedia.flight.detail.view.fragmentnew.FlightDetailPriceFragment
import com.tokopedia.flight.detail.view.model.FlightDetailModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_flight_detail.view.*

/**
 * @author by furqan on 21/04/2020
 */
class FlightDetailBottomSheet : BottomSheetUnify() {

    lateinit var listener: Listener

    private lateinit var mChildView: View
    private lateinit var flightAnalytics: FlightAnalytics

    private lateinit var journeyFragment: FlightDetailFragment
    private lateinit var facilityFragment: FlightDetailFacilityFragment
    private lateinit var priceFragment: FlightDetailPriceFragment

    private lateinit var flightDetailModel: FlightDetailModel
    private var isShowSubmitButton: Boolean = false

    private lateinit var flightDetailListener: FlightDetailListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            if (it.containsKey(SAVED_INSTANCE_MANAGER_ID)) {
                val manager = SaveInstanceCacheManager(requireContext(), it.getString(SAVED_INSTANCE_MANAGER_ID))
                flightDetailModel = manager.get(SAVED_DETAIL_MODEL, FlightDetailModel::class.java)
                        ?: FlightDetailModel()
                isShowSubmitButton = manager.get(SAVED_SHOW_BUTTON, Boolean::class.java)
                        ?: false
            }
        }

        flightAnalytics = FlightAnalytics(FlightDateUtil())

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        context?.run {
            val manager = SaveInstanceCacheManager(this, true).also {
                it.put(SAVED_DETAIL_MODEL, flightDetailModel)
                it.put(SAVED_SHOW_BUTTON, isShowSubmitButton)
            }
            outState.putString(SAVED_INSTANCE_MANAGER_ID, manager.id)
        }
    }

    fun setShowSubmitButton(isShowSubmitButton: Boolean) {
        this.isShowSubmitButton = isShowSubmitButton
    }

    fun setDetailModel(detailModel: FlightDetailModel) {
        this.flightDetailModel = detailModel
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        showKnob = false
        isDragable = true
        isHideable = true
        setTitle(getString(R.string.flight_detail_bottom_sheet_title))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_detail, null)
        setChild(mChildView)

        flightDetailListener = object : FlightDetailListener {
            override fun getDetailModel(): FlightDetailModel = flightDetailModel

        }
    }

    private fun initView() {
        initTabLayout()
        initViewPager()

        if (::flightDetailModel.isInitialized) {
            with(mChildView) {
                flightDetailDepartureAirportCode.text = flightDetailModel.departureAirport
                flightDetailDepartureAirportName.text = flightDetailModel.departureAirportCity
                flightDetailArrivalAirportCode.text = flightDetailModel.arrivalAirport
                flightDetailArrivalAirportName.text = flightDetailModel.arrivalAirportCity

                if (isShowSubmitButton) {
                    flightDetailButtonContainer.visibility = View.VISIBLE
                } else {
                    flightDetailButtonContainer.visibility = View.GONE
                }
                flightDetailSelectButton.setOnClickListener {
                    if (::listener.isInitialized) listener.onSelectedFromDetail(this@FlightDetailBottomSheet, flightDetailModel.id)
                }

            }
        }
    }

    private fun initTabLayout() {
        with(mChildView) {
            if (flightDetailTabs.getUnifyTabLayout().tabCount == 0) {
                flightDetailTabs.addNewTab(getString(R.string.flight_detail_bottom_sheet_tab_journey_title))
                flightDetailTabs.addNewTab(getString(R.string.flight_detail_bottom_sheet_tab_facility_title))
                flightDetailTabs.addNewTab(getString(R.string.flight_detail_bottom_sheet_tab_price_title))
            }

            flightDetailTabs.getUnifyTabLayout().addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab) {}

                override fun onTabUnselected(tab: TabLayout.Tab) {}

                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (tab.position) {
                        TAB_JOURNEY_POSITION -> {
                            flightAnalytics.eventDetailTabClick(flightDetailModel)
                        }
                        TAB_FACILITY_POSITION -> {
                            flightAnalytics.eventDetailFacilitiesTabClick(flightDetailModel)
                        }
                        TAB_PRICE_POSITION -> {
                            flightAnalytics.eventDetailPriceTabClick(flightDetailModel)
                        }
                    }
                }
            })

            flightDetailTabs.getUnifyTabLayout().setupWithViewPager(flightDetailViewPager)
        }
    }

    private fun initViewPager() {
        val pagerStateAdapter = object : FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    TAB_JOURNEY_POSITION -> {
                        if (!::journeyFragment.isInitialized) {
                            journeyFragment = FlightDetailFragment()
                        }
                        journeyFragment.listener = flightDetailListener
                        journeyFragment
                    }
                    TAB_FACILITY_POSITION -> {
                        if (!::facilityFragment.isInitialized) {
                            facilityFragment = FlightDetailFacilityFragment()
                        }
                        facilityFragment.listener = flightDetailListener
                        facilityFragment
                    }
                    else -> {
                        if (!::priceFragment.isInitialized) {
                            priceFragment = FlightDetailPriceFragment()
                        }
                        priceFragment.listener = flightDetailListener
                        priceFragment
                    }
                }
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return when (position) {
                    TAB_JOURNEY_POSITION -> getString(R.string.flight_detail_bottom_sheet_tab_journey_title)
                    TAB_FACILITY_POSITION -> getString(R.string.flight_detail_bottom_sheet_tab_facility_title)
                    TAB_PRICE_POSITION -> getString(R.string.flight_detail_bottom_sheet_tab_price_title)
                    else -> super.getPageTitle(position)
                }
            }

            override fun getCount(): Int = TAB_DETAIL_SIZE
        }

        with(mChildView) {
            flightDetailViewPager.offscreenPageLimit = TAB_DETAIL_SIZE
            flightDetailViewPager.addOnPageChangeListener(object :
                    TabLayout.TabLayoutOnPageChangeListener(flightDetailTabs.getUnifyTabLayout()) {})
            flightDetailViewPager.adapter = pagerStateAdapter
        }
    }

    interface Listener {
        fun onSelectedFromDetail(detailBottomSheet: FlightDetailBottomSheet, selectedId: String)
    }

    companion object {
        const val TAG_FLIGHT_DETAIL_BOTTOM_SHEET = "TAG_FLIGHT_DETAIL_BOTTOM_SHEET"

        private const val SAVED_DETAIL_MODEL = "SAVED_DETAIL_MODEL"
        private const val SAVED_SHOW_BUTTON = "SAVED_SHOW_BUTTON"
        private const val SAVED_INSTANCE_MANAGER_ID = "SAVED_INSTANCE_MANAGER_ID"

        private const val TAB_DETAIL_SIZE = 3
        private const val TAB_JOURNEY_POSITION = 0
        private const val TAB_FACILITY_POSITION = 1
        private const val TAB_PRICE_POSITION = 2

        fun getInstance(): FlightDetailBottomSheet = FlightDetailBottomSheet()
    }

}

interface FlightDetailListener {
    fun getDetailModel(): FlightDetailModel
}