package com.tokopedia.hotel.roomdetail.presentation.fragment

import android.app.ProgressDialog
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomdetail.presentation.viewmodel.HotelRoomDetailViewModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomDetailModel
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.imagepreviewslider.presentation.activity.ImagePreviewSliderActivity
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_room_detail.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*
import javax.inject.Inject

/**
 * @author by resakemal on 23/04/19
 */

class HotelRoomDetailFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var roomDetailViewModel: HotelRoomDetailViewModel

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    lateinit var hotelRoom: HotelRoom
    lateinit var addToCartParam: HotelAddCartParam

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    lateinit var progressDialog: ProgressDialog

    private var roomIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            roomDetailViewModel = viewModelProvider.get(HotelRoomDetailViewModel::class.java)

            roomIndex = arguments?.getInt(HotelRoomDetailActivity.EXTRA_ROOM_INDEX, 0) ?: 0

            saveInstanceCacheManager = SaveInstanceCacheManager(this, savedInstanceState)
            val manager = if (savedInstanceState == null) SaveInstanceCacheManager(this,
                    arguments?.getString(HotelRoomDetailActivity.EXTRA_SAVED_INSTANCE_ID)) else saveInstanceCacheManager

            val hotelRoomDetailModel = manager.get(EXTRA_ROOM_DATA, HotelRoomDetailModel::class.java)
                    ?: HotelRoomDetailModel()
            hotelRoom = hotelRoomDetailModel.hotelRoom
            addToCartParam = hotelRoomDetailModel.addToCartParam

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        roomDetailViewModel.addCartResponseResult.observe(this, android.arch.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    val cartId = it.data.response.cartId
                    context?.run {
                        startActivity(HotelBookingActivity.getCallingIntent(this, cartId))
                    }
                }
                is Fail -> {
                    NetworkErrorHelper.showRedSnackbar(activity, ErrorHandler.getErrorMessage(activity, it.throwable))
                }
            }
            room_detail_button.isEnabled = true
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_room_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initProgressDialog()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveInstanceCacheManager.onSave(outState)
        saveInstanceCacheManager.put(HotelRoomDetailFragment.EXTRA_ROOM_DATA, HotelRoomDetailModel(hotelRoom, addToCartParam))
    }

    private fun initView() {
        setupCollapsingToolbar()
        setupRoomImages()
        setupRoomHeader()
        setupRoomPayAtHotel()
        setupRoomCancellation()
        setupRoomTax()
        setupRoomDeposit()
        setupRoomFacilities()
        setupRoomDescription()
        setupRoomBreakfast()
        setupRoomExtraBed()
        setupRoomPrice()
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.hotel_progress_dialog_title))
        progressDialog.setCancelable(false)
    }

    private fun setupCollapsingToolbar() {
        (activity as HotelRoomDetailActivity).setSupportActionBar(room_detail_detail_toolbar)
        (activity as HotelRoomDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = room_detail_detail_toolbar.navigationIcon
        navIcon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
        (activity as HotelRoomDetailActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        room_detail_collapsing_toolbar.title = ""
        room_detail_app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    room_detail_collapsing_toolbar.title = hotelRoom.roomInfo.name
                    navIcon?.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    room_detail_collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
                    isShow = false
                }
            }
        })
    }

    private fun setupRoomImages() {
        if (hotelRoom.roomInfo.roomImages.isNotEmpty()) {
            val roomImageUrls300 = hotelRoom.roomInfo.roomImages.map { it.url300 }
            val roomImageUrls = hotelRoom.roomInfo.roomImages.map { it.urlOriginal }
            val roomImageUrlsSquare = hotelRoom.roomInfo.roomImages.map { it.url300 }

            if (roomImageUrls300.size >= 5) room_detail_images.setImages(roomImageUrls300.subList(0, 5))
            else room_detail_images.setImages(roomImageUrls300)

            room_detail_images.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener {
                override fun onImageClicked(position: Int) {
                    trackingHotelUtil.hotelClickRoomDetailsPhoto(hotelRoom.additionalPropertyInfo.propertyId,
                            hotelRoom.roomId, hotelRoom.roomPrice.roomPrice)
                    context?.run {
                        startActivity(ImagePreviewSliderActivity.getCallingIntent(
                                this, hotelRoom.roomInfo.name, roomImageUrls, roomImageUrlsSquare, position
                        ))
                    }
                }
            }
        }
        room_detail_images.buildView()
    }

    private fun setupRoomHeader() {
        tv_room_detail_title.text = hotelRoom.roomInfo.name
        tv_room_detail_occupancy.text = getString(R.string.hotel_room_detail_header_occupancy,
                hotelRoom.occupancyInfo.occupancyText)
        tv_room_detail_size.text = getString(R.string.hotel_room_detail_header_room_size, hotelRoom.roomInfo.size, hotelRoom.bedInfo)

        context?.run {
            val breakfastTextView = FacilityTextView(this)
            if (hotelRoom.breakfastInfo.isBreakfastIncluded) {
                breakfastTextView.setIconAndText(R.drawable.ic_hotel_free_breakfast, getString(R.string.hotel_room_list_free_breakfast))
            } else {
                breakfastTextView.setIconAndText(R.drawable.ic_hotel_no_breakfast, getString(R.string.hotel_room_list_breakfast_not_included))
            }
            room_detail_header_facilities.addView(breakfastTextView)

            val refundableTextView = FacilityTextView(this)
            if (hotelRoom.refundInfo.isRefundable) {
                refundableTextView.setIconAndText(R.drawable.ic_hotel_refundable, getString(R.string.hotel_room_list_refundable_with_condition))
            } else {
                refundableTextView.setIconAndText(R.drawable.ic_hotel_not_refundable, getString(R.string.hotel_room_list_not_refundable))
            }
            room_detail_header_facilities.addView(refundableTextView)
        }

        if (hotelRoom.numberRoomLeft <= MINIMUM_ROOM_COUNT) {
            tv_room_detail_count.visibility = View.VISIBLE
            tv_room_detail_count.text = getString(R.string.hotel_room_room_left_text,
                    Integer.toString(hotelRoom.numberRoomLeft))
        }
    }

    fun setupRoomPayAtHotel() {
        if (!hotelRoom.additionalPropertyInfo.isDirectPayment) {
            pay_at_hotel_container.visibility = View.VISIBLE

            val iconId = if (hotelRoom.creditCardInfo.isCCRequired)
                R.drawable.ic_pay_at_hotel_cc else R.drawable.ic_pay_at_hotel_no_cc
            pay_at_hotel_icon.setBackgroundResource(iconId)

            val spannableString = SpannableString(" " + hotelRoom.creditCardInfo.header
                    + "\n" + hotelRoom.creditCardInfo.creditCardInfo)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 1, 1 + hotelRoom.creditCardInfo.header.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(LeadingMarginSpan.Standard(50, 0), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            pay_at_hotel_title.text = getString(R.string.hotel_room_detail_pay_at_hotel)
            pay_at_hotel_desc.text = spannableString
        }
    }

    fun setupRoomCancellation() {
        if (hotelRoom.cancelPolicy.isNotEmpty()) {
            val spannableStringBuilder = SpannableStringBuilder()
            for (policy in hotelRoom.cancelPolicy) {
                val start = spannableStringBuilder.length
                spannableStringBuilder.append(policy.subheader)
                val end = spannableStringBuilder.length
                spannableStringBuilder.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableStringBuilder.append("\n" + policy.content + "\n\n")
            }
            spannableStringBuilder.delete(spannableStringBuilder.length - 2, spannableStringBuilder.length)

            room_detail_cancellation.setTitleAndDescription(getString(R.string.hotel_room_detail_cancellation), spannableStringBuilder)
            room_detail_cancellation.truncateDescription = false
            room_detail_cancellation.buildView()
        }
    }

    fun setupRoomTax() {
        if (hotelRoom.taxes.isNotEmpty()) {
            room_detail_tax.setTitleAndDescription(getString(R.string.hotel_room_detail_tax), hotelRoom.taxes)
            room_detail_tax.buildView()
        }
    }

    fun setupRoomDeposit() {
        if (hotelRoom.depositInfo.isNeedDeposit) {
            room_detail_deposit.setTitleAndDescription(getString(R.string.hotel_room_detail_deposit), hotelRoom.depositInfo.depositText)
            room_detail_deposit.buildView()
        }
    }

    fun setupRoomFacilities() {
        if (hotelRoom.roomInfo.facility.isNotEmpty()) {
            val facilityList = hotelRoom.roomInfo.facility
            val stringBuilder = StringBuffer()
            var previewFacilitiesString = ""
            var fullFacilitiesString: String

            for (i in 0 until facilityList.size) {
                stringBuilder.append(getString(R.string.hotel_room_detail_facility_item, facilityList[i].name))
                stringBuilder.append("\n")
                if (i == ROOM_FACILITY_DEFAULT_COUNT - 1) {
                    previewFacilitiesString = stringBuilder.toString()
                }
            }
            if (previewFacilitiesString.isEmpty()) previewFacilitiesString = stringBuilder.toString()
            fullFacilitiesString = stringBuilder.toString()
            previewFacilitiesString = previewFacilitiesString.dropLast(1)
            fullFacilitiesString = fullFacilitiesString.dropLast(1)

            room_detail_facilities.setTitleAndDescription(getString(R.string.hotel_room_detail_facilities), previewFacilitiesString)
            room_detail_facilities.info_more.visibility = View.VISIBLE
            room_detail_facilities.infoViewListener = object : InfoTextView.InfoViewListener {
                override fun onMoreClicked() {
                    room_detail_facilities.info_desc.text = fullFacilitiesString
                    room_detail_facilities.resetMaxLineCount()
                    room_detail_facilities.invalidate()
                }
            }
            room_detail_facilities.truncateDescription = false
            room_detail_facilities.descriptionLineCount = ROOM_FACILITY_DEFAULT_COUNT
            room_detail_facilities.buildView()
        }
    }

    fun setupRoomDescription() {
        if (hotelRoom.roomInfo.description.isNotEmpty()) {
            room_detail_description.setTitleAndDescription(getString(R.string.hotel_room_detail_description), hotelRoom.roomInfo.description)
            room_detail_description.buildView()
        }
    }

    fun setupRoomBreakfast() {
        if (hotelRoom.breakfastInfo.mealPlan.isNotEmpty()) {
            room_detail_breakfast.setTitleAndDescription(getString(R.string.hotel_room_detail_breakfast), hotelRoom.breakfastInfo.mealPlan)
            room_detail_breakfast.buildView()
        }
    }

    fun setupRoomExtraBed() {
        if (hotelRoom.extraBedInfo.content.isNotEmpty()) {
            room_detail_extra_bed.setTitleAndDescription(getString(R.string.hotel_room_detail_extra_bed), hotelRoom.extraBedInfo.content)
            room_detail_extra_bed.buildView()
        }
    }

    fun setupRoomPrice() {
        tv_room_detail_price.text = hotelRoom.roomPrice.roomPrice
        room_detail_button.text = getString(R.string.hotel_room_list_choose_room_button)
        room_detail_button.setOnClickListener {
            progressDialog.show()
            room_detail_button.isEnabled = false
            if (userSessionInterface.isLoggedIn) {
                trackingHotelUtil.hotelChooseRoomDetails(hotelRoom)
                roomDetailViewModel.addToCart(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_add_to_cart), addToCartParam)
            } else {
                goToLoginPage()
            }
        }
    }

    fun goToLoginPage() {
        if (activity != null) {
            progressDialog.dismiss()
            RouteManager.route(context, ApplinkConst.LOGIN)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomDetailComponent::class.java).inject(this)
    }

    override fun onErrorRetryClicked() {
    }

    companion object {
        const val EXTRA_ROOM_DATA = "extra_room_data"

        const val MINIMUM_ROOM_COUNT = 3
        const val ROOM_FACILITY_DEFAULT_COUNT = 6

        fun getInstance(savedInstanceId: String, roomIndex: Int): HotelRoomDetailFragment =
                HotelRoomDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelRoomDetailActivity.EXTRA_SAVED_INSTANCE_ID, savedInstanceId)
                        putInt(HotelRoomDetailActivity.EXTRA_ROOM_INDEX, roomIndex)
                    }
                }
    }
}