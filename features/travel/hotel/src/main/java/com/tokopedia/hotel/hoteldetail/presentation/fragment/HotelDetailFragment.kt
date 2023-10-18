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
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelSourceEnum
import com.tokopedia.hotel.common.data.HotelTypeEnum
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelStringUtils
import com.tokopedia.hotel.common.util.HotelUtils.Companion.getImageUrl
import com.tokopedia.hotel.common.util.QueryHotelNearbyLandmarks
import com.tokopedia.hotel.common.util.QueryHotelPropertyDetail
import com.tokopedia.hotel.common.util.QueryHotelPropertyReview
import com.tokopedia.hotel.common.util.QueryHotelPropertyRoomList
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_PDP
import com.tokopedia.hotel.databinding.FragmentHotelDetailBinding
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity
import com.tokopedia.hotel.globalsearch.presentation.widget.HotelGlobalSearchWidget
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.hoteldetail.data.entity.HotelNearbyLandmark
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
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelNearbyPlacesSectionAdapter
import com.tokopedia.hotel.hoteldetail.presentation.model.HotelDetailAllFacilityModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelDetailViewModel
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.hotel.hoteldetail.util.HotelShare
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.createDefaultProgressDialog
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mapviewer.activity.MapViewerActivity
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.addTimeToSpesificDate
import com.tokopedia.utils.date.toString
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.android.synthetic.main.item_network_error_view.*
import java.lang.ref.WeakReference
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
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

    private var binding by autoClearedNullable<FragmentHotelDetailBinding>()

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
    private var hotelId: String = "0"
    private var roomPrice: String = "0"
    private var hotelImage: String = ""
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
    private lateinit var nearbyLandmarks: HotelNearbyPlacesSectionAdapter

    private var loadingProgressDialog: ProgressDialog? = null
    private var isTickerValid = false
    private var isScrolled = false
    private lateinit var hotelShare: HotelShare
    private val userSession: UserSessionInterface by lazy {
        UserSession(requireContext())
    }

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
                hotelHomepageModel.checkInDate = it.getString(
                    HotelDetailActivity.EXTRA_CHECK_IN_DATE,
                    DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 1).toString(DateUtil.YYYY_MM_DD)
                )
                hotelHomepageModel.checkOutDate = it.getString(
                    HotelDetailActivity.EXTRA_CHECK_OUT_DATE,
                    DateUtil.getCurrentDate().addTimeToSpesificDate(Calendar.DATE, 2).toString(DateUtil.YYYY_MM_DD)
                )
                hotelHomepageModel.roomCount = it.getInt(HotelDetailActivity.EXTRA_ROOM_COUNT)
                hotelHomepageModel.adultCount = it.getInt(HotelDetailActivity.EXTRA_ADULT_COUNT, 1)
                hotelHomepageModel.locName = it.getString(HotelDetailActivity.EXTRA_DESTINATION_NAME, "")
                hotelHomepageModel.locType = it.getString(HotelDetailActivity.EXTRA_DESTINATION_TYPE, HotelTypeEnum.PROPERTY.value)
                isDirectPayment = it.getBoolean(HotelDetailActivity.EXTRA_IS_DIRECT_PAYMENT, true)
            }
            isButtonEnabled = it.getBoolean(EXTRA_SHOW_ROOM, true)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeShare(view)

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_SEARCH_PARAMETER)) {
            hotelHomepageModel = savedInstanceState.getParcelable(SAVED_SEARCH_PARAMETER)
                ?: HotelHomepageModel()
            isButtonEnabled = savedInstanceState.getBoolean(SAVED_ENABLE_BUTTON)
        }

        showLoadingLayout()

        detailViewModel.fetchTickerData()

        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                QueryHotelPropertyDetail(), // HotelGqlQuery.PROPERTY_DETAIL,
                QueryHotelPropertyRoomList(), // HotelGqlQuery.PROPERTY_ROOM_LIST,
                QueryHotelPropertyReview(), // HotelGqlQuery.PROPERTY_REVIEW,
                QueryHotelNearbyLandmarks(), // HotelGqlQuery.HOTEL_NEARBY_LANDMARKS,
                hotelHomepageModel.locId,
                hotelHomepageModel,
                source
            )
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                QueryHotelPropertyDetail(), // HotelGqlQuery.PROPERTY_DETAIL,
                QueryHotelPropertyReview(), // HotelGqlQuery.PROPERTY_REVIEW,
                QueryHotelNearbyLandmarks(), // HotelGqlQuery.HOTEL_NEARBY_LANDMARKS,
                hotelHomepageModel.locId,
                source
            )
        }

        setupGlobalSearchWidget()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailViewModel.roomListResult.observe(
            viewLifecycleOwner,
            Observer {
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
            }
        )

        detailViewModel.hotelInfoResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        isHotelDetailSuccess = true
                        setupLayout(it.data)
                        hotelName = it.data.property.name
                        hotelId = it.data.property.id
                        hotelImage = it.data.property.locationImageStatic
                    }
                    is Fail -> {
                        isHotelDetailSuccess = false
                        showErrorView(it.throwable)
                    }
                }
                isHotelInfoLoaded = true
                stopTrace()
            }
        )

        detailViewModel.hotelReviewResult.observe(
            viewLifecycleOwner,
            Observer {
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
            }
        )

        detailViewModel.tickerData.observe(
            viewLifecycleOwner,
            Observer {
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
            }
        )

        observeABTestNearbyLandmarks()
    }
    fun observeABTestNearbyLandmarks() {
        if (!isABTestNearbyLandmarks()) {
            detailViewModel.hotelNearbyLandmarks.observe(viewLifecycleOwner, {
                when (it) {
                    is Success -> {
                        setupLayoutNearbyLandmarks(it.data)
                    }
                    is Fail -> {
                        hideNearbyLandmarks()
                    }
                }
            })
        }
    }

    private fun hideTickerView() {
        binding?.let {
            it.hotelDetailTicker.hide()
        }
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        isTickerValid = true
        if (travelTickerModel.title.isNotEmpty()) binding?.hotelDetailTicker?.tickerTitle = travelTickerModel.title
        var message = travelTickerModel.message
        if (travelTickerModel.url.isNotEmpty()) message += getString(R.string.hotel_ticker_desc, travelTickerModel.url)
        binding?.let {
            it.hotelDetailTicker.setHtmlDescription(message)
            it.hotelDetailTicker.tickerType = Ticker.TYPE_WARNING
            it.hotelDetailTicker.setDescriptionClickEvent(object : TickerCallback {
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
                it.hotelDetailTicker.setOnClickListener {
                    RouteManager.route(requireContext(), travelTickerModel.url)
                }
            }

            it.hotelDetailTicker.show()
        }
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
                        if (it.hasExtra(HotelGlobalSearchActivity.CHECK_IN_DATE)) checkInDate = it.getStringExtra(HotelGlobalSearchActivity.CHECK_IN_DATE) ?: ""
                        if (it.hasExtra(HotelGlobalSearchActivity.CHECK_OUT_DATE)) checkOutDate = it.getStringExtra(HotelGlobalSearchActivity.CHECK_OUT_DATE) ?: ""
                        if (it.hasExtra(HotelGlobalSearchActivity.NUM_OF_ROOMS)) roomCount = it.getIntExtra(HotelGlobalSearchActivity.NUM_OF_ROOMS, 1)
                        if (it.hasExtra(HotelGlobalSearchActivity.NUM_OF_GUESTS)) adultCount = it.getIntExtra(HotelGlobalSearchActivity.NUM_OF_GUESTS, 1)
                    }
                    showLoadingContainerBottom()
                    hideRoomAvailableContainerBottom()
                    hideRoomNotAvailableContainerBottom()
                    detailViewModel.getRoomWithoutHotelData(
                        QueryHotelPropertyRoomList(), // HotelGqlQuery.PROPERTY_ROOM_LIST,
                        hotelHomepageModel
                    )
                }
            }
        }
    }

    private fun initializeShare(view: View) {
        if (context != null) {
            activity?.run {
                val ctx = WeakReference<Activity>(this)
                hotelShare = HotelShare(ctx, requireContext(), view, trackingHotelUtil)
            }
        }
    }

    private fun showErrorView(error: Throwable) {
        if (!isHotelDetailSuccess && !isHotelReviewSuccess && !isRoomListSuccess) {
            stopTrace()

            binding?.let {
                it.containerContent.visibility = View.GONE
                it.containerError.root.visibility = View.VISIBLE
            }

            context?.run {
                ErrorHandlerHotel.getErrorUnify(
                    this,
                    error,
                    { onErrorRetryClicked() },
                    global_error,
                    { (activity as HotelDetailActivity).onBackPressed() }
                )
            }
        }
    }

    private fun hideErrorView() {
        binding?.let {
            it.containerContent.visibility = View.VISIBLE
            it.containerError.root.visibility = View.GONE
        }
    }

    private fun setupLayout(data: PropertyDetailData) {
        hideLoadingLayout()
        (activity as HotelDetailActivity).setSupportActionBar(binding?.detailToolbar)
        (activity as HotelDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.detailToolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN0), PorterDuff.Mode.SRC_ATOP)

        binding?.collapsingToolbar?.setExpandedTitleTextAppearance(R.style.hotelPdpExpandedToolbarLayoutTitleColor)
        binding?.collapsingToolbar?.setCollapsedTitleTextAppearance(R.style.hotelPdpCollapsingToolbarLayoutTitleColor)
        binding?.collapsingToolbar?.title = data.property.name

        binding?.appBarLayout?.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) >= appBarLayout.totalScrollRange && !isScrolled) {
                    if (isTickerValid) binding?.hotelDetailTicker?.hide()
                    binding?.detailToolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN950_96), PorterDuff.Mode.SRC_ATOP)
                    (activity as HotelDetailActivity).optionMenu?.setIcon(com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_black)
                    isScrolled = true
                } else if (abs(verticalOffset) == 0 && isScrolled) {
                    if (isTickerValid) binding?.hotelDetailTicker?.show()
                    binding?.detailToolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_NN0), PorterDuff.Mode.SRC_ATOP)
                    (activity as HotelDetailActivity).optionMenu?.setIcon(com.tokopedia.abstraction.R.drawable.ic_toolbar_overflow_level_two_white)
                    isScrolled = false
                }
            }
        )

        setupMainImage(data.property.images)

        binding?.hotelRatingContainer?.let {
            it.removeAllViews()
        }
        context?.run {
            val textView = Typography(requireContext())
            textView.apply {
                background = ContextCompat.getDrawable(context, R.drawable.bg_search_destination_tag)
                setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN500))
                text = data.property.typeName
                setType(Typography.BODY_3)
                setWeight(Typography.BOLD)
                setPadding(getDimens(R.dimen.hotel_6dp), getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1), getDimens(R.dimen.hotel_6dp), getDimens(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl1))
                val params: LinearLayout.LayoutParams =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.setMargins(IMAGE_COUNTER_ZERO, IMAGE_COUNTER_ZERO, getDimens(R.dimen.hotel_6dp), IMAGE_COUNTER_ZERO)
                layoutParams = params
            }
            binding?.hotelRatingContainer?.addView(textView)
        }
        binding?.tvHotelName?.text = data.property.name
        for (i in 1..data.property.star) {
            context?.run { binding?.hotelRatingContainer?.addView(RatingStarView(this)) }
        }
        binding?.tvHotelAddress?.text = data.property.address

        binding?.ivHotelDetailLocation?.loadImage(data.property.locationImageStatic)
        setupSafetyBadgeLayout(data.safetyBadge)
        setupPolicySwitcher(data)
        setupImportantInfo(data)
        setupDescription(data)
        setupMainFacilityItem(data)
        setupShareLink(data)

        binding?.btnHotelDetailShow?.setOnClickListener {
            context?.run {
                startActivity(
                    MapViewerActivity.getCallingIntent(
                        this,
                        data.property.name,
                        data.property.latitude,
                        data.property.longitude,
                        data.property.address,
                        HOTEL_PIN
                    )
                )
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
            binding?.hotelSafetyInformationLayout?.show()
            if (propertySafetyBadge.title.isNotEmpty()) {
                binding?.tvHotelSafetyInformationTitle?.text = propertySafetyBadge.title
                binding?.ivHotelSafetyBadgeIcon?.loadIcon(propertySafetyBadge.icon.light)
            } else {
                binding?.tvHotelSafetyInformationTitle?.hide()
                binding?.ivHotelSafetyBadgeIcon?.hide()
            }

            if (propertySafetyBadge.content.isNotEmpty()) {
                binding?.tvHotelSafetyInformationContent?.text = propertySafetyBadge.content
            } else {
                binding?.tvHotelSafetyInformationContent?.hide()
            }
        } else {
            binding?.hotelSafetyInformationLayout?.hide()
        }
    }

    private fun setupShareLink(propertyDetailData: PropertyDetailData) {
        binding?.hotelShareButton?.setOnClickListener {
            trackingHotelUtil.clickShareUrl(requireContext(), hotelId)
            if (::hotelShare.isInitialized) {
                if (fragmentManager != null && context != null && view != null) {
                    hotelShare.showUniversalBottomSheet(fragmentManager!!, propertyDetailData, isPromo, userSession.userId, ArrayList(imageList))
                }
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
        binding?.appBarLayout?.visibility = View.GONE
        binding?.hotelDetailNestedScrollView?.visibility = View.GONE
        binding?.containerHotelDetailShimmering?.root?.visibility = View.VISIBLE
    }

    private fun hideLoadingLayout() {
        binding?.appBarLayout?.visibility = View.VISIBLE
        binding?.hotelDetailNestedScrollView?.visibility = View.VISIBLE
        binding?.containerHotelDetailShimmering?.root?.visibility = View.GONE
    }

    private fun setupMainImage(images: List<PropertyImageItem>) {
        var imageCounter = 0

        for ((imageIndex, item) in images.withIndex()) {
            imageList.add(item.urlOriginal)
            thumbnailImageList.add(getImageUrl(context, item.urlOriginal, item.urlMax300))

            when (imageCounter) {
                IMAGE_COUNTER_ZERO -> {
                    // do nothing, preventing break if mainPhoto not in the first item
                }
                IMAGE_COUNTER_FIRST -> {
                    binding?.ivFirstPhotoPreview?.loadImage(getImageUrl(context, item.urlOriginal, item.urlMax300)) {
                        setPlaceHolder(com.tokopedia.iconunify.R.drawable.iconunify_image_broken)
                    }
                    binding?.ivFirstPhotoPreview?.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, binding?.ivFirstPhotoPreview)
                    }
                    imageCounter++
                }
                IMAGE_COUNTER_SECOND -> {
                    binding?.ivSecondPhotoPreview?.loadImage(getImageUrl(context, item.urlOriginal, item.urlMax300)) {
                        setPlaceHolder(com.tokopedia.iconunify.R.drawable.iconunify_image_broken)
                    }
                    binding?.ivSecondPhotoPreview?.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, binding?.ivSecondPhotoPreview)
                    }
                    imageCounter++
                }
                IMAGE_COUNTER_THIRD -> {
                    binding?.ivThirdPhotoPreview?.loadImage(getImageUrl(context, item.urlOriginal, item.urlMax300)) {
                        setPlaceHolder(com.tokopedia.iconunify.R.drawable.iconunify_image_broken)
                    }
                    binding?.ivThirdPhotoPreview?.setOnClickListener {
                        onPhotoClicked()
                        openImagePreview(imageList, imageIndex, binding?.ivThirdPhotoPreview)
                    }
                    imageCounter++
                }
            }
            if (item.mainPhoto) {
                binding?.ivMainPhotoPreview?.loadImage(getImageUrl(context, item.urlOriginal, item.urlMax300)) {
                    setPlaceHolder(com.tokopedia.iconunify.R.drawable.iconunify_image_broken)
                }
                binding?.ivMainPhotoPreview?.setOnClickListener {
                    onPhotoClicked()
                    openImagePreview(imageList, imageIndex, binding?.ivMainPhotoPreview)
                }
                imageCounter++
            }
        }

        if (images.size - imageCounter > 0) {
            binding?.tvMoreImageCounter?.text = getString(R.string.hotel_detail_more_image_counter, images.size - imageCounter)
        }
    }

    private fun onPhotoClicked() {
        trackingHotelUtil.hotelClickHotelPhoto(context, hotelId, roomPriceAmount, PDP_SCREEN_NAME)
    }

    private fun setupReviewLayout(data: HotelReview.ReviewData) {
        binding?.containerHotelReview?.visibility = View.VISIBLE
        setupReviewHeader(data)
        setupReviewItem(data.reviewList)
    }

    private fun setupReviewHeader(data: HotelReview.ReviewData) {
        if (data.totalReview > 0 || data.averageScoreReview > 0) {
            var hasHeadline = false
            if (data.totalReview > 0) {
                binding?.tvHotelRatingCount?.text = getString(
                    R.string.hotel_detail_based_on_review_number,
                    HotelStringUtils.convertPriceValue(data.totalReview.toDouble(), false)
                )
                binding?.tvHotelRatingDetail?.text = data.headline
                hasHeadline = true
            } else if (!hasHeadline) {
                binding?.tvHotelRatingCount?.visibility = View.GONE
            }

            if (data.averageScoreReview > 0) {
                binding?.tvHotelRatingNumber?.text = data.averageScoreReview.toString()
                binding?.tvHotelRatingDetail?.text = data.headline
            } else if (!hasHeadline) {
                binding?.tvHotelRatingNumber?.visibility = View.GONE
                binding?.tvHotelRatingDetail?.text = getString(R.string.hotel_detail_no_rating)
            }
        } else {
            binding?.tvHotelRatingNumber?.visibility = View.GONE
            binding?.tvHotelRatingCount?.visibility = View.GONE
            binding?.tvHotelDetailAllReviews?.visibility = View.GONE
            binding?.tvHotelRatingDetail?.text = getString(R.string.hotel_detail_no_rating_review)
        }
    }

    private fun setupReviewItem(reviewList: List<HotelReview>) {
        if (reviewList.isNotEmpty()) {
            if (!::detailReviewAdapter.isInitialized) {
                detailReviewAdapter = HotelDetailReviewAdapter(reviewList)
            }

            binding?.let {
                val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                it.rvBestReview.layoutManager = layoutManager
                it.rvBestReview.setHasFixedSize(true)
                it.rvBestReview.isNestedScrollingEnabled = false
                it.rvBestReview.adapter = detailReviewAdapter

                it.tvHotelDetailAllReviews.setOnClickListener {
                    trackingHotelUtil.hotelClickHotelReviews(context, hotelId, roomPriceAmount, PDP_SCREEN_NAME)
                    context?.run {
                        startActivityForResult(HotelReviewActivity.getCallingIntent(this, hotelHomepageModel.locId), RESULT_REVIEW)
                    }
                }
            }
        } else {
            binding?.rvBestReview?.visibility = View.GONE
        }
    }

    private fun setupMainFacilityItem(data: PropertyDetailData) {
        if (!::mainFacilityAdapter.isInitialized) {
            mainFacilityAdapter = HotelDetailMainFacilityAdapter(data.mainFacility)
        }

        binding?.let {
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            it.rvHotelFacilities.layoutManager = layoutManager
            it.rvHotelFacilities.setHasFixedSize(true)
            it.rvHotelFacilities.isNestedScrollingEnabled = false
            it.rvHotelFacilities.adapter = mainFacilityAdapter

            it.tvHotelDetailAllFacilities.setOnClickListener {
                context?.run {
                    startActivity(
                        HotelDetailAllFacilityActivity.getCallingIntent(
                            this,
                            hotelName,
                            HotelDetailAllFacilityModel.transform(data),
                            HotelDetailAllFacilityFragment.FACILITY_TITLE
                        )
                    )
                }
            }
        }
    }

    private fun setupImportantInfo(data: PropertyDetailData) {
        if (data.property.importantInformation.isNotEmpty()) {
            binding?.tvHotelImportantInfo?.text = data.property.importantInformation
            binding?.tvHotelImportantInfoMore?.setOnClickListener {
                context?.run {
                    startActivity(
                        HotelDetailAllFacilityActivity.getCallingIntent(
                            this,
                            hotelName,
                            HotelDetailAllFacilityModel.transform(data),
                            HotelDetailAllFacilityFragment.IMPORTANT_INFO_TITLE
                        )
                    )
                }
            }
        } else {
            binding?.containerImportantInfo?.visibility = View.GONE
        }
    }

    private fun setupDescription(data: PropertyDetailData) {
        if (data.property.description.isNotEmpty()) {
            binding?.tvHotelDescription?.text = data.property.description
            binding?.tvHotelDescriptionMore?.setOnClickListener {
                context?.run {
                    startActivity(
                        HotelDetailAllFacilityActivity.getCallingIntent(
                            this,
                            hotelName,
                            HotelDetailAllFacilityModel.transform(data),
                            HotelDetailAllFacilityFragment.DESCRIPTION_TITLE
                        )
                    )
                }
            }
        } else {
            binding?.containerHotelDescription?.visibility = View.GONE
        }
    }

    private fun setupPolicySwitcher(data: PropertyDetailData) {
        binding?.scvHotelDate?.setLeftTitleText(data.property.checkinInfo)
        binding?.scvHotelDate?.setRightTitleText(data.property.checkoutInfo)

        binding?.tvHotelDetailAllPolicies?.setOnClickListener {
            context?.run {
                startActivity(
                    HotelDetailAllFacilityActivity.getCallingIntent(
                        this,
                        hotelName,
                        HotelDetailAllFacilityModel.transform(data),
                        HotelDetailAllFacilityFragment.POLICY_TITLE
                    )
                )
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
            binding?.tvHotelPrice?.text = roomPrice

            var hotelDetailTag = data.first().additionalPropertyInfo.hotelTagging
            if (hotelDetailTag.isNotEmpty()) {
                binding?.hotelDetailTag?.show()
                binding?.hotelDetailTag?.text = hotelDetailTag
                isPromo = true
            } else {
                binding?.hotelDetailTag?.hide()
            }

            if (data[0].additionalPropertyInfo.isEnabled) {
                isAvailable = true

                binding?.btnSeeRoom?.text = getString(R.string.hotel_detail_show_room_text)
                binding?.btnSeeRoom?.buttonType = UnifyButton.Type.MAIN
                binding?.btnSeeRoom?.setOnClickListener {
                    trackingHotelUtil.hotelChooseViewRoom(context, hotelHomepageModel, hotelId, hotelName, PDP_SCREEN_NAME)
                    context?.run {
                        startActivityForResult(
                            HotelRoomListActivity.createInstance(
                                this, hotelHomepageModel.locId, hotelName,
                                hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate, hotelHomepageModel.adultCount, 0,
                                hotelHomepageModel.roomCount, hotelHomepageModel.locType, hotelHomepageModel.locName
                            ),
                            RESULT_ROOM_LIST
                        )
                    }
                }
            } else {
                binding?.btnSeeRoom?.text = getString(R.string.hotel_detail_coming_soon_text)
                binding?.btnSeeRoom?.isEnabled = false
            }
        } else {
            showRoomNotAvailableContainerBottom()
        }

        trackingHotelUtil.hotelViewDetails(
            context,
            hotelHomepageModel,
            hotelName,
            hotelId,
            isAvailable,
            ceil(data.firstOrNull()?.roomPrice?.priceAmount ?: 0.0).toInt().toString(),
            data.firstOrNull()?.additionalPropertyInfo?.isDirectPayment
                ?: isDirectPayment,
            PDP_SCREEN_NAME
        )

        if (!isButtonEnabled) {
            binding?.btnSeeRoom?.isEnabled = false
        }

        setupGlobalSearchWidget()
    }

    private fun setupLayoutNearbyLandmarks(dataList: HotelNearbyLandmark) {
        if (dataList.result.isNotEmpty()) {
            if (!::nearbyLandmarks.isInitialized) {
                nearbyLandmarks = HotelNearbyPlacesSectionAdapter(dataList.result)
            }
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding?.rvNearbyLandmarks?.layoutManager = layoutManager
            binding?.rvNearbyLandmarks?.isNestedScrollingEnabled = false
            binding?.rvNearbyLandmarks?.adapter = nearbyLandmarks

            binding?.tvHotelNearbyLandmarkInfo?.text = dataList.information

            context?.let {
                trackingHotelUtil.hotelDetailViewNearbyLandmarks(it, PDP_SCREEN_NAME, hotelId, roomPriceAmount, hotelName)
            }
        } else {
            hideNearbyLandmarks()
        }
    }

    private fun setupGlobalSearchWidget() {
        // setup hotel global search widget
        // add condition if checkin date & checkout date isNotEmpty, to prevent crash access hotel detail from applink
        binding?.let {
            if (hotelHomepageModel.checkInDate.isNotEmpty() &&
                hotelHomepageModel.checkOutDate.isNotEmpty()
            ) {
                it.widgetHotelGlobalSearch.title = hotelName
                it.widgetHotelGlobalSearch.globalSearchListener = this
                it.widgetHotelGlobalSearch.setPreferencesData(
                    hotelHomepageModel.checkInDate,
                    hotelHomepageModel.checkOutDate,
                    hotelHomepageModel.adultCount,
                    hotelHomepageModel.roomCount
                )
                it.widgetHotelGlobalSearch.buildView()
            } else {
                it.widgetHotelGlobalSearch.hide()
            }
            if (!isButtonEnabled) it.widgetHotelGlobalSearch.hide()
        }
    }

    private fun openImagePreview(imageList: MutableList<String>, index: Int, imageViewTransitionFrom: ImageView?) {
        ImagePreviewSlider.instance.start(context, hotelName, imageList, thumbnailImageList, index, imageViewTransitionFrom)
    }

    override fun onErrorRetryClicked() {
        hideErrorView()
        if (isButtonEnabled) {
            detailViewModel.getHotelDetailData(
                QueryHotelPropertyDetail(), // HotelGqlQuery.PROPERTY_DETAIL,
                QueryHotelPropertyRoomList(), // HotelGqlQuery.PROPERTY_ROOM_LIST,
                QueryHotelPropertyReview(), // HotelGqlQuery.PROPERTY_REVIEW,
                QueryHotelNearbyLandmarks(), // HotelGqlQuery.HOTEL_NEARBY_LANDMARKS,
                hotelHomepageModel.locId,
                hotelHomepageModel,
                source
            )
        } else {
            detailViewModel.getHotelDetailDataWithoutRoom(
                QueryHotelPropertyDetail(), // HotelGqlQuery.PROPERTY_DETAIL,
                QueryHotelPropertyReview(), // HotelGqlQuery.PROPERTY_REVIEW,
                QueryHotelNearbyLandmarks(), // HotelGqlQuery.HOTEL_NEARBY_LANDMARKS,
                hotelHomepageModel.locId,
                source
            )
        }
    }

    private fun showLoadingContainerBottom() {
        binding?.let {
            it.containerShimmeringBottom.visibility = View.VISIBLE
        }
    }

    private fun hideLoadingContainerBottom() {
        binding?.let {
            it.containerShimmeringBottom.visibility = View.GONE
        }
    }

    private fun showRoomAvailableContainerBottom() {
        binding?.let {
            it.containerRoomAvailable.visibility = View.VISIBLE
        }
    }

    private fun hideRoomAvailableContainerBottom() {
        binding?.let {
            it.containerRoomAvailable.visibility = View.GONE
        }
    }

    private fun showRoomNotAvailableContainerBottom() {
        binding?.let {
            it.containerRoomNotAvailable.visibility = View.VISIBLE
        }
    }

    private fun hideRoomNotAvailableContainerBottom() {
        binding?.let {
            it.containerRoomNotAvailable.visibility = View.GONE
        }
    }

    private fun showNearbyLandmarks() {
        binding?.let {
            it.rvNearbyLandmarks.visible()
            it.tvHotelNearbyLandmarkInfo.visible()
        }
    }

    private fun hideNearbyLandmarks() {
        binding?.let {
            it.rvNearbyLandmarks.gone()
            it.tvHotelNearbyLandmarkInfo.gone()
        }
    }

    private fun stopTrace() {
        if (!isTraceStop) {
            if (isHotelInfoLoaded && isHotelReviewLoaded && isRoomListLoaded) {
                performanceMonitoring?.stopTrace()
                isTraceStop = true
            }
        }
    }

    fun isABTestNearbyLandmarks(): Boolean = (
        RemoteConfigInstance.getInstance().abTestPlatform
            .getString(AB_TEST_KEY_NEARBY_LANDMARK, AB_TEST_SHOW_LANDMARK)
            == AB_TEST_HIDE_LANDMARK
        )
    companion object {

        const val REQUEST_CODE_GLOBAL_SEARCH = 103

        const val SAVED_SEARCH_PARAMETER = "SAVED_SEARCH_PARAMETER"
        const val SAVED_ENABLE_BUTTON = "SAVED_ENABLE_BUTTON"

        const val EXTRA_SHOW_ROOM = "EXTRA_SHOW_ROOM"

        const val HOTEL_PIN = "HOTEL_PIN"

        const val RESULT_ROOM_LIST = 101
        const val RESULT_REVIEW = 102

        const val IMAGE_COUNTER_ZERO = 0
        const val IMAGE_COUNTER_FIRST = 1
        const val IMAGE_COUNTER_SECOND = 2
        const val IMAGE_COUNTER_THIRD = 3

        const val AB_TEST_KEY_NEARBY_LANDMARK = "hotel_nearlandmark"
        const val AB_TEST_HIDE_LANDMARK = "hide_nearby"
        const val AB_TEST_SHOW_LANDMARK = "Show_nearby"

        fun getInstance(
            checkInDate: String,
            checkOutDate: String,
            propertyId: Long,
            roomCount: Int,
            adultCount: Int,
            destinationType: String,
            destinationName: String,
            isDirectPayment: Boolean,
            isShowRoom: Boolean,
            source: String
        ): HotelDetailFragment =
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
