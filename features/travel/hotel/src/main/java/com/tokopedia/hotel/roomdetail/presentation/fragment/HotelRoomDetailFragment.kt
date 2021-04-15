package com.tokopedia.hotel.roomdetail.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.LeadingMarginSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.data.HotelErrorException
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.FacilityTextView
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.common.util.ErrorHandlerHotel
import com.tokopedia.hotel.common.util.HotelGqlMutation
import com.tokopedia.hotel.roomdetail.di.HotelRoomDetailComponent
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity.Companion.ROOM_DETAIL_SCREEN_NAME
import com.tokopedia.hotel.roomdetail.presentation.viewmodel.HotelRoomDetailViewModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomDetailModel
import com.tokopedia.hotel.roomlist.widget.ImageViewPager
import com.tokopedia.imagepreviewslider.presentation.util.ImagePreviewSlider
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_hotel_room_detail.*
import kotlinx.android.synthetic.main.layout_hotel_image_slider.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*
import javax.inject.Inject
import kotlin.math.roundToLong

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

        roomDetailViewModel.addCartResponseResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    val cartId = it.data.response.cartId
                    context?.run {
                        startActivity(HotelBookingActivity.getCallingIntent(this, cartId))
                    }
                }
                is Fail -> {
                    when {
                        ErrorHandlerHotel.isPhoneNotVerfiedError(it.throwable) -> navigateToAddPhonePage()
                        ErrorHandlerHotel.isGetFailedRoomError(it.throwable) -> {
                            showFailedGetRoomErrorDialog((it.throwable as HotelErrorException).message)
                        }
                        ErrorHandlerHotel.isEmailNotVerifiedError(it.throwable) -> navigateToAddEmailPage()
                        else -> view?.let { v ->
                            Toaster.build(v, ErrorHandler.getErrorMessage(activity, it.throwable), Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                                    getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                        }
                    }
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

    override fun onResume() {
        super.onResume()
        trackingHotelUtil.hotelViewRoomDetail(context, hotelRoom, addToCartParam, roomIndex, ROOM_DETAIL_SCREEN_NAME)
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
        navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
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
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    room_detail_collapsing_toolbar.title = " "
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
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
                    trackingHotelUtil.hotelClickRoomDetailsPhoto(context, hotelRoom.additionalPropertyInfo.propertyId,
                            hotelRoom.roomId, hotelRoom.roomPrice.priceAmount.roundToLong().toString(), ROOM_DETAIL_SCREEN_NAME)
                    ImagePreviewSlider.instance.start(context, hotelRoom.roomInfo.name, roomImageUrls, roomImageUrlsSquare, position, image_banner)
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

        room_detail_header_facilities.removeAllViews()
        context?.run {

            if (hotelRoom.breakfastInfo.breakFast.isNotEmpty()) {
                val breakfastTextView = FacilityTextView(this)
                breakfastTextView.setIconAndText(hotelRoom.breakfastInfo.iconUrl, hotelRoom.breakfastInfo.breakFast)
                room_detail_header_facilities.addView(breakfastTextView)
            }

            if (hotelRoom.refundInfo.refundStatus.isNotEmpty()) {
                val refundableTextView = FacilityTextView(this)
                refundableTextView.setIconAndText(hotelRoom.refundInfo.iconUrl, hotelRoom.refundInfo.refundStatus)
                room_detail_header_facilities.addView(refundableTextView)
            }
        }

        if (hotelRoom.numberRoomLeft <= MINIMUM_ROOM_COUNT) {
            tv_room_detail_count.visibility = View.VISIBLE
            tv_room_detail_count.text = getString(R.string.hotel_room_room_left_text,
                    Integer.toString(hotelRoom.numberRoomLeft))
        }
    }

    private fun setupRoomPayAtHotel() {
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
            pay_at_hotel_title.text = hotelRoom.isDirectPaymentString
            pay_at_hotel_desc.text = spannableString
        }
    }

    private fun setupRoomCancellation() {
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

    private fun setupRoomTax() {
        if (hotelRoom.taxes.isNotEmpty()) {
            room_detail_tax.setTitleAndDescription(getString(R.string.hotel_room_detail_tax), hotelRoom.taxes)
            room_detail_tax.buildView()
        }
    }

    private fun setupRoomDeposit() {
        if (hotelRoom.depositInfo.isNeedDeposit) {
            room_detail_deposit.setTitleAndDescription(getString(R.string.hotel_room_detail_deposit), hotelRoom.depositInfo.depositText)
            room_detail_deposit.buildView()
        }
    }

    private fun setupRoomFacilities() {
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

    private fun setupRoomDescription() {
        if (hotelRoom.roomInfo.description.isNotEmpty()) {
            room_detail_description.setTitleAndDescription(getString(R.string.hotel_room_detail_description), hotelRoom.roomInfo.description)
            room_detail_description.buildView()
        }
    }

    private fun setupRoomBreakfast() {
        if (hotelRoom.breakfastInfo.mealPlan.isNotEmpty()) {
            room_detail_breakfast.setTitleAndDescription(getString(R.string.hotel_room_detail_breakfast), hotelRoom.breakfastInfo.mealPlan)
            room_detail_breakfast.buildView()
        }
    }

    private fun setupRoomExtraBed() {
        if (hotelRoom.extraBedInfo.content.isNotEmpty()) {
            room_detail_extra_bed.setTitleAndDescription(getString(R.string.hotel_room_detail_extra_bed), hotelRoom.extraBedInfo.content)
            room_detail_extra_bed.buildView()
        }
    }

    private fun setupRoomPrice() {
        if (hotelRoom.roomPrice.deals.tagging.isNotEmpty()) {
            room_detail_tagging.show()
            room_detail_tagging.text = hotelRoom.roomPrice.deals.tagging
        } else room_detail_tagging.hide()

        tv_room_detail_price.text = hotelRoom.roomPrice.roomPrice
        room_detail_button.text = getString(R.string.hotel_room_list_choose_room_button)
        room_detail_button.isEnabled = true
        room_detail_button.setOnClickListener {
            progressDialog.show()
            if (userSessionInterface.isLoggedIn) {
                room_detail_button.isEnabled = false
                trackingHotelUtil.hotelChooseRoomDetails(context, hotelRoom, roomIndex, addToCartParam,
                        ROOM_DETAIL_SCREEN_NAME)
                roomDetailViewModel.addToCart(HotelGqlMutation.ADD_TO_CART, addToCartParam)
            } else {
                navigateToLoginPage()
            }
        }
    }

    private fun navigateToLoginPage() {
        if (activity != null) {
            progressDialog.dismiss()
            context?.let { startActivityForResult(RouteManager.getIntent(it, ApplinkConst.LOGIN), REQ_CODE_LOGIN) }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE_LOGIN -> if (resultCode == Activity.RESULT_OK) {
                progressDialog.show()
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            }
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelRoomDetailComponent::class.java).inject(this)
    }

    override fun onErrorRetryClicked() {
    }

    private fun navigateToAddPhonePage() {
        RouteManager.route(requireContext(), ApplinkConstInternalGlobal.ADD_PHONE)
    }

    private fun navigateToAddEmailPage() {
        RouteManager.route(context, ApplinkConstInternalGlobal.ADD_EMAIL)
    }

    private fun showFailedGetRoomErrorDialog(message: String) {
        val dialog = DialogUnify(activity as AppCompatActivity, DialogUnify.SINGLE_ACTION, DialogUnify.WITH_ICON)
        dialog.setTitle(getString(R.string.hotel_room_list_failed_get_room_availability_error_title))
        dialog.setDescription(message)
        dialog.setImageDrawable(R.drawable.ic_hotel_room_error_refresh)
        dialog.setPrimaryCTAText(getString(R.string.hotel_room_list_failed_get_room_availability_cta_title))
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
            progressDialog.show()
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }
        dialog.setCancelable(false)
        dialog.setOverlayClose(false)
        dialog.show()
    }

    companion object {
        const val EXTRA_ROOM_DATA = "extra_room_data"

        const val MINIMUM_ROOM_COUNT = 3
        const val ROOM_FACILITY_DEFAULT_COUNT = 6
        const val REQ_CODE_LOGIN = 1345

        fun getInstance(savedInstanceId: String, roomIndex: Int): HotelRoomDetailFragment =
                HotelRoomDetailFragment().also {
                    it.arguments = Bundle().apply {
                        putString(HotelRoomDetailActivity.EXTRA_SAVED_INSTANCE_ID, savedInstanceId)
                        putInt(HotelRoomDetailActivity.EXTRA_ROOM_INDEX, roomIndex)
                    }
                }
    }
}