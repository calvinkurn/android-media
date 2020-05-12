package com.tokopedia.hotel.search.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity
import com.tokopedia.hotel.search.di.DaggerHotelSearchPropertyComponent
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import kotlinx.android.synthetic.main.activity_hotel_search_result.*
import javax.inject.Inject

class HotelSearchResultActivity : HotelBaseActivity(), HasComponent<HotelSearchPropertyComponent> {

    var checkIn = ""
    var checkOut = ""
    var checkInString = ""
    var checkOutString = ""
    var id: Long = 0
    var name = ""
    var type = ""
    var room = 1
    var adult = 1
    var lat = 0f
    var long = 0f
    var children = 0
    var subtitle = ""
    private lateinit var wrapper: LinearLayout

    override fun getLayoutRes(): Int = R.layout.activity_hotel_search_result

    override fun getToolbarResourceID(): Int = R.id.hotel_search_header

    override fun getParentViewResourceID(): Int = R.id.hotel_search_parent_view

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            if (!uri.getQueryParameter(PARAM_HOTEL_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_HOTEL_ID).toLong()
                name = uri.getQueryParameter(PARAM_HOTEL_NAME)
                type = TYPE_PROPERTY
            } else if (!uri.getQueryParameter(PARAM_CITY_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_CITY_ID).toLong()
                name = uri.getQueryParameter(PARAM_CITY_NAME)
                type = TYPE_CITY
            } else if (!uri.getQueryParameter(PARAM_DISTRICT_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_DISTRICT_ID).toLong()
                name = uri.getQueryParameter(PARAM_DISTRICT_NAME)
                type = TYPE_DISTRICT
            } else if (!uri.getQueryParameter(PARAM_REGION_ID).isNullOrEmpty()) {
                id = uri.getQueryParameter(PARAM_REGION_ID).toLong()
                name = uri.getQueryParameter(PARAM_REGION_NAME)
                type = TYPE_REGION
            }

            if (!uri.getQueryParameter(PARAM_CHECK_IN).isNullOrEmpty()) checkIn = uri.getQueryParameter(PARAM_CHECK_IN)
            if (!uri.getQueryParameter(PARAM_CHECK_OUT).isNullOrEmpty()) checkOut = uri.getQueryParameter(PARAM_CHECK_OUT)
            if (!uri.getQueryParameter(PARAM_ROOM).isNullOrEmpty()) room = uri.getQueryParameter(PARAM_ROOM).toInt()
            if (!uri.getQueryParameter(PARAM_ADULT).isNullOrEmpty()) adult = uri.getQueryParameter(PARAM_ADULT).toInt()

        } else {
            name = intent.getStringExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME)
            id = intent.getLongExtra(HotelSearchResultFragment.ARG_DESTINATION_ID, 0)
            type = intent.getStringExtra(HotelSearchResultFragment.ARG_TYPE)
            lat = intent.getFloatExtra(HotelSearchResultFragment.ARG_LAT, 0f)
            long = intent.getFloatExtra(HotelSearchResultFragment.ARG_LONG, 0f)
            room = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, 1)
            adult = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, 1)
            children = intent.getIntExtra(HotelSearchResultFragment.ARG_TOTAL_CHILDREN, 0)
            checkIn = intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_IN)
            checkOut = intent.getStringExtra(HotelSearchResultFragment.ARG_CHECK_OUT)
        }

        checkParameter()

        super.onCreate(savedInstanceState)

        setupSearchToolbarAction()
        setUpTitleAndSubtitle()
    }

    private fun setupSearchToolbarAction() {
        wrapper = LinearLayout(this)
        wrapper.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Neutral_N0))
            layoutParams = param
        }

        val textView = TextView(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = param
        textView.text = resources.getString(R.string.hotel_search_result_change)
        textView.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Green_G500))

        wrapper.addView(textView)
        wrapper.setOnClickListener {
            changeSearchParameter()
        }
        hotel_search_header.addCustomRightContent(wrapper)
        hotel_search_header.isShowBackButton = true
        hotel_search_header.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpTitleAndSubtitle() {
        checkInString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkIn))
        checkOutString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOut))
        subtitle = getString(R.string.template_search_subtitle,
                checkInString,
                checkOutString,
                room,
                adult)

        hotel_search_header.title = name
        hotel_search_header.subtitle = subtitle
        hotel_search_header.subheaderView?.setTextColor(ContextCompat.getColor(this, R.color.search_subtitle))
    }

    private fun changeSearchParameter() {
        if (fragment is HotelSearchResultFragment) {
            (fragment as HotelSearchResultFragment).onClickChangeSearch(type, name, room, adult, checkIn, checkOut, SEARCH_SCREEN_NAME)
        }
        startActivityForResult(HotelChangeSearchActivity.getIntent(this, id, name, type, lat.toDouble(), long.toDouble(),
                checkIn, checkOut, adult, room, getString(R.string.hotel_search_result_change_toolbar_title)),
                CHANGE_SEARCH_REQ_CODE)
    }

    private fun checkParameter() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(checkIn, checkOut)
        checkIn = updatedCheckInCheckOutDate.first
        checkOut = updatedCheckInCheckOutDate.second
        checkInString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkIn))
        checkOutString = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOut))
    }

    override fun getNewFragment(): Fragment {
        return HotelSearchResultFragment.createInstance(name, id, type, lat, long, checkIn, checkOut, room, adult, children)
    }

    override fun getComponent(): HotelSearchPropertyComponent =
            DaggerHotelSearchPropertyComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == CHANGE_SEARCH_REQ_CODE) {
            if (fragment is HotelSearchResultFragment) {
                data?.let {
                    id = it.getLongExtra(HotelChangeSearchActivity.DESTINATION_ID, 0)
                    name = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_NAME)
                    type = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_TYPE)
                    lat = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LAT, 0.0).toFloat()
                    long = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LONG, 0.0).toFloat()
                    checkIn = it.getStringExtra(HotelChangeSearchActivity.CHECK_IN_DATE)
                    checkOut = it.getStringExtra(HotelChangeSearchActivity.CHECK_OUT_DATE)
                    room = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_ROOMS, 1)
                    adult = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_GUESTS, 0)
                    (fragment as HotelSearchResultFragment).let { searchFragment ->
                        searchFragment.searchResultviewModel.initSearchParam(id, type,
                                lat, long, checkIn, checkOut, room, adult)
                        searchFragment.searchDestinationName = name
                        searchFragment.searchDestinationType = type

                        setUpTitleAndSubtitle()
                        searchFragment.changeSearchParam()
                    }
                }
            }

        }
    }

    companion object {

        const val CHANGE_SEARCH_REQ_CODE = 101

        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_DISTRICT_ID = "district_id"
        const val PARAM_DISTRICT_NAME = "district_name"
        const val PARAM_CITY_ID = "city_id"
        const val PARAM_CITY_NAME = "city_name"
        const val PARAM_REGION_ID = "region_id"
        const val PARAM_REGION_NAME = "region_name"
        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"

        const val TYPE_REGION = "region"
        const val TYPE_DISTRICT = "district"
        const val TYPE_CITY = "city"
        const val TYPE_PROPERTY = "property"

        const val SEARCH_SCREEN_NAME = "/hotel/searchresult"

        fun createIntent(context: Context, destinationName: String = "", destinationID: Long = 0, type: String = "",
                         latitude: Float = 0f, longitude: Float = 0f, checkIn: String = "",
                         checkOut: String = "", totalRoom: Int = 1, totalAdult: Int = 0,
                         totalChildren: Int = 0): Intent =
                Intent(context, HotelSearchResultActivity::class.java)
                        .putExtra(HotelSearchResultFragment.ARG_DESTINATION_NAME, destinationName)
                        .putExtra(HotelSearchResultFragment.ARG_DESTINATION_ID, destinationID)
                        .putExtra(HotelSearchResultFragment.ARG_TYPE, type)
                        .putExtra(HotelSearchResultFragment.ARG_LAT, latitude)
                        .putExtra(HotelSearchResultFragment.ARG_LONG, longitude)
                        .putExtra(HotelSearchResultFragment.ARG_CHECK_IN, checkIn)
                        .putExtra(HotelSearchResultFragment.ARG_CHECK_OUT, checkOut)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_ROOM, totalRoom)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_ADULT, totalAdult)
                        .putExtra(HotelSearchResultFragment.ARG_TOTAL_CHILDREN, totalChildren)
    }
}