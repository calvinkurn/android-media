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
import com.tokopedia.hotel.databinding.FragmentHotelRoomDetailBinding
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
import com.tokopedia.utils.lifecycle.autoClearedNullable
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

    private var binding by autoClearedNullable<FragmentHotelRoomDetailBinding>()

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
            binding?.roomDetailButton?.isEnabled = true
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelRoomDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

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
        val toolbar = binding?.roomDetailDetailToolbar
        (activity as HotelRoomDetailActivity).setSupportActionBar(toolbar)
        (activity as HotelRoomDetailActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navIcon = binding?.roomDetailDetailToolbar?.navigationIcon
        navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0), PorterDuff.Mode.SRC_ATOP)
        (activity as HotelRoomDetailActivity).supportActionBar?.setHomeAsUpIndicator(navIcon)

        binding?.roomDetailCollapsingToolbar?.title = ""
        binding?.roomDetailAppBarLayout?.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    binding?.roomDetailCollapsingToolbar?.title = hotelRoom.roomInfo.name
                    navIcon?.setColorFilter(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96), PorterDuff.Mode.SRC_ATOP)
                    isShow = true
                } else if (isShow) {
                    binding?.roomDetailCollapsingToolbar?.title = " "
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

            if (roomImageUrls300.size >= 5) binding?.roomDetailImages?.setImages(roomImageUrls300.subList(0, 5))
            else binding?.roomDetailImages?.setImages(roomImageUrls300)

            binding?.roomDetailImages?.imageViewPagerListener = object : ImageViewPager.ImageViewPagerListener {
                override fun onImageClicked(position: Int) {
                    trackingHotelUtil.hotelClickRoomDetailsPhoto(context, hotelRoom.additionalPropertyInfo.propertyId,
                            hotelRoom.roomId, hotelRoom.roomPrice.priceAmount.roundToLong().toString(), ROOM_DETAIL_SCREEN_NAME)
                    ImagePreviewSlider.instance.start(context, hotelRoom.roomInfo.name, roomImageUrls, roomImageUrlsSquare, position, image_banner)
                }
            }
        }
        binding?.roomDetailImages?.buildView()
    }

    private fun setupRoomHeader() {
        binding?.tvRoomDetailTitle?.text = hotelRoom.roomInfo.name
        binding?.tvRoomDetailOccupancy?.text = getString(R.string.hotel_room_detail_header_occupancy,
                hotelRoom.occupancyInfo.occupancyText)
        binding?.tvRoomDetailSize?.text = getString(R.string.hotel_room_detail_header_room_size, hotelRoom.roomInfo.size, hotelRoom.bedInfo)

        binding?.roomDetailHeaderFacilities?.removeAllViews()
        context?.run {

            if (hotelRoom.breakfastInfo.breakFast.isNotEmpty()) {
                val breakfastTextView = FacilityTextView(this)
                breakfastTextView.setIconAndText(hotelRoom.breakfastInfo.iconUrl, hotelRoom.breakfastInfo.breakFast)
                binding?.roomDetailHeaderFacilities?.addView(breakfastTextView)
            }

            if (hotelRoom.refundInfo.refundStatus.isNotEmpty()) {
                val refundableTextView = FacilityTextView(this)
                refundableTextView.setIconAndText(hotelRoom.refundInfo.iconUrl, hotelRoom.refundInfo.refundStatus)
                binding?.roomDetailHeaderFacilities?.addView(refundableTextView)
            }
        }

        if (hotelRoom.numberRoomLeft <= MINIMUM_ROOM_COUNT) {
            binding?.tvRoomDetailCount?.visibility = View.VISIBLE
            binding?.tvRoomDetailCount?.text = getString(R.string.hotel_room_room_left_text,
                    Integer.toString(hotelRoom.numberRoomLeft))
        }
    }

    private fun setupRoomPayAtHotel() {
        if (!hotelRoom.additionalPropertyInfo.isDirectPayment) {
            binding?.payAtHotelContainer?.visibility = View.VISIBLE

            val iconId = if (hotelRoom.creditCardInfo.isCCRequired)
                R.drawable.ic_pay_at_hotel_cc else R.drawable.ic_pay_at_hotel_no_cc
            binding?.payAtHotelIcon?.setBackgroundResource(iconId)

            val spannableString = SpannableString(" " + hotelRoom.creditCardInfo.header
                    + "\n" + hotelRoom.creditCardInfo.creditCardInfo)
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 1, 1 + hotelRoom.creditCardInfo.header.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(LeadingMarginSpan.Standard(50, 0), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.payAtHotelTitle?.text = hotelRoom.isDirectPaymentString
            binding?.payAtHotelDesc?.text = spannableString
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

            binding?.roomDetailCancellation?.setTitleAndDescription(getString(R.string.hotel_room_detail_cancellation), spannableStringBuilder)
            binding?.roomDetailCancellation?.truncateDescription = false
            binding?.roomDetailCancellation?.buildView()
        }
    }

    private fun setupRoomTax() {
        if (hotelRoom.taxes.isNotEmpty()) {
            binding?.roomDetailTax?.setTitleAndDescription(getString(R.string.hotel_room_detail_tax), hotelRoom.taxes)
            binding?.roomDetailTax?.buildView()
        }
    }

    private fun setupRoomDeposit() {
        if (hotelRoom.depositInfo.isNeedDeposit) {
            binding?.roomDetailDeposit?.setTitleAndDescription(getString(R.string.hotel_room_detail_deposit), hotelRoom.depositInfo.depositText)
            binding?.roomDetailDeposit?.buildView()
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

            binding?.let {
                it.roomDetailFacilities.setTitleAndDescription(getString(R.string.hotel_room_detail_facilities), previewFacilitiesString)
                it.roomDetailFacilities.info_more.visibility = View.VISIBLE
                it.roomDetailFacilities.infoViewListener = object : InfoTextView.InfoViewListener {
                    override fun onMoreClicked() {
                        it.roomDetailFacilities.info_desc.text = fullFacilitiesString
                        it.roomDetailFacilities.resetMaxLineCount()
                        it.roomDetailFacilities.invalidate()
                    }
                }
                it.roomDetailFacilities.truncateDescription = false
                it.roomDetailFacilities.descriptionLineCount = ROOM_FACILITY_DEFAULT_COUNT
                it.roomDetailFacilities.buildView()
            }
        }
    }

    private fun setupRoomDescription() {
        if (hotelRoom.roomInfo.description.isNotEmpty()) {
            binding?.roomDetailDescription?.setTitleAndDescription(getString(R.string.hotel_room_detail_description), hotelRoom.roomInfo.description)
            binding?.roomDetailDescription?.buildView()
        }
    }

    private fun setupRoomBreakfast() {
        if (hotelRoom.breakfastInfo.mealPlan.isNotEmpty()) {
            binding?.roomDetailBreakfast?.setTitleAndDescription(getString(R.string.hotel_room_detail_breakfast), hotelRoom.breakfastInfo.mealPlan)
            binding?.roomDetailBreakfast?.buildView()
        }
    }

    private fun setupRoomExtraBed() {
        if (hotelRoom.extraBedInfo.content.isNotEmpty()) {
            binding?.roomDetailExtraBed?.setTitleAndDescription(getString(R.string.hotel_room_detail_extra_bed), hotelRoom.extraBedInfo.content)
            binding?.roomDetailExtraBed?.buildView()
        }
    }

    private fun setupRoomPrice() {
        binding?.let {
            if (hotelRoom.roomPrice.deals.tagging.isNotEmpty()) {
                it.roomDetailTagging.show()
                it.roomDetailTagging.text = hotelRoom.roomPrice.deals.tagging
            } else it.roomDetailTagging.hide()

            it.tvRoomDetailPrice.text = hotelRoom.roomPrice.roomPrice
            it.roomDetailButton.text = getString(R.string.hotel_room_list_choose_room_button)
            it.roomDetailButton.isEnabled = true
            it.roomDetailButton.setOnClickListener {_ ->
                progressDialog.show()
                if (userSessionInterface.isLoggedIn) {
                    it.roomDetailButton.isEnabled = false
                    trackingHotelUtil.hotelChooseRoomDetails(context, hotelRoom, roomIndex, addToCartParam,
                        ROOM_DETAIL_SCREEN_NAME)
                    roomDetailViewModel.addToCart(HotelGqlMutation.ADD_TO_CART, addToCartParam)
                } else {
                    navigateToLoginPage()
                }
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