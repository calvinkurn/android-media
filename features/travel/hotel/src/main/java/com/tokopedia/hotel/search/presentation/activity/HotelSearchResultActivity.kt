package com.tokopedia.hotel.search.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelChangeSearchActivity
import com.tokopedia.hotel.search.data.model.HotelSearchModel
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.di.DaggerHotelSearchPropertyComponent
import com.tokopedia.hotel.search.di.HotelSearchPropertyComponent
import com.tokopedia.hotel.search.presentation.fragment.HotelSearchResultFragment
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.activity_hotel_search_result.*

class HotelSearchResultActivity : HotelBaseActivity(), HasComponent<HotelSearchPropertyComponent> {

    var hotelSearchModel = HotelSearchModel()
    var selectedParam = ParamFilterV2()

    private lateinit var wrapper: LinearLayout

    override fun getLayoutRes(): Int = R.layout.activity_hotel_search_result

    override fun getToolbarResourceID(): Int = R.id.hotel_search_header

    override fun getParentViewResourceID(): Int = R.id.hotel_search_parent_view

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getScreenName(): String = SEARCH_SCREEN_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            //for newer applink
            if (!uri.getQueryParameter(PARAM_ID).isNullOrEmpty()) {
                hotelSearchModel.searchId = uri.getQueryParameter(PARAM_ID) ?: ""
                hotelSearchModel.searchType = uri.getQueryParameter(PARAM_TYPE) ?: ""
                hotelSearchModel.name = uri.getQueryParameter(PARAM_NAME) ?: ""
            } else {
                // for older applink
                when {
                    !uri.getQueryParameter(PARAM_HOTEL_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(PARAM_HOTEL_ID) ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(PARAM_HOTEL_NAME) ?: ""
                        hotelSearchModel.type = HotelTypeEnum.PROPERTY.value
                    }
                    !uri.getQueryParameter(PARAM_CITY_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(PARAM_CITY_ID) ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(PARAM_CITY_NAME) ?: ""
                        hotelSearchModel.type = HotelTypeEnum.CITY.value
                    }
                    !uri.getQueryParameter(PARAM_DISTRICT_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(PARAM_DISTRICT_ID) ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(PARAM_DISTRICT_NAME) ?: ""
                        hotelSearchModel.type = HotelTypeEnum.DISTRICT.value
                    }
                    !uri.getQueryParameter(PARAM_REGION_ID).isNullOrEmpty() -> {
                        hotelSearchModel.id = (uri.getQueryParameter(PARAM_REGION_ID) ?: "0").toLong()
                        hotelSearchModel.name = uri.getQueryParameter(PARAM_REGION_NAME) ?: ""
                        hotelSearchModel.type = HotelTypeEnum.REGION.value
                    }
                }
            }

            hotelSearchModel.checkIn = uri.getQueryParameter(PARAM_CHECK_IN) ?: ""
            hotelSearchModel.checkOut = uri.getQueryParameter(PARAM_CHECK_OUT) ?: ""
            hotelSearchModel.room = uri.getQueryParameter(PARAM_ROOM)?.toInt() ?: 1
            hotelSearchModel.adult = uri.getQueryParameter(PARAM_ADULT)?.toInt() ?: 1

            selectedParam.name = uri.getQueryParameter(PARAM_FILTER_ID) ?: ""
            val values: String = uri.getQueryParameter(PARAM_FILTER_VALUE) ?: ""
            selectedParam.values = values.split(",").toMutableList()

        } else {
            //when activity open from intent
            hotelSearchModel = intent.getParcelableExtra(HotelSearchResultFragment.ARG_HOTEL_SEARCH_MODEL)
                    ?: HotelSearchModel()
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
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            layoutParams = param
        }

        val textView = Typography(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.layoutParams = param
        textView.text = resources.getString(R.string.hotel_search_result_change)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
        textView.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G500))

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
        val checkInString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelSearchModel.checkIn))
        val checkOutString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, hotelSearchModel.checkOut))

        hotel_search_header.title = hotelSearchModel.name
        hotel_search_header.subtitle = getString(R.string.template_search_subtitle,
                checkInString,
                checkOutString,
                hotelSearchModel.room,
                hotelSearchModel.adult)
        hotel_search_header.subheaderView?.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
    }

    private fun changeSearchParameter() {
        if (fragment is HotelSearchResultFragment) {
            (fragment as HotelSearchResultFragment).onClickChangeSearch(hotelSearchModel, SEARCH_SCREEN_NAME)
        }
        startActivityForResult(HotelChangeSearchActivity.getIntent(this,
                hotelSearchModel.id,
                hotelSearchModel.name,
                hotelSearchModel.type,
                hotelSearchModel.lat.toDouble(),
                hotelSearchModel.long.toDouble(),
                hotelSearchModel.checkIn,
                hotelSearchModel.checkOut,
                hotelSearchModel.adult,
                hotelSearchModel.room,
                hotelSearchModel.searchId,
                hotelSearchModel.searchType,
                getString(R.string.hotel_search_result_change_toolbar_title)),
                CHANGE_SEARCH_REQ_CODE)
    }

    private fun checkParameter() {
        val updatedCheckInCheckOutDate = HotelUtils.validateCheckInAndCheckOutDate(hotelSearchModel.checkIn, hotelSearchModel.checkOut)
        hotelSearchModel.checkIn = updatedCheckInCheckOutDate.first
        hotelSearchModel.checkOut = updatedCheckInCheckOutDate.second
    }

    override fun getNewFragment(): Fragment {
        return HotelSearchResultFragment.createInstance(hotelSearchModel, selectedParam)
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
                    hotelSearchModel = HotelSearchModel(
                            id = it.getLongExtra(HotelChangeSearchActivity.DESTINATION_ID, 0),
                            name = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_NAME),
                            type = it.getStringExtra(HotelChangeSearchActivity.DESTINATION_TYPE),
                            lat = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LAT, 0.0),
                            long = it.getDoubleExtra(HotelChangeSearchActivity.DESTINATION_LONG, 0.0),
                            checkIn = it.getStringExtra(HotelChangeSearchActivity.CHECK_IN_DATE),
                            checkOut = it.getStringExtra(HotelChangeSearchActivity.CHECK_OUT_DATE),
                            room = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_ROOMS, 1),
                            adult = it.getIntExtra(HotelChangeSearchActivity.NUM_OF_GUESTS, 0),
                            searchType = it.getStringExtra(HotelChangeSearchActivity.SEARCH_TYPE),
                            searchId = it.getStringExtra(HotelChangeSearchActivity.SEARCH_ID))

                    (fragment as HotelSearchResultFragment).let { searchFragment ->
                        searchFragment.searchResultviewModel.initSearchParam(hotelSearchModel)
                        searchFragment.searchDestinationName = hotelSearchModel.name
                        searchFragment.searchDestinationType = if (hotelSearchModel.searchType.isNotEmpty()) hotelSearchModel.searchType else hotelSearchModel.type

                        setUpTitleAndSubtitle()
                        searchFragment.changeSearchParam()
                    }
                }
            }

        }
    }

    companion object {

        const val CHANGE_SEARCH_REQ_CODE = 101

        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_ROOM = "room"
        const val PARAM_ADULT = "adult"

        //newer version applink param
        const val PARAM_ID = "id"
        const val PARAM_NAME = "name"
        const val PARAM_TYPE = "type"

        const val PARAM_HOTEL_ID = "hotel_id"
        const val PARAM_HOTEL_NAME = "hotel_name"
        const val PARAM_DISTRICT_ID = "district_id"
        const val PARAM_DISTRICT_NAME = "district_name"
        const val PARAM_CITY_ID = "city_id"
        const val PARAM_CITY_NAME = "city_name"
        const val PARAM_REGION_ID = "region_id"
        const val PARAM_REGION_NAME = "region_name"
        const val PARAM_FILTER_ID = "filter_selected_id"
        const val PARAM_FILTER_VALUE = "filter_selected_value"

        const val SEARCH_SCREEN_NAME = "/hotel/searchresult"

        fun createIntent(context: Context, hotelSearchModel: HotelSearchModel): Intent =
                Intent(context, HotelSearchResultActivity::class.java)
                        .putExtra(HotelSearchResultFragment.ARG_HOTEL_SEARCH_MODEL, hotelSearchModel)
    }
}