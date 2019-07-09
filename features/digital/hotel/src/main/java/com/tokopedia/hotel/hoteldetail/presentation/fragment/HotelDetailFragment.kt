package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyImageItem
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailAllFacilityActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailMapActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelReviewActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailMainFacilityAdapter
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailReviewAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import java.util.*
import javax.inject.Inject


/**
 * @author by furqan on 22/04/19
 */
class HotelDetailFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var detailViewModel: HotelDetailViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private var hotelHomepageModel = HotelHomepageModel()
    private var isButtonEnabled: Boolean = true
    private var hotelName: String = ""
    private var hotelId: Int = 0
    private var roomPrice: String = "0"

    private val thumbnailImageList = mutableListOf<String>()
    private val imageList = mutableListOf<String>()

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

            if (it.getString(HotelDetailActivity.EXTRA_CHECK_IN_DATE).isNotEmpty()) {
                hotelHomepageModel.checkInDate = it.getString(HotelDetailActivity.EXTRA_CHECK_IN_DATE,
                        TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1)))
                hotelHomepageModel.checkOutDate = it.getString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE,
                        TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 2)))
                hotelHomepageModel.roomCount = it.getInt(HotelDetailActivity.EXTRA_ROOM_COUNT)
                hotelHomepageModel.adultCount = it.getInt(HotelDetailActivity.EXTRA_ADULT_COUNT, 1)
            }
            isButtonEnabled = hotelHomepageModel.checkInDate.isNotEmpty()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_SEARCH_PARAMETER)) {
            hotelHomepageModel = savedInstanceState.getParcelable(SAVED_SEARCH_PARAMETER)!!
            isButtonEnabled = savedInstanceState.getBoolean(SAVED_ENABLE_BUTTON)
        }

        showLoadingLayout()

        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_info),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_get_hotel_review),
                    hotelHomepageModel.locId,
                    hotelHomepageModel)
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_info),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_get_hotel_review),
                    hotelHomepageModel.locId)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailViewModel.roomListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupPriceButton(it.data)
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })

        detailViewModel.hotelInfoResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupLayout(it.data)
                    hotelName = it.data.property.name
                    hotelId = it.data.property.id
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })

        detailViewModel.hotelReviewResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    setupReviewLayout(it.data)
                }
                is Fail -> {
                    showErrorState(it.throwable)
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

        val navIcon = detail_toolbar.navigationIcon
        navIcon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        (activity as HotelDetailActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

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
                    navIcon?.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
                    isShow = false
                }

                (activity as HotelDetailActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)
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

        setupPolicySwitcher(data)
        setupImportantInfo(data)
        setupDescription(data)
        setupMainFacilityItem(data)

        btn_hotel_detail_show.setOnClickListener {
            startActivity(HotelDetailMapActivity.getCallingIntent(context!!, data.property.name,
                    data.property.latitude, data.property.longitude, data.property.address))
        }

        if (!isButtonEnabled) {
            container_shimmering_bottom.visibility = View.GONE
            container_bottom.visibility = View.GONE
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
        var imageCounter = 0

        for ((imageIndex, item) in images.withIndex()) {
            imageList.add(item.urlOriginal)
            thumbnailImageList.add(item.urlSquare6)

            when (imageCounter) {
                0 -> {
                    // do nothing, preventing break if mainPhoto not in the first item
                }
                1 -> {
                    iv_first_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_first_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageIndex)
                    }
                    imageCounter++
                }
                2 -> {
                    iv_second_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_second_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageIndex)
                    }
                    imageCounter++
                }
                3 -> {
                    iv_third_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_third_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageIndex)
                    }
                    imageCounter++
                }
            }
            if (item.mainPhoto) {
                iv_main_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                iv_main_photo_preview.setOnClickListener {
                    onPhotoClicked()
                    openImagePreview(imageIndex)
                }
                imageCounter++
            }
        }

        if (images.size - imageCounter > 0) {
            tv_more_image_counter.text = getString(R.string.hotel_detail_more_image_counter, images.size - imageCounter)
        }
    }

    private fun onPhotoClicked() {
        trackingHotelUtil.hotelClickHotelPhoto(hotelId, roomPrice)
    }

    private fun setupReviewLayout(data: HotelReview.ReviewData) {
        container_hotel_review.visibility = View.VISIBLE
        setupReviewHeader(data)
        setupReviewItem(data.reviewList)
    }

    private fun setupReviewHeader(data: HotelReview.ReviewData) {
        if (data.totalReview > 0 || data.averageScoreReview > 0) {
            if (data.totalReview > 0) {
                tv_hotel_rating_count.text = getString(R.string.hotel_detail_based_on_review_number,
                        CurrencyFormatUtil.convertPriceValue(data.totalReview.toDouble(), false))
                tv_hotel_rating_detail.text = data.headline
            } else {
                tv_hotel_rating_count.visibility = View.GONE
            }

            if (data.averageScoreReview > 0) {
                tv_hotel_rating_number.text = data.averageScoreReview.toString()
                tv_hotel_rating_detail.text = data.headline
            } else {
                tv_hotel_rating_number.visibility = View.GONE
                tv_hotel_rating_detail.text = getString(R.string.hotel_detail_no_rating)
            }
        } else {
            tv_hotel_rating_number.visibility = View.GONE
            tv_hotel_rating_count.visibility = View.GONE
            tv_hotel_detail_all_promo.visibility = View.GONE
            tv_hotel_rating_detail.text = getString(R.string.hotel_detail_no_rating_review)
        }
    }

    private fun setupReviewItem(reviewList: List<HotelReview>) {
        if (reviewList.isNotEmpty()) {
            if (!::detailReviewAdapter.isInitialized) {
                detailReviewAdapter = HotelDetailReviewAdapter(reviewList)
            }

            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            rv_best_review.layoutManager = layoutManager
            rv_best_review.setHasFixedSize(true)
            rv_best_review.isNestedScrollingEnabled = false
            rv_best_review.adapter = detailReviewAdapter

            tv_hotel_detail_all_promo.setOnClickListener {
                trackingHotelUtil.hotelClickHotelReviews(hotelId, roomPrice)
                startActivityForResult(HotelReviewActivity.getCallingIntent(context!!, hotelHomepageModel.locId), RESULT_REVIEW)
            }
        } else {
            rv_best_review.visibility = View.GONE
        }
    }

    private fun setupMainFacilityItem(data: PropertyDetailData) {
        if (!::mainFacilityAdapter.isInitialized) {
            mainFacilityAdapter = HotelDetailMainFacilityAdapter(data.mainFacility)
        }

        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_hotel_facilities.layoutManager = layoutManager
        rv_hotel_facilities.setHasFixedSize(true)
        rv_hotel_facilities.isNestedScrollingEnabled = false
        rv_hotel_facilities.adapter = mainFacilityAdapter

        tv_hotel_detail_all_facilities.setOnClickListener {
            startActivity(HotelDetailAllFacilityActivity.getCallingIntent(context!!, hotelName,
                    HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.FACILITY_TITLE))
        }
    }

    private fun setupImportantInfo(data: PropertyDetailData) {
        if (data.property.importantInformation.isNotEmpty()) {
            tv_hotel_important_info.text = data.property.importantInformation
            tv_hotel_important_info_more.setOnClickListener {
                startActivity(HotelDetailAllFacilityActivity.getCallingIntent(context!!, hotelName,
                        HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE))
            }
        } else {
            container_important_info.visibility = View.GONE
        }
    }

    private fun setupDescription(data: PropertyDetailData) {
        if (data.property.description.isNotEmpty()) {
            tv_hotel_description.text = data.property.description
            tv_hotel_description_more.setOnClickListener {
                startActivity(HotelDetailAllFacilityActivity.getCallingIntent(context!!, hotelName,
                        HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.DESCRIPTION_TITLE))
            }
        } else {
            container_hotel_description.visibility = View.GONE
        }
    }

    private fun setupPolicySwitcher(data: PropertyDetailData) {
        scv_hotel_date.setLeftSubtitleText(data.property.checkinInfo)
        scv_hotel_date.setRightSubtitleText(data.property.checkoutInfo)

        if (data.property.checkinTo.isNotEmpty()) {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_from_to, data.property.checkInFrom, data.property.checkinTo))
        } else {
            scv_hotel_date.setLeftTitleText(getString(R.string.hotel_detail_check_start_from, data.property.checkInFrom))
        }

        if (data.property.checkoutFrom.isNotEmpty()) {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_from_to, data.property.checkoutFrom, data.property.checkoutTo))
        } else {
            scv_hotel_date.setRightTitleText(getString(R.string.hotel_detail_check_to, data.property.checkoutTo))
        }

        tv_hotel_detail_all_policies.setOnClickListener {
            startActivity(HotelDetailAllFacilityActivity.getCallingIntent(context!!, hotelName,
                    HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.POLICY_TITLE))
        }
    }

    private fun setupPriceButton(data: List<HotelRoom>) {
        container_shimmering_bottom.visibility = View.GONE
        container_bottom.visibility = View.VISIBLE

        if (data.isNotEmpty()) {
            trackingHotelUtil.hotelViewDetails(hotelName, hotelId, true, data[0].roomPrice.priceAmount.toInt(), data[0].additionalPropertyInfo.isDirectPayment)
            roomPrice = data[0].roomPrice.roomPrice
            tv_hotel_price.text = roomPrice

            if (data[0].additionalPropertyInfo.isDirectPayment) {
                btn_see_room.text = getString(R.string.hotel_detail_coming_soon_text)
                btn_see_room.isEnabled = false
                btn_see_room.buttonCompatType = ButtonCompat.DISABLE
            } else {
                btn_see_room.setOnClickListener {
                    trackingHotelUtil.hotelChooseViewRoom(hotelId, roomPrice)
                    startActivityForResult(HotelRoomListActivity.createInstance(context!!, hotelHomepageModel.locId, hotelName,
                            hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate, hotelHomepageModel.adultCount, 0,
                            hotelHomepageModel.roomCount), RESULT_ROOM_LIST)
                }
            }
        } else {
            trackingHotelUtil.hotelViewDetails(hotelName, hotelId, false, 0, false)
            tv_hotel_price_subtitle.visibility = View.GONE
            tv_hotel_price.text = getString(R.string.hotel_detail_room_full_text)
            tv_hotel_price.setTextColor(ContextCompat.getColor(context!!, com.tokopedia.design.R.color.light_disabled))
            btn_see_room.buttonCompatType = ButtonCompat.PRIMARY
            btn_see_room.text = getString(R.string.hotel_detail_change_search_text)
            btn_see_room.setOnClickListener {
                activity!!.finish()
            }
        }

        if (!isButtonEnabled) {
            btn_see_room.isEnabled = false
            btn_see_room.buttonCompatType = ButtonCompat.DISABLE
        }
    }

    private fun openImagePreview(index: Int) {
        startActivity(ImagePreviewSliderActivity.getCallingIntent(context!!, hotelName, imageList, thumbnailImageList, index))
    }

    override fun onErrorRetryClicked() {
        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_info),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_room_list),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_get_hotel_review),
                    hotelHomepageModel.locId,
                    hotelHomepageModel)
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                    GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_info),
                    GraphqlHelper.loadRawString(resources, R.raw.gql_get_hotel_review),
                    hotelHomepageModel.locId)
        }
    }

    companion object {

        const val SAVED_SEARCH_PARAMETER = "SAVED_SEARCH_PARAMETER"
        const val SAVED_ENABLE_BUTTON = "SAVED_ENABLE_BUTTON"

        const val RESULT_ROOM_LIST = 101
        const val RESULT_REVIEW = 102

        fun getInstance(checkInDate: String, checkOutDate: String, propertyId: Int, roomCount: Int,
                        adultCount: Int): HotelDetailFragment =
                HotelDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelDetailActivity.EXTRA_CHECK_IN_DATE, checkInDate)
                        putString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE, checkOutDate)
                        putInt(HotelDetailActivity.EXTRA_PROPERTY_ID, propertyId)
                        putInt(HotelDetailActivity.EXTRA_ROOM_COUNT, roomCount)
                        putInt(HotelDetailActivity.EXTRA_ADULT_COUNT, adultCount)
                    }
                }

    }
}