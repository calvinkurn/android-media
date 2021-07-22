package com.tokopedia.hotel.booking.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.applink.internal.ApplinkConstInternalTravel
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.data.model.*
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity.Companion.HOTEL_BOOKING_SCREEN_NAME
import com.tokopedia.hotel.booking.presentation.activity.HotelPayAtHotelPromoActivity
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.booking.presentation.widget.HotelBookingBottomSheets
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.HotelGqlMutation
import com.tokopedia.hotel.common.util.HotelGqlQuery
import com.tokopedia.hotel.common.util.TRACKING_HOTEL_CHECKOUT
import com.tokopedia.hotel.databinding.FragmentHotelBookingBinding
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.data.PromoCheckoutCommonQueryConst
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.ButtonPromoCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.travel.passenger.presentation.activity.TravelContactDataActivity
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.presentation.widget.TravellerInfoWidget
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.android.synthetic.main.widget_info_text_view.view.*
import javax.inject.Inject


class HotelBookingFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    private var binding by autoClearedNullable<FragmentHotelBookingBinding>()

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    private var performanceMonitoring: PerformanceMonitoring? = null
    private var isTraceStop = false

    lateinit var hotelCart: HotelCart
    var hotelBookingPageModel = HotelBookingPageModel()
    var promoCode = ""

    lateinit var progressDialog: ProgressDialog

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    var roomRequestMaxCharCount = ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        performanceMonitoring = PerformanceMonitoring.start(TRACKING_HOTEL_CHECKOUT)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(HotelBookingViewModel::class.java)
        }

        arguments?.let {
            hotelBookingPageModel.cartId = it.getString(ARG_CART_ID, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.hotelCartResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    hotelCart = it.data.response
                    initView()
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
            stopTrace()
        })

        bookingViewModel.hotelCheckoutResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    context?.run {
                        val checkoutData = PaymentPassData()
                        checkoutData.queryString = it.data.queryString
                        checkoutData.redirectUrl = it.data.redirectUrl
                        val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                        val intent = RouteManager.getIntent(context, paymentCheckoutString)
                        intent?.run {
                            putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutData)
                            startActivityForResult(this, REQUEST_CODE_CHECKOUT)
                        }
                    }

                }
                is Fail -> {
                    val message = when (it.throwable is MessageErrorException) {
                        true -> it.throwable.message ?: ""
                        false -> ErrorHandler.getErrorMessage(activity, it.throwable)
                    }
                    view?.let { v ->
                        Toaster.build(v, message, Toaster.LENGTH_INDEFINITE, Toaster.TYPE_ERROR,
                                getString(com.tokopedia.resources.common.R.string.general_label_ok)).show()
                    }
                }
            }
        })

        bookingViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
            contactList?.let { travelContactArrayAdapter.updateItem(it.toMutableList()) }
        })

        bookingViewModel.tickerData.observe(viewLifecycleOwner, Observer {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelBookingBinding.inflate(inflater, container, false)
        binding?.root?.setBackgroundResource(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_BOOKING_MODEL)) {
            hotelBookingPageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_BOOKING_MODEL)
                    ?: HotelBookingPageModel()
        }
        initProgressDialog()
        initGuestInfoEditText()
        showLoadingBar()

        bookingViewModel.fetchTickerData()
        bookingViewModel.getCartData(HotelGqlQuery.GET_CART, hotelBookingPageModel.cartId)
        bookingViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_CONTACT_DATA -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.run {
                        hotelBookingPageModel.contactData = this.getParcelableExtra(HotelContactDataFragment.EXTRA_CONTACT_DATA)
                        renderContactData()
                    }
                }
            }

            REQUEST_CODE_CHECKOUT -> {
                when (resultCode) {
                    PaymentConstant.PAYMENT_SUCCESS, PaymentConstant.PAYMENT_FAILED -> {
                        context?.run {
                            val taskStackBuilder = TaskStackBuilder.create(this)

                            val intentHome = RouteManager.getIntent(this, ApplinkConst.HOME)
                            taskStackBuilder.addNextIntent(intentHome)
                            val intent = RouteManager.getIntent(this, ApplinkConstInternalTravel.DASHBOARD_HOTEL)
                            intent?.run {
                                taskStackBuilder.addNextIntent(this)
                                taskStackBuilder.startActivities()
                            }
                        }
                    }
                }
            }

            COUPON_EXTRA_LIST_ACTIVITY_RESULT, COUPON_EXTRA_DETAIL_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        if (it.hasExtra(COUPON_EXTRA_PROMO_DATA)) {
                            it.getParcelableExtra<PromoData>(COUPON_EXTRA_PROMO_DATA)?.let { itemPromoData ->
                                bookingViewModel.applyPromoData(itemPromoData)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_HOTEL_BOOKING_MODEL, hotelBookingPageModel)
    }

    private fun initView() {
        hideLoadingBar()

        setupHotelInfo(hotelCart.property)
        setupRoomDuration(hotelCart.property, hotelCart.cart)
        setupRoomInfo(hotelCart.property, hotelCart.cart)
        setupRoomRequestForm(hotelCart.cart)
        setupContactDetail(hotelCart.cart)
        setupInvoiceSummary(hotelCart.cart, hotelCart.property)
        setupImportantNotes(hotelCart.property)
        initPromoSection()

        binding?.bookingButton?.setOnClickListener { onBookingButtonClicked() }
    }

    private fun initPromoSection(){
        bookingViewModel.promoData.observe(viewLifecycleOwner, Observer {
            promoCode = it.promoCode
            setupPayNowPromoTicker(it)
        })
    }

    private fun initGuestInfoEditText() {
        binding?.tvGuestInput?.setHint(getString(R.string.hotel_booking_guest_form_hint))
        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv,
                    arrayListOf(), object : TravelContactArrayAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return binding?.tvGuestInput?.getEditableValue() ?: ""
                }
            })
            (binding?.tvGuestInput?.getAutoCompleteTextView() as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.hotel_progress_dialog_title))
        progressDialog.setCancelable(false)
    }

    private fun showLoadingBar() {
        binding?.hotelBookingContainer?.visibility = View.GONE
        binding?.hotelBookingLoadingBar?.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        binding?.hotelBookingContainer?.visibility = View.VISIBLE
        binding?.hotelBookingLoadingBar?.visibility = View.GONE
    }

    private fun hideTickerView() {
        binding?.hotelBookingTicker?.hide()
    }

    private fun renderTickerView(travelTickerModel: TravelTickerModel) {
        if (travelTickerModel.title.isNotEmpty()) binding?.hotelBookingTicker?.tickerTitle = travelTickerModel.title
        var message = travelTickerModel.message
        if (travelTickerModel.url.isNotEmpty()) message += getString(R.string.hotel_ticker_desc, travelTickerModel.url)
        binding?.hotelBookingTicker?.setHtmlDescription(message)
        binding?.hotelBookingTicker?.tickerType = Ticker.TYPE_WARNING
        binding?.hotelBookingTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                if (linkUrl.isNotEmpty()) {
                    RouteManager.route(context, linkUrl.toString())
                }
            }

            override fun onDismiss() {}

        })
        if (travelTickerModel.url.isNotEmpty()) {
            binding?.hotelBookingTicker?.setOnClickListener {
                RouteManager.route(requireContext(), travelTickerModel.url)
            }
        }

        binding?.hotelBookingTicker?.show()
    }

    private fun setupHotelInfo(property: HotelPropertyData) {
        binding?.tvHotelInfoName?.text = property.name
        binding?.tvHotelInfoPropertyType?.text = property.type
        for (i in 1..property.star) {
            context?.run { binding?.hotelInfoRatingContainer?.addView(RatingStarView(this)) }
        }
        binding?.tvHotelInfoAddress?.text = property.address
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding?.ivHotelInfoImage?.clipToOutline = true
        }
        binding?.ivHotelInfoImage?.loadImage(property.image.urlMax300, com.tokopedia.iconunify.R.drawable.iconunify_image_broken)
    }

    private fun setupRoomDuration(property: HotelPropertyData, cart: HotelCartData) {
        binding?.bookingRoomDurationInfo?.setRoomDates(cart.checkIn, cart.checkOut)
        binding?.bookingRoomDurationInfo?.setRoomCheckTimes(property.checkInFrom, property.checkOutTo)
    }

    private fun setupRoomInfo(property: HotelPropertyData, cart: HotelCartData) {
        if (property.rooms.isNotEmpty()) {
            binding?.tvBookingRoomInfoTitle?.text = property.rooms[0].roomName

            if (!property.isDirectPayment) {
                binding?.tvBookingRoomInfoPayAtHotel?.let {
                    it.visibility = View.VISIBLE
                    it.text = property.isDirectPaymentString
                }
            }

            binding?.tvBookingRoomInfoOccupancy?.text = cart.roomContent
            if (!property.rooms[0].isBreakFastIncluded) binding?.tvBookingRoomInfoBreakfast?.visibility = View.GONE

            val cancellationPolicy = property.rooms[0].cancellationPolicies
            if (cancellationPolicy.title.isEmpty()) { // Hide cancellation info ticker
                binding?.cancellationPolicyTicker?.visibility = View.GONE
            } else {
                binding?.cancellationPolicyTicker?.tickerTitle = cancellationPolicy.title
                if (cancellationPolicy.isClickable) {
                    binding?.cancellationPolicyTicker?.setHtmlDescription(getString(R.string.hotel_booking_cancellation_ticker, cancellationPolicy.content))
                    binding?.cancellationPolicyTicker?.setOnClickListener { onCancellationPolicyClicked(property) }
                } else {
                    binding?.cancellationPolicyTicker?.setTextDescription(cancellationPolicy.content)
                }
            }
        }
    }

    private fun onCancellationPolicyClicked(property: HotelPropertyData) {
        if (property.rooms.isNotEmpty()) {
            val hotelCancellationPolicyBottomSheets = HotelBookingBottomSheets()
            hotelCancellationPolicyBottomSheets.setTitle(getString(R.string.hotel_booking_cancellation_policy_title))

            for (policy in property.rooms[0].cancellationPolicies.details) {
                context?.run {
                    val policyView = InfoTextView(this)
                    policyView.setTitleAndDescription(policy.longTitle, policy.longDesc)
                    policyView.rootView.info_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    policyView.rootView.info_container.setMargin(0, 0, 0, policyView.rootView.info_container.getDimens(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2))
                    hotelCancellationPolicyBottomSheets.addContentView(policyView)
                }
            }

            activity?.run { hotelCancellationPolicyBottomSheets.show(this.supportFragmentManager, TAG_HOTEL_CANCELLATION_POLICY) }
        }
    }

    private fun setupRoomRequestForm(cart: HotelCartData) {
        if (cart.specialRequest.isNotEmpty()) hotelBookingPageModel.roomRequest = cart.specialRequest
        if (hotelBookingPageModel.roomRequest.isNotEmpty()) {
            showRequestForm()
            binding?.tvRoomRequestInput?.textFieldInput?.setText(hotelBookingPageModel.roomRequest)
        } else {
            binding?.addRequestContainer?.setOnClickListener {
                showRequestForm()
            }
        }

        binding?.tvRoomRequestInput?.setCounter(roomRequestMaxCharCount)
        binding?.tvRoomRequestInput?.textFieldInput?.inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }

    private fun showRequestForm() {
        binding?.addRequestContainer?.visibility = View.GONE
        binding?.roomRequestFormContainer?.visibility = View.VISIBLE
    }

    private fun setupContactDetail(cart: HotelCartData) {

        // Check if contact data is empty
        if (hotelBookingPageModel.contactData.isEmpty() || hotelBookingPageModel.contactData.email.isEmpty()) {
            val initContactData = cart.contact
            hotelBookingPageModel.contactData = TravelContactData(
                    name = initContactData.name,
                    email = initContactData.email,
                    phoneCode = initContactData.phoneCode,
                    phone = initContactData.phone
            )
        }
        renderContactData()

        binding?.widgetTravellerInfo?.setListener(object : TravellerInfoWidget.TravellerInfoWidgetListener {
            override fun onClickEdit() {
                context?.run {
                    startActivityForResult(TravelContactDataActivity.getCallingIntent(this, hotelBookingPageModel.contactData,
                            TravelContactDataActivity.HOTEL),
                            REQUEST_CODE_CONTACT_DATA)
                }
            }

        })

        if (hotelBookingPageModel.guestName.isNotEmpty() && hotelBookingPageModel.isForOtherGuest == 1) {
            binding?.radioButtonContactGuest?.isChecked = true
            binding?.tvGuestInput?.setEditableText(hotelBookingPageModel.guestName)
            toggleShowGuestForm(true)
        }
        binding?.radioGroupContact?.setOnCheckedChangeListener { _, checkedId ->
            if (binding?.radioButtonContactGuest?.id == checkedId) {
                toggleShowGuestForm(true)
                hotelBookingPageModel.isForOtherGuest = 1
            } else {
                toggleShowGuestForm(false)
                hotelBookingPageModel.isForOtherGuest = 0
            }
        }


        binding?.tvGuestInput?.setLabel(getString(R.string.hotel_booking_guest_form_title))
        toggleGuestFormError(false)
    }

    private fun renderContactData() {
        val contactData = hotelBookingPageModel.contactData
        binding?.widgetTravellerInfo?.let {
            it.setContactName(contactData.name)
            it.setContactEmail(contactData.email)
            it.setContactPhoneNum(getString(R.string.hotel_booking_contact_detail_phone_number,
                contactData.phoneCode, contactData.phone))
            it.invalidate()
        }
    }

    private fun toggleShowGuestForm(value: Boolean) {
        when (value) {
            true -> binding?.guestFormContainer?.visibility = View.VISIBLE
            false -> binding?.guestFormContainer?.visibility = View.GONE
        }
    }

    private fun toggleGuestFormError(value: Boolean) {
        val noticeString = getString(R.string.hotel_booking_guest_form_notice)
        when (value) {
            true -> {
                binding?.tvGuestInput?.setError(noticeString)
            }
            false -> {
                binding?.tvGuestInput?.setHelper(noticeString)
            }
        }
    }

    private fun setupPayNowPromoTicker(promoData: PromoData) {
        if (promoData.promoCode.isEmpty()){
            setupPromoTicker(TickerCheckoutView.State.ACTIVE, getString(R.string.hotel_promo_btn_default_title))
            binding?.bookingPayNowPromoTicker?.chevronIcon = com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_right_grayscale_24
        }else if (promoData.promoCode.isNotEmpty() && hotelCart.property.isDirectPayment){
            setupPromoTicker(TickerCheckoutView.State.ACTIVE,
                    promoData.title,
                    promoData.description)
            binding?.bookingPayNowPromoTicker?.chevronIcon = com.tokopedia.resources.common.R.drawable.ic_system_action_close_grayscale_24
        }

        if(hotelCart.property.isDirectPayment){
            binding?.bookingPayNowPromoTicker?.setOnClickListener { onClickUsePromo(promoData) }

            binding?.bookingPayNowPromoTicker?.setListenerChevronIcon {
                if (binding?.bookingPayNowPromoTicker?.desc?.isNotEmpty() == true) {
                    binding?.bookingPayNowPromoTicker?.state = ButtonPromoCheckoutView.State.LOADING
                    onResetPromo()
                } else {
                    onClickUsePromo(promoData)
                }
            }
        }
        else{
            binding?.bookingPayNowPromoTicker?.setOnClickListener {
                startActivity(HotelPayAtHotelPromoActivity.getCallingIntent(requireContext()))
            }
        }
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State,
                                 title: String = "",
                                 description: String = "") {

        if (state == TickerCheckoutView.State.ACTIVE) {
            binding?.bookingPayNowPromoTicker?.title = title
            binding?.bookingPayNowPromoTicker?.desc = description
            binding?.bookingPayNowPromoTicker?.state = ButtonPromoCheckoutView.State.ACTIVE
        }else if(state == TickerCheckoutView.State.EMPTY){
            binding?.bookingPayNowPromoTicker?.state = ButtonPromoCheckoutView.State.LOADING
        }
    }

    private fun onClickUsePromo(promoData: PromoData){
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_HOTEL)
        intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, promoData.isActive())
        intent.putExtra(COUPON_EXTRA_CART_ID, hotelCart.cartID)
        startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
    }

    private fun onResetPromo(){
        bookingViewModel.applyPromoData(PromoData(state = TickerCheckoutView.State.ACTIVE))
        bookingViewModel.onCancelAppliedVoucher(getCancelVoucherQuery())
    }

    private fun setupInvoiceSummary(cart: HotelCartData, property: HotelPropertyData) {
        cart.fares.find { it.type == "base_price" }?.let {
            binding?.tvRoomPriceLabel?.text = it.description
            binding?.tvRoomPrice?.text = if (cart.localCurrency.isEmpty()) it.price else it.localPrice
        }
        cart.fares.find { it.type == "tax" }?.let {
            binding?.tvRoomTaxLabel?.text = it.description
            binding?.tvRoomTax?.text = if (cart.localCurrency.isEmpty()) it.price else it.localPrice
        }

        val priceLabelResId = if (!property.isDirectPayment) R.string.hotel_booking_invoice_estimate_pay_at_hotel else R.string.hotel_booking_invoice_estimate_pay_now
        val price: String
        if (cart.localCurrency.isEmpty()) {
            price = cart.totalPrice
        } else {
            price = cart.localTotalPrice

            // Show price estimate in IDR
            binding?.tvInvoiceForeignCurrency?.visibility = View.VISIBLE
            val spannableString = SpannableString(getString(R.string.hotel_booking_invoice_foreign_currency_label, cart.totalPrice))
            spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - cart.totalPrice.length, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding?.tvInvoiceForeignCurrency?.text = spannableString
        }
        binding?.tvRoomEstimatedPriceLabel?.text = getString(priceLabelResId)
        binding?.tvRoomEstimatedPrice?.text = price
        context?.run { binding?.tvRoomEstimatedPrice?.setTextColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Y500)) }
    }

    private fun setupImportantNotes(property: HotelPropertyData) {
        if (property.rooms.isNotEmpty() && property.paymentNote.isNotEmpty()) {
            binding?.hotelBookingImportantNotes?.visibility = View.VISIBLE

            val notesDescription = getString(R.string.hotel_booking_important_notes)
            val expandNotesLabel = getString(R.string.hotel_read_more_title)
            val spannableString = SpannableString("$notesDescription $expandNotesLabel")
            val moreInfoSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    onImportantNotesClicked(property.paymentNote)
                }
            }
            spannableString.setSpan(moreInfoSpan, spannableString.length - expandNotesLabel.length, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            context?.run {
                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_G200)),
                        spannableString.length - expandNotesLabel.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            binding?.tvBookingImportantNotes?.text = spannableString
            binding?.tvBookingImportantNotes?.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onImportantNotesClicked(notes: String) {
        context?.run {
            val importantNotesBottomSheets = HotelBookingBottomSheets()
            val textView = Typography(this)
            textView.text = notes
            importantNotesBottomSheets.setTitle(getString(R.string.hotel_important_info_title))
            importantNotesBottomSheets.addContentView(textView)
            activity?.let {
                importantNotesBottomSheets.show(it.supportFragmentManager, TAG_HOTEL_IMPORTANT_NOTES)
            }
        }
    }

    private fun onBookingButtonClicked() {
        progressDialog.show()
        if (validateData()) {
            if (binding?.radioButtonContactGuest?.isChecked == true && binding?.tvGuestInput?.getEditableValue()?.isNotEmpty() == true)
                hotelBookingPageModel.guestName = binding?.tvGuestInput?.getEditableValue() ?: ""
            else hotelBookingPageModel.guestName = hotelBookingPageModel.contactData.name
            hotelBookingPageModel.roomRequest = binding?.tvRoomRequestInput?.getEditableValue().toString()
            trackingHotelUtil.hotelClickNext(context, hotelCart,
                    hotelCart.property.type, hotelCart.property.name,
                    hotelCart.cart.rooms.firstOrNull()?.numOfRooms ?: 1,
                    hotelCart.cart.adult,
                    hotelBookingPageModel.isForOtherGuest == 0, HOTEL_BOOKING_SCREEN_NAME)

            hotelBookingPageModel.promoCode = promoCode

            val hotelCheckoutParam = HotelCheckoutParam(
                    cartId = hotelBookingPageModel.cartId,
                    contact = mapToCheckoutContact(hotelBookingPageModel.contactData),
                    guestName = hotelBookingPageModel.guestName,
                    promoCode = hotelBookingPageModel.promoCode,
                    specialRequest = hotelBookingPageModel.roomRequest
            )
            bookingViewModel.checkoutCart(HotelGqlMutation.CHECKOUT, hotelCheckoutParam)
        } else {
            progressDialog.dismiss()
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        if ((binding?.tvRoomRequestInput?.getEditableValue().toString().length) > roomRequestMaxCharCount) isValid = false
        if (binding?.radioButtonContactGuest?.isChecked == true && binding?.tvGuestInput?.getEditableValue()?.isEmpty() == true) {
            toggleGuestFormError(true)
            isValid = false
        } else if (binding?.tvGuestInput?.getEditableValue()?.isNotEmpty() == true && !validateNameIsAlphabetOnly(binding?.tvGuestInput?.getEditableValue() ?: "")) {
            toggleGuestFormError(true)
            isValid = false
        }
        return isValid
    }

    private fun validateNameIsAlphabetOnly(expression: String): Boolean = expression.matches(REGEX_IS_ALPHANUMERIC_ONLY.toRegex())

    private fun mapToCheckoutContact(contactData: TravelContactData): HotelCartData.BookingContact {
        return HotelCartData.BookingContact(
                name = contactData.name,
                email = contactData.email,
                phoneCode = contactData.phoneCode,
                phone = contactData.phone
        )
    }

    override fun getScreenName(): String = "Pembayaran"

    override fun initInjector() {
        getComponent(HotelBookingComponent::class.java).inject(this)
    }

    override fun onErrorRetryClicked() {
        bookingViewModel.getCartData(HotelGqlQuery.GET_CART, hotelBookingPageModel.cartId)
    }

    private fun getCancelVoucherQuery(): String = PromoCheckoutCommonQueryConst.QUERY_FLIGHT_CANCEL_VOUCHER

    private fun stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring?.stopTrace()
            isTraceStop = true
        }
    }

    companion object {
        const val ARG_CART_ID = "arg_cart_id"
        const val ARG_DESTINATION_TYPE = "arg_destination_type"
        const val ARG_DESTINATION_NAME = "arg_destination_name"
        const val ARG_ROOM_COUNT = "arg_room_count"
        const val ARG_GUEST_COUNT = "arg_guest_count"
        const val EXTRA_HOTEL_BOOKING_MODEL = "extra_hotel_booking_model"
        const val EXTRA_PARAMETER_TOP_PAY_DATA = "EXTRA_PARAMETER_TOP_PAY_DATA"
        const val REQUEST_CODE_CONTACT_DATA = 104
        const val REQUEST_CODE_CHECKOUT = 105
        const val TAG_HOTEL_CANCELLATION_POLICY = "hotel_cancellation_policy"
        const val TAG_HOTEL_TAX_POLICY = "hotel_tax_policy"
        const val TAG_HOTEL_IMPORTANT_NOTES = "hotel_important_notes"
        const val ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT = 250

        const val COUPON_EXTRA_COUPON_ACTIVE = "EXTRA_COUPON_ACTIVE"
        const val COUPON_EXTRA_CART_ID = "EXTRA_CART_ID"
        const val COUPON_EXTRA_PROMO_CODE = "EXTRA_PROMO_CODE"
        const val COUPON_EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        const val COUPON_EXTRA_LIST_ACTIVITY_RESULT = 3121
        const val COUPON_EXTRA_DETAIL_ACTIVITY_RESULT = 3122
        const val COUPON_EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"

        private const val REGEX_IS_ALPHANUMERIC_ONLY = "^[a-zA-Z\\s]*$"


        fun getInstance(cartId: String): HotelBookingFragment =
                HotelBookingFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CART_ID, cartId)
                    }
                }
    }
}
