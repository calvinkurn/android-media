package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityItem
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyImageItem
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailMapActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailMainFacilityAdapter
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailReviewAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import java.util.*
import javax.inject.Inject

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var detailViewModel: HotelDetailViewModel

    private var hotelHomepageModel = HotelHomepageModel()
    private var isButtonEnabled: Boolean = true
    private var hotelName: String = ""

    private lateinit var detailReviewAdapter: HotelDetailReviewAdapter
    private lateinit var mainFacilityAdapter: HotelDetailMainFacilityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            detailViewModel = viewModelProvider.get(HotelDetailViewModel::class.java)
        }

        arguments?.let {
            hotelHomepageModel.locId = it.getInt(HotelDetailActivity.EXTRA_PROPERTY_ID)
            hotelHomepageModel.checkInDate = it.getString(HotelDetailActivity.EXTRA_CHECK_IN_DATE,
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                            TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1)))
            hotelHomepageModel.checkOutDate = it.getString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE,
                    TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                            TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 2)))
            hotelHomepageModel.roomCount = it.getInt(HotelDetailActivity.EXTRA_ROOM_COUNT)
            hotelHomepageModel.adultCount = it.getInt(HotelDetailActivity.EXTRA_ADULT_COUNT, 1)
            isButtonEnabled = it.getBoolean(HotelDetailActivity.EXTRA_ENABLE_BUTTON, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_SEARCH_PARAMETER)) {
            hotelHomepageModel = savedInstanceState.getParcelable(SAVED_SEARCH_PARAMETER)!!
        }

        showLoadingLayout()

        detailViewModel.getHotelDetailData(
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_info),
                GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                hotelHomepageModel.locId,
                hotelHomepageModel)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailViewModel.roomListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupPriceButton(it.data)
                }
                is Fail -> {
                    // TODO Fail get Room List
                }
            }
        })

        detailViewModel.hotelInfoResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupLayout(it.data)
                    hotelName = it.data.property.name
                }
                is Fail -> {
                    // TODO Fail get Hotel Info
                }
            }
        })
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelDetailComponent::class.java).inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SAVED_SEARCH_PARAMETER, hotelHomepageModel)
        outState.putBoolean(SAVED_ENABLE_BUTTON, isButtonEnabled)
    }

    private fun setupLayout(data: PropertyDetailData) {
        hideLoadingLayout()
        (activity as HotelDetailActivity).setSupportActionBar(detail_toolbar)
        (activity as HotelDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar.title = ""
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = data.property.name
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })

        setupMainImage(data.property.images)

        tv_hotel_name.text = data.property.name
        hotel_property_type.text = data.property.typeName
        for (i in 1..data.property.star) {
            hotel_rating_container.addView(RatingStarView(context!!))
        }
        tv_hotel_address.text = data.property.address

        iv_hotel_detail_location.loadImage(data.property.locationImageStatic)

        setupPolicySwitcher(data.property)
        setupImportantInfo(data.property.importantInformation)
        setupDescription(data.property.description)
        setupReviewItem(listOf("", "", "", "", ""))
        setupMainFacilityItem(listOf(FacilityItem(1, "Internet", ""),
                FacilityItem(1, "Kamar Mandi", ""),
                FacilityItem(1, "Air", ""),
                FacilityItem(1, "Listrik", ""),
                FacilityItem(1, "Restoran", ""),
                FacilityItem(1, "Kolam Renang", "")))

        btn_hotel_detail_show.setOnClickListener {
            startActivity(HotelDetailMapActivity.getCallingIntent(context!!, data.property.name,
                    data.property.latitude, data.property.longitude, data.property.address))
        }
    }

    private fun showLoadingLayout() {
        app_bar_layout.visibility = View.GONE
        container_hotel_detail.visibility = View.GONE
        container_hotel_detail_shimmering.visibility = View.VISIBLE
    }

    private fun hideLoadingLayout() {
        app_bar_layout.visibility = View.VISIBLE
        container_hotel_detail.visibility = View.VISIBLE
        container_hotel_detail_shimmering.visibility = View.GONE
    }

    private fun setupMainImage(images: List<PropertyImageItem>) {
        // TODO add action to open preview

        var imageCounter = 0

        loop@ for ((imageIndex, item) in images.withIndex()) {
            when (imageCounter) {
                0 -> {
                    // do nothing, preventing break if mainPhoto not in the first item
                }
                1 -> {
                    iv_first_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    imageCounter++
                }
                2 -> {
                    iv_second_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    imageCounter++
                }
                3 -> {
                    iv_third_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    imageCounter++
                }
                else -> {
                    break@loop
                }
            }
            if (item.mainPhoto) {
                iv_main_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                imageCounter++
            }
        }

        if (images.size - imageCounter > 0) {
            tv_more_image_counter.text = getString(R.string.hotel_detail_more_image_counter, images.size - imageCounter)
        }
    }

    private fun setupReviewItem(reviewList: List<String>) {
        if (!::detailReviewAdapter.isInitialized) {
            detailReviewAdapter = HotelDetailReviewAdapter(reviewList)
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_best_review.layoutManager = layoutManager
        rv_best_review.setHasFixedSize(true)
        rv_best_review.isNestedScrollingEnabled = false
        rv_best_review.adapter = detailReviewAdapter
    }

    private fun setupMainFacilityItem(facilityList: List<FacilityItem>) {
        if (!::mainFacilityAdapter.isInitialized) {
            mainFacilityAdapter = HotelDetailMainFacilityAdapter(facilityList)
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_hotel_facilities.layoutManager = layoutManager
        rv_hotel_facilities.setHasFixedSize(true)
        rv_hotel_facilities.isNestedScrollingEnabled = false
        rv_hotel_facilities.adapter = mainFacilityAdapter
    }

    private fun setupImportantInfo(importantInfo: String) {
        if (importantInfo.isNotEmpty()) {
            tv_hotel_important_info.text = importantInfo
        } else {
            container_important_info.visibility = View.GONE
        }
    }

    private fun setupDescription(description: String) {
        if (description.isNotEmpty()) {
            tv_hotel_description.text = description
        } else {
            container_hotel_description.visibility = View.GONE
        }
    }

    private fun setupPolicySwitcher(property: PropertyData) {
        if (property.checkinTo.isNotEmpty()) {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_from_to, property.checkInFrom, property.checkinTo))
        } else {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_start_from, property.checkInFrom))
        }

        if (property.checkoutFrom.isNotEmpty()) {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_from_to, property.checkoutFrom, property.checkoutTo))
        } else {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_to, property.checkoutTo))
        }
    }

    private fun setupPriceButton(data: List<HotelRoom>) {
        if (data.isNotEmpty()) {
            tv_hotel_price.text = data[0].roomPrice[0].roomPrice
        }
        btn_see_room.setOnClickListener {
            startActivityForResult(HotelRoomListActivity.createInstance(context!!, hotelHomepageModel.locId, hotelName,
                    hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate, hotelHomepageModel.adultCount, 0,
                    hotelHomepageModel.roomCount), RESULT_ROOM_LIST)
        }
    }

    companion object {

        const val SAVED_SEARCH_PARAMETER = "SAVED_SEARCH_PARAMETER"
        const val SAVED_ENABLE_BUTTON = "SAVED_ENABLE_BUTTON"

        const val RESULT_ROOM_LIST = 101

        fun getInstance(checkInDate: String, checkOutDate: String, propertyId: Int, roomCount: Int,
                        adultCount: Int, enableButton: Boolean = true): HotelDetailFragment =
                HotelDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelDetailActivity.EXTRA_CHECK_IN_DATE, checkInDate)
                        putString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE, checkOutDate)
                        putInt(HotelDetailActivity.EXTRA_PROPERTY_ID, propertyId)
                        putInt(HotelDetailActivity.EXTRA_ROOM_COUNT, roomCount)
                        putInt(HotelDetailActivity.EXTRA_ADULT_COUNT, adultCount)
                        putBoolean(HotelDetailActivity.EXTRA_ENABLE_BUTTON, enableButton)
                    }
                }

    }
}