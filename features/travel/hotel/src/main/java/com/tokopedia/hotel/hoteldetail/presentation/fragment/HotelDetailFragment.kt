package com.tokopedia.hotel.hoteldetail.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelSourceEnum
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.common.util.HotelStringUtils
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_PDP
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity
import com.tokopedia.hotel.globalsearch.presentation.widget.HotelGlobalSearchWidget
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyImageItem
import com.tokopedia.hotel.hoteldetail.data.entity.PropertySafetyBadge
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailActivity.Companion.PDP_SCREEN_NAME
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelDetailAllFacilityActivity
import com.tokopedia.hotel.hoteldetail.presentation.activity.HotelReviewActivity
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailMainFacilityAdapter
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailReviewAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.util.HotelShare
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mapviewer.activity.MapViewerActivity
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_detail.*
import kotlinx.android.synthetic.main.item_network_error_view.*
import java.util.*
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.round

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailFragment : HotelBaseFragment(), HotelGlobalSearchWidget.GlobalSearchListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var detailViewModel: HotelDetailViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false
    private var isRoomListLoaded = false
    private var isHotelInfoLoaded = false
    private var isHotelReviewLoaded = false

    private var hotelHomepageModel = HotelHomepageModel()
    private var isButtonEnabled: Boolean = true
    private var hotelName: String = ""
    private var hotelId: Long = 0
    private var roomPrice: String = "0"
    private var roomPriceAmount: String = ""
    private var isDirectPayment: Boolean = true
    private var source: String = HotelSourceEnum.SEARCHRESULT.value
    private var isPromo: Boolean = false

    private var isHotelDetailSuccess: Boolean = true
    private var isHotelReviewSuccess: Boolean = true
    private var isRoomListSuccess: Boolean = true

    private val thumbnailImageList = mutableListOf<String>()
    private val imageList = mutableListOf<String>()

    private lateinit var detailReviewAdapter: HotelDetailReviewAdapter
    private lateinit var mainFacilityAdapter: HotelDetailMainFacilityAdapter

    private var loadingProgressDialog: ProgressDialog? = null
    private var isTickerValid = false
    private var isScrolled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_PDP)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            detailViewModel = viewModelProvider.get(HotelDetailViewModel::class.java)
        }

        arguments?.let {
            hotelHomepageModel.locId = it.getLong(HotelDetailActivity.EXTRA_PROPERTY_ID)
            source = it.getString(HotelDetailActivity.EXTRA_SOURCE)
                    ?: HotelSourceEnum.SEARCHRESULT.value

            if (it.getString(HotelDetailActivity.EXTRA_CHECK_IN_DATE)?.isNotEmpty() == true) {
                hotelHomepageModel.checkInDate = it.getString(HotelDetailActivity.EXTRA_CHECK_IN_DATE,
                        TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 1)))
                hotelHomepageModel.checkOutDate = it.getString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE,
                        TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.addTimeToSpesificDate(
                                TravelDateUtil.getCurrentCalendar().time, Calendar.DATE, 2)))
                hotelHomepageModel.roomCount = it.getInt(HotelDetailActivity.EXTRA_ROOM_COUNT)
                hotelHomepageModel.adultCount = it.getInt(HotelDetailActivity.EXTRA_ADULT_COUNT, 1)
                hotelHomepageModel.locName = it.getString(HotelDetailActivity.EXTRA_DESTINATION_NAME, "")
                hotelHomepageModel.locType = it.getString(HotelDetailActivity.EXTRA_DESTINATION_TYPE, HotelTypeEnum.PROPERTY.value)
                isDirectPayment = it.getBoolean(HotelDetailActivity.EXTRA_IS_DIRECT_PAYMENT, true)
            }
            isButtonEnabled = it.getBoolean(EXTRA_SHOW_ROOM, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_SEARCH_PARAMETER)) {
            hotelHomepageModel = savedInstanceState.getParcelable(SAVED_SEARCH_PARAMETER)
                    ?: HotelHomepageModel()
            isButtonEnabled = savedInstanceState.getBoolean(SAVED_ENABLE_BUTTON)
        }

        showLoadingLayout()

        detailViewModel.fetchTickerData()

        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                    HotelGqlQuery.PROPERTY_DETAIL,
                    HotelGqlQuery.PROPERTY_ROOM_LIST,
                    HotelGqlQuery.PROPERTY_REVIEW,
                    hotelHomepageModel.locId,
                    hotelHomepageModel, source)
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                    HotelGqlQuery.PROPERTY_DETAIL,
                    HotelGqlQuery.PROPERTY_REVIEW,
                    hotelHomepageModel.locId, source)
        }

        setupGlobalSearchWidget()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailViewModel.roomListResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    isRoomListSuccess = true
                    setupPriceButton(it.data)
                }
                is Fail -> {
                    isRoomListSuccess = false
                    showErrorView(it.throwable)
                }
            }
            isRoomListLoaded = true
            stopTrace()
        })

        detailViewModel.hotelInfoResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    isHotelDetailSuccess = true
                    setupLayout(it.data)
                    hotelName = it.data.property.name
                    hotelId = it.data.property.id
                }
                is Fail -> {
                    isHotelDetailSuccess = false
                    showErrorView(it.throwable)
                }
            }
            isHotelInfoLoaded = true
            stopTrace()
        })

        detailViewModel.hotelReviewResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    isHotelReviewSuccess = true
                    setupReviewLayout(it.data)
                }
                is Fail -> {
                    isHotelReviewSuccess = false
                    showErrorView(it.throwable)
                }
            }
            isHotelReviewLoaded = true
            stopTrace()
        })

        detailViewModel.tickerData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.message.isNotEmpty()) {
                        renderTickerView(it.data)
                    } else {
                        hideTickerView()
                    }
                }
                is Fail -> {
                    hideTickerView()
                }
            }
        })
    }

    private fun hideTickerView() {
        hotelDetailTicker.hide()
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        isTickerValid = true
        if (travelTickerModel.title.isNotEmpty()) hotelDetailTicker.tickerTitle = travelTickerModel.title
        var message = travelTickerModel.message
        if (travelTickerModel.url.isNotEmpty()) message += getString(R.string.hotel_ticker_desc, travelTickerModel.url)
        hotelDetailTicker.setHtmlDescription(message)
        hotelDetailTicker.tickerType = Ticker.TYPE_WARNING
        hotelDetailTicker.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {
                isTickerValid = false
            }
        })

        if (travelTickerModel.url.isNotEmpty()) {
            hotelDetailTicker.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        hotelDetailTicker.show()
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

    override fun onClick(intent: Intent) {
        startActivityForResult(intent, REQUEST_CODE_GLOBAL_SEARCH)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_GLOBAL_SEARCH -> if (resultCode == Activity.RESULT_OK) {
                data?.let {
                    hotelHomepageModel.apply {
                        if (it.hasExtra(HotelGlobalSearchActivity.CHECK_IN_DATE)) checkInDate = it.getStringExtra(HotelGlobalSearchActivity.CHECK_IN_DATE)
                        if (it.hasExtra(HotelGlobalSearchActivity.CHECK_OUT_DATE)) checkOutDate = it.getStringExtra(HotelGlobalSearchActivity.CHECK_OUT_DATE)
                        if (it.hasExtra(HotelGlobalSearchActivity.NUM_OF_ROOMS)) roomCount = it.getIntExtra(HotelGlobalSearchActivity.NUM_OF_ROOMS, 1)
                        if (it.hasExtra(HotelGlobalSearchActivity.NUM_OF_GUESTS)) adultCount = it.getIntExtra(HotelGlobalSearchActivity.NUM_OF_GUESTS, 1)
                    }
                    showLoadingContainerBottom()
                    hideRoomAvailableContainerBottom()
                    hideRoomNotAvailableContainerBottom()
                    detailViewModel.getRoomWithoutHotelData(
                            HotelGqlQuery.PROPERTY_ROOM_LIST,
                            hotelHomepageModel)
                }
            }
        }
    }

    private fun showErrorView(e: Throwable) {
        if (!isHotelDetailSuccess && !isHotelReviewSuccess && !isRoomListSuccess) {
            stopTrace()

            container_content.visibility = View.GONE
            container_error.visibility = View.VISIBLE

            iv_icon.setImageResource(ErrorHandlerHotel.getErrorImage(e))
            message_retry.text = ErrorHandlerHotel.getErrorTitle(context, e)
            sub_message_retry.text = ErrorHandlerHotel.getErrorMessage(context, e)

            button_retry.setOnClickListener {
                hideErrorView()
                onErrorRetryClicked()
            }
        }
    }

    private fun hideErrorView() {
        container_content.visibility = View.VISIBLE
        container_error.visibility = View.GONE
    }

    private fun setupLayout(data: PropertyDetailData) {
        hideLoadingLayout()
        (activity as HotelDetailActivity).setSupportActionBar(detail_toolbar)
        (activity as HotelDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        detail_toolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.hotelPdpExpandedToolbarLayoutTitleColor)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.hotelPdpCollapsingToolbarLayoutTitleColor)
        collapsingToolbar.title = data.property.name

        app_bar_layout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (abs(verticalOffset) >= appBarLayout.totalScrollRange && !isScrolled) {
                if (isTickerValid) hotelDetailTicker.hide()
                detail_toolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N700_96), PorterDuff.Mode.SRC_ATOP)
                (activity as HotelDetailActivity).optionMenu?.setIcon(com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_black)
                isScrolled = true
            } else if (abs(verticalOffset) == 0 && isScrolled) {
                if (isTickerValid) hotelDetailTicker.show()
                detail_toolbar.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
                (activity as HotelDetailActivity).optionMenu?.setIcon(com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_white)
                isScrolled = false
            }
        })

        setupMainImage(data.property.images)

        tv_hotel_name.text = data.property.name
        hotel_property_type.text = data.property.typeName
        for (i in 1..data.property.star) {
            context?.run { hotel_rating_container.addView(RatingStarView(this)) }
        }
        tv_hotel_address.text = data.property.address

        iv_hotel_detail_location.loadImage(data.property.locationImageStatic)

        setupSafetyBadgeLayout(data.safetyBadge)
        setupPolicySwitcher(data)
        setupImportantInfo(data)
        setupDescription(data)
        setupMainFacilityItem(data)
        setupShareLink(data)

        btn_hotel_detail_show.setOnClickListener {
            context?.run {
                startActivity(MapViewerActivity.getCallingIntent(this, data.property.name,
                        data.property.latitude, data.property.longitude, data.property.address, HOTEL_PIN))
            }
        }

        if (!isButtonEnabled) {
            hideLoadingContainerBottom()
            hideRoomNotAvailableContainerBottom()
            hideRoomAvailableContainerBottom()
        }
    }

    private fun setupSafetyBadgeLayout(propertySafetyBadge: PropertySafetyBadge) {
        if (propertySafetyBadge.isShow) {
            hotel_safety_information_layout.show()
            if (propertySafetyBadge.title.isNotEmpty()) {
                tv_hotel_safety_information_title.text = propertySafetyBadge.title
                iv_hotel_safety_badge_icon.loadImage(propertySafetyBadge.icon.light)
            } else {
                tv_hotel_safety_information_title.hide()
                iv_hotel_safety_badge_icon.hide()
            }

            if (propertySafetyBadge.content.isNotEmpty()) {
                tv_hotel_safety_information_content.text = propertySafetyBadge.content
            } else tv_hotel_safety_information_content.hide()

        } else hotel_safety_information_layout.hide()
    }

    private fun setupShareLink(propertyDetailData: PropertyDetailData) {
        hotel_share_button.setOnClickListener {
            trackingHotelUtil.clickShareUrl(requireContext(), PDP_SCREEN_NAME, hotelId.toString(), roomPriceAmount)
            activity?.run {
                HotelShare(this).shareEvent(propertyDetailData, isPromo,
                        { showProgressDialog() },
                        { hideProgressDialog() },
                        this.applicationContext)
            }
        }
    }

    private fun showProgressDialog() {
        if (loadingProgressDialog == null) {
            loadingProgressDialog = activity?.createDefaultProgressDialog(getString(R.string.hotel_detail_share_loading), false, null)
        }
        loadingProgressDialog?.run {
            if (!isShowing) {
                show()
            }
        }
    }

    private fun hideProgressDialog() {
        if (loadingProgressDialog != null && loadingProgressDialog?.isShowing == true) {
            loadingProgressDialog?.dismiss()
        }
    }

    private fun showLoadingLayout() {
        app_bar_layout.visibility = View.GONE
        hotelDetailNestedScrollView.visibility = View.GONE
        container_hotel_detail_shimmering.visibility = View.VISIBLE
    }

    private fun hideLoadingLayout() {
        app_bar_layout.visibility = View.VISIBLE
        hotelDetailNestedScrollView.visibility = View.VISIBLE
        container_hotel_detail_shimmering.visibility = View.GONE
    }

    private fun setupMainImage(images: List<PropertyImageItem>) {
        var imageCounter = 0

        for ((imageIndex, item) in images.withIndex()) {
            imageList.add(item.urlOriginal)
            thumbnailImageList.add(item.urlMax300)

            when (imageCounter) {
                0 -> {
                    // do nothing, preventing break if mainPhoto not in the first item
                }
                1 -> {
                    iv_first_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_first_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, iv_first_photo_preview)
                    }
                    imageCounter++
                }
                2 -> {
                    iv_second_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_second_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, iv_second_photo_preview)
                    }
                    imageCounter++
                }
                3 -> {
                    iv_third_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                    iv_third_photo_preview.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, iv_third_photo_preview)
                    }
                    imageCounter++
                }
            }
            if (item.mainPhoto) {
                iv_main_photo_preview.loadImage(item.urlMax300, R.drawable.ic_failed_load_image)
                iv_main_photo_preview.setOnClickListener {
                    onPhotoClicked()
                    openImagePreview(imageList, imageIndex, iv_main_photo_preview)
                }
                imageCounter++
            }
        }

        if (images.size - imageCounter > 0) {
            tv_more_image_counter.text = getString(R.string.hotel_detail_more_image_counter, images.size - imageCounter)
        }
    }

    private fun onPhotoClicked() {
        trackingHotelUtil.hotelClickHotelPhoto(context, hotelId, roomPriceAmount, PDP_SCREEN_NAME)
    }

    private fun setupReviewLayout(data: HotelReview.ReviewData) {
        container_hotel_review.visibility = View.VISIBLE
        setupReviewHeader(data)
        setupReviewItem(data.reviewList)
    }

    private fun setupReviewHeader(data: HotelReview.ReviewData) {
        if (data.totalReview > 0 || data.averageScoreReview > 0) {
            var hasHeadline = false
            if (data.totalReview > 0) {
                tv_hotel_rating_count.text = getString(R.string.hotel_detail_based_on_review_number,
                        HotelStringUtils.convertPriceValue(data.totalReview.toDouble(), false))
                tv_hotel_rating_detail.text = data.headline
                hasHeadline = true
            } else if (!hasHeadline) {
                tv_hotel_rating_count.visibility = View.GONE
            }

            if (data.averageScoreReview > 0) {
                tv_hotel_rating_number.text = data.averageScoreReview.toString()
                tv_hotel_rating_detail.text = data.headline
            } else if (!hasHeadline) {
                tv_hotel_rating_number.visibility = View.GONE
                tv_hotel_rating_detail.text = getString(R.string.hotel_detail_no_rating)
            }
        } else {
            tv_hotel_rating_number.visibility = View.GONE
            tv_hotel_rating_count.visibility = View.GONE
            tv_hotel_detail_all_reviews.visibility = View.GONE
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

            tv_hotel_detail_all_reviews.setOnClickListener {
                trackingHotelUtil.hotelClickHotelReviews(context, hotelId, roomPriceAmount, PDP_SCREEN_NAME)
                context?.run {
                    startActivityForResult(HotelReviewActivity.getCallingIntent(this, hotelHomepageModel.locId), RESULT_REVIEW)
                }
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
            context?.run {
                startActivity(HotelDetailAllFacilityActivity.getCallingIntent(this, hotelName,
                        HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.FACILITY_TITLE))
            }
        }
    }

    private fun setupImportantInfo(data: PropertyDetailData) {
        if (data.property.importantInformation.isNotEmpty()) {
            tv_hotel_important_info.text = data.property.importantInformation
            tv_hotel_important_info_more.setOnClickListener {
                context?.run {
                    startActivity(HotelDetailAllFacilityActivity.getCallingIntent(this, hotelName,
                            HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE))
                }
            }
        } else {
            container_important_info.visibility = View.GONE
        }
    }

    private fun setupDescription(data: PropertyDetailData) {
        if (data.property.description.isNotEmpty()) {
            tv_hotel_description.text = data.property.description
            tv_hotel_description_more.setOnClickListener {
                context?.run {
                    startActivity(HotelDetailAllFacilityActivity.getCallingIntent(this, hotelName,
                            HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.DESCRIPTION_TITLE))
                }
            }
        } else {
            container_hotel_description.visibility = View.GONE
        }
    }

    private fun setupPolicySwitcher(data: PropertyDetailData) {

        scv_hotel_date.setLeftTitleText(data.property.checkinInfo)
        scv_hotel_date.setRightTitleText(data.property.checkoutInfo)

        tv_hotel_detail_all_policies.setOnClickListener {
            context?.run {
                startActivity(HotelDetailAllFacilityActivity.getCallingIntent(this, hotelName,
                        HotelDetailAllFacilityModel.transform(data), HotelDetailAllFacilityFragment.POLICY_TITLE))
            }
        }
    }

    private fun setupPriceButton(data: List<HotelRoom>) {
        hideLoadingContainerBottom()

        var isAvailable = false
        if (data.isNotEmpty()) {
            showRoomAvailableContainerBottom()
            roomPrice = data.first().roomPrice.roomPrice
            roomPriceAmount = round(data.first().roomPrice.priceAmount).toLong().toString()
            tv_hotel_price.text = roomPrice

            var hotelDetailTag = data.first().additionalPropertyInfo.hotelTagging
            if (hotelDetailTag.isNotEmpty()) {
                hotel_detail_tag.show()
                hotel_detail_tag.text = hotelDetailTag
                isPromo = true
            } else hotel_detail_tag.hide()

            if (data[0].additionalPropertyInfo.isEnabled) {
                isAvailable = true

                btn_see_room.text = getString(R.string.hotel_detail_show_room_text)
                btn_see_room.buttonType = UnifyButton.Type.TRANSACTION
                btn_see_room.setOnClickListener {
                    trackingHotelUtil.hotelChooseViewRoom(context, hotelHomepageModel, hotelId, hotelName, PDP_SCREEN_NAME)
                    context?.run {
                        startActivityForResult(HotelRoomListActivity.createInstance(this, hotelHomepageModel.locId, hotelName,
                                hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate, hotelHomepageModel.adultCount, 0,
                                hotelHomepageModel.roomCount, hotelHomepageModel.locType, hotelHomepageModel.locName), RESULT_ROOM_LIST)
                    }
                }
            } else {
                btn_see_room.text = getString(R.string.hotel_detail_coming_soon_text)
                btn_see_room.isEnabled = false
            }
        } else {
            showRoomNotAvailableContainerBottom()
        }

        trackingHotelUtil.hotelViewDetails(context, hotelHomepageModel, hotelName, hotelId, isAvailable,
                ceil(data.firstOrNull()?.roomPrice?.priceAmount ?: 0.0).toInt().toString(),
                data.firstOrNull()?.additionalPropertyInfo?.isDirectPayment
                        ?: isDirectPayment, PDP_SCREEN_NAME)

        if (!isButtonEnabled) {
            btn_see_room.isEnabled = false
        }

        setupGlobalSearchWidget()
    }

    private fun setupGlobalSearchWidget() {
        // setup hotel global search widget
        // add condition if checkin date & checkout date isNotEmpty, to prevent crash access hotel detail from applink
        if (hotelHomepageModel.checkInDate.isNotEmpty() &&
                hotelHomepageModel.checkOutDate.isNotEmpty()) {
            widget_hotel_global_search.title = hotelName
            widget_hotel_global_search.globalSearchListener = this
            widget_hotel_global_search.setPreferencesData(hotelHomepageModel.checkInDate,
                    hotelHomepageModel.checkOutDate, hotelHomepageModel.adultCount, hotelHomepageModel.roomCount)
            widget_hotel_global_search.buildView()
        } else {
            widget_hotel_global_search.hide()
        }
        if (!isButtonEnabled) widget_hotel_global_search.hide()
    }

    private fun openImagePreview(imageList: MutableList<String>, index: Int, imageViewTransitionFrom: ImageView?) {
        ImagePreviewSlider.instance.start(context, hotelName, imageList, thumbnailImageList, index, imageViewTransitionFrom)
    }

    override fun onErrorRetryClicked() {
        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                    HotelGqlQuery.PROPERTY_DETAIL,
                    HotelGqlQuery.PROPERTY_ROOM_LIST,
                    HotelGqlQuery.PROPERTY_REVIEW,
                    hotelHomepageModel.locId,
                    hotelHomepageModel, source)
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                    HotelGqlQuery.PROPERTY_DETAIL,
                    HotelGqlQuery.PROPERTY_REVIEW,
                    hotelHomepageModel.locId, source)
        }
    }

    private fun showLoadingContainerBottom() {
        container_shimmering_bottom.visibility = View.VISIBLE
    }

    private fun hideLoadingContainerBottom() {
        container_shimmering_bottom.visibility = View.GONE
    }

    private fun showRoomAvailableContainerBottom() {
        container_room_available.visibility = View.VISIBLE
    }

    private fun hideRoomAvailableContainerBottom() {
        container_room_available.visibility = View.GONE
    }

    private fun showRoomNotAvailableContainerBottom() {
        container_room_not_available.visibility = View.VISIBLE
    }

    private fun hideRoomNotAvailableContainerBottom() {
        container_room_not_available.visibility = View.GONE
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            if (isHotelInfoLoaded && isHotelReviewLoaded && isRoomListLoaded) {
                performanceMonitoring?.stopTrace()
                isTraceStop = true
            }
        }
    }

    companion object {

        const val REQUEST_CODE_GLOBAL_SEARCH = 103

        const val SAVED_SEARCH_PARAMETER = "SAVED_SEARCH_PARAMETER"
        const val SAVED_ENABLE_BUTTON = "SAVED_ENABLE_BUTTON"

        const val EXTRA_SHOW_ROOM = "EXTRA_SHOW_ROOM"

        const val HOTEL_PIN = "HOTEL_PIN"

        const val RESULT_ROOM_LIST = 101
        const val RESULT_REVIEW = 102

        fun getInstance(checkInDate: String, checkOutDate: String, propertyId: Long, roomCount: Int,
                        adultCount: Int, destinationType: String, destinationName: String,
                        isDirectPayment: Boolean, isShowRoom: Boolean, source: String): HotelDetailFragment =
                HotelDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelDetailActivity.EXTRA_CHECK_IN_DATE, checkInDate)
                        putString(HotelDetailActivity.EXTRA_CHECK_OUT_DATE, checkOutDate)
                        putLong(HotelDetailActivity.EXTRA_PROPERTY_ID, propertyId)
                        putInt(HotelDetailActivity.EXTRA_ROOM_COUNT, roomCount)
                        putInt(HotelDetailActivity.EXTRA_ADULT_COUNT, adultCount)
                        putString(HotelDetailActivity.EXTRA_DESTINATION_TYPE, destinationType)
                        putString(HotelDetailActivity.EXTRA_DESTINATION_NAME, destinationName)
                        putBoolean(HotelDetailActivity.EXTRA_IS_DIRECT_PAYMENT, isDirectPayment)
                        putBoolean(EXTRA_SHOW_ROOM, isShowRoom)
                        putString(HotelDetailActivity.EXTRA_SOURCE, source)
                    }
                }
    }
}
