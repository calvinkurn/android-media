package com.tokopedia.hotel.booking.presentation.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.common.travel.presentation.activity.TravelContactDataActivity
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.widget.TravelContactArrayAdapter
import com.tokopedia.common.travel.widget.TravellerInfoWidget
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.data.model.*
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.booking.presentation.widget.HotelBookingBottomSheets
import com.tokopedia.hotel.common.analytics.TrackingHotelUtil
import com.tokopedia.hotel.common.presentation.HotelBaseFragment
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_booking.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*
import javax.inject.Inject


class HotelBookingFragment : HotelBaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    @Inject
    lateinit var trackingHotelUtil: TrackingHotelUtil

    lateinit var hotelCart: HotelCart
    lateinit var appliedVoucher: HotelCart.AppliedVoucher
    var hotelBookingPageModel = HotelBookingPageModel()
    var promoCode = ""
    internal var destinationType: String = ""
    internal var destinationName: String = ""

    lateinit var progressDialog: ProgressDialog

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    var roomRequestMaxCharCount = ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(HotelBookingViewModel::class.java)
        }

        arguments?.let {
            hotelBookingPageModel.cartId = it.getString(ARG_CART_ID, "")
            destinationType = it.getString(ARG_DESTINATION_TYPE, "")
            destinationName = it.getString(ARG_DESTINATION_NAME, "")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.hotelCartResult.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is Success -> {
                    hotelCart = it.data.response
                    appliedVoucher = it.data.appliedVoucher
                    initView()
                }
                is Fail -> {
                    showErrorState(it.throwable)
                }
            }
        })

        bookingViewModel.hotelCheckoutResult.observe(this, androidx.lifecycle.Observer {
            progressDialog.dismiss()
            when (it) {
                is Success -> {
                    val checkoutData = PaymentPassData()
                    checkoutData.queryString = it.data.queryString
                    checkoutData.redirectUrl = it.data.redirectUrl
                    val paymentCheckoutString = ApplinkConstInternalPayment.PAYMENT_CHECKOUT
                    val intent = RouteManager.getIntent(context, paymentCheckoutString)
                    intent?.run {
                        putExtra(EXTRA_PARAMETER_TOP_PAY_DATA, checkoutData)
                        startActivityForResult(intent, REQUEST_CODE_CHECKOUT)
                    }
                }
                is Fail -> {
                    val message = when (it.throwable is MessageErrorException) {
                        true -> it.throwable.message ?: ""
                        false -> ErrorHandler.getErrorMessage(activity, it.throwable)
                    }
                    NetworkErrorHelper.showRedSnackbar(activity, message)
                }
            }
        })

        bookingViewModel.contactListResult.observe(this, androidx.lifecycle.Observer { contactList ->
            contactList?.let{ travelContactArrayAdapter.updateItem(it.toMutableList()) }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_booking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_BOOKING_MODEL)) {
            hotelBookingPageModel = savedInstanceState.getParcelable(EXTRA_HOTEL_BOOKING_MODEL)
                    ?: HotelBookingPageModel()
        }
        initProgressDialog()
        initGuestInfoEditText()
        showLoadingBar()

        bookingViewModel.getCartData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_cart), hotelBookingPageModel.cartId)
        bookingViewModel.getContactList(GraphqlHelper.loadRawString(resources, com.tokopedia.common.travel.R.raw.query_get_travel_contact_list))
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

            COUPON_EXTRA_LIST_ACTIVITY_RESULT, COUPON_EXTRA_DETAIL_ACTIVITY_RESULT -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.let {
                        if (it.hasExtra(COUPON_EXTRA_PROMO_DATA)) {
                            val itemPromoData = it.getParcelableExtra<PromoData>(COUPON_EXTRA_PROMO_DATA)
                            promoCode = itemPromoData.promoCode
                            when (itemPromoData.state) {
                                TickerCheckoutView.State.EMPTY -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.EMPTY,
                                            "",
                                            "")
                                }
                                TickerCheckoutView.State.FAILED -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.FAILED,
                                            itemPromoData?.title.toEmptyStringIfNull(),
                                            itemPromoData?.description.toEmptyStringIfNull())

                                }
                                TickerCheckoutView.State.ACTIVE -> {
                                    setupPromoTicker(TickerCheckoutView.State.ACTIVE,
                                            itemPromoData?.title.toEmptyStringIfNull(),
                                            itemPromoData?.description.toEmptyStringIfNull())
                                }
                                else -> {
                                    promoCode = ""
                                    setupPromoTicker(TickerCheckoutView.State.EMPTY,
                                            "",
                                            "")
                                }
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
        setupPayNowPromoTicker(hotelCart, appliedVoucher)
        setupInvoiceSummary(hotelCart.cart, hotelCart.property)
        setupImportantNotes(hotelCart.property)

        booking_button.setOnClickListener { onBookingButtonClicked() }
    }

    fun initGuestInfoEditText() {
        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, com.tokopedia.common.travel.R.layout.layout_travel_autocompletetv,
                    arrayListOf(), object: TravelContactArrayAdapter.ContactArrayListener {
                override fun getFilterText(): String {
                    return til_guest.editText.text.toString()
                }
            })
            (til_guest.editText as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)
        }
    }

    private fun initProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage(getString(R.string.hotel_progress_dialog_title))
        progressDialog.setCancelable(false)
    }

    private fun showLoadingBar() {
        hotel_booking_container.visibility = View.GONE
        hotel_booking_loading_bar.visibility = View.VISIBLE
    }

    private fun hideLoadingBar() {
        hotel_booking_container.visibility = View.VISIBLE
        hotel_booking_loading_bar.visibility = View.GONE
    }

    private fun setupHotelInfo(property: HotelPropertyData) {
        tv_hotel_info_name.text = property.name
        tv_hotel_info_property_type.text = property.type
        for (i in 1..property.star) {
            context?.run { hotel_info_rating_container.addView(RatingStarView(this)) }
        }
        tv_hotel_info_address.text = property.address
        iv_hotel_info_image.loadImage(property.image.urlMax300, R.drawable.ic_failed_load_image)
    }

    private fun setupRoomDuration(property: HotelPropertyData, cart: HotelCartData) {
        booking_room_duration_info.setRoomDates(cart.checkIn, cart.checkOut)
        booking_room_duration_info.setRoomCheckTimes(property.checkInFrom, property.checkOutTo)
    }

    private fun setupRoomInfo(property: HotelPropertyData, cart: HotelCartData) {
        if (property.rooms.isNotEmpty()) {
            tv_booking_room_info_title.text = property.rooms[0].roomName

            if (!property.isDirectPayment) {
                tv_booking_room_info_pay_at_hotel.visibility = View.VISIBLE
                tv_booking_room_info_pay_at_hotel.setDrawableLeft(R.drawable.ic_hotel_16)
                var payAtHotelString = if (property.rooms.first().isCCRequired) SpannableString(getString(R.string.hotel_booking_pay_at_hotel_cc_required_label))
                else SpannableString(getString(R.string.hotel_booking_pay_at_hotel_label))
                payAtHotelString.setSpan(StyleSpan(Typeface.BOLD), 1, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_booking_room_info_pay_at_hotel.text = payAtHotelString
            }

            tv_booking_room_info_occupancy.text = cart.roomContent
            if (!property.rooms[0].isBreakFastIncluded) tv_booking_room_info_breakfast.visibility = View.GONE

            val cancellationPolicy = property.rooms[0].cancellationPolicies
            if (cancellationPolicy.title.isEmpty()) { // Hide cancellation info ticker
                cancellation_policy_ticker.visibility = View.GONE
            } else {
                cancellation_policy_ticker.tickerTitle = cancellationPolicy.title
                if (cancellationPolicy.isClickable) {
                    cancellation_policy_ticker.setHtmlDescription(getString(R.string.hotel_booking_cancellation_ticker, cancellationPolicy.content))
                    cancellation_policy_ticker.setOnClickListener { onCancellationPolicyClicked(property) }
                } else {
                    cancellation_policy_ticker.setTextDescription(cancellationPolicy.content)
                }
            }
        }
    }

    private fun onCancellationPolicyClicked(property: HotelPropertyData) {
        if (property.rooms.isNotEmpty()) {
            val hotelCancellationPolicyBottomSheets = HotelBookingBottomSheets()
            hotelCancellationPolicyBottomSheets.title = getString(R.string.hotel_booking_cancellation_policy_title)

            for (policy in property.rooms[0].cancellationPolicies.details) {
                context?.run {
                    val policyView = InfoTextView(this)
                    policyView.setTitleAndDescription(policy.longTitle, policy.longDesc)
                    policyView.info_title.setFontSize(TextViewCompat.FontSize.SMALL)
                    policyView.info_container.setMargin(0, 0, 0, policyView.info_container.getDimens(com.tokopedia.design.R.dimen.dp_16))
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
            tv_room_request_input.setText(hotelBookingPageModel.roomRequest)
        } else {
            add_request_container.setOnClickListener {
                showRequestForm()
            }
        }

        til_room_request.setLabel(getString(R.string.hotel_booking_request_form_title))
        til_room_request.setErrorTextAppearance(R.style.ErrorTextAppearance)
        til_room_request.counterMaxLength = roomRequestMaxCharCount
        tv_room_request_input.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                when (s.length > roomRequestMaxCharCount) {
                    true -> til_room_request.error = getString(R.string.hotel_booking_request_char_count_error, roomRequestMaxCharCount)
                    false -> til_room_request.error = null
                }

            }
        })
    }

    private fun showRequestForm() {
        add_request_container.visibility = View.GONE
        room_request_form_container.visibility = View.VISIBLE
    }

    private fun setupContactDetail(cart: HotelCartData) {
        // Check if contact data is empty
        if (hotelBookingPageModel.contactData.isEmpty()) {
            val initContactData = cart.contact
            hotelBookingPageModel.contactData = TravelContactData(
                    name = initContactData.name,
                    email = initContactData.email,
                    phoneCode = initContactData.phoneCode,
                    phone = initContactData.phone
            )
        }
        renderContactData()

        widget_traveller_info.setListener(object : TravellerInfoWidget.TravellerInfoWidgetListener {
            override fun onClickEdit() {
                context?.run {
                    startActivityForResult(TravelContactDataActivity.getCallingIntent(this, hotelBookingPageModel.contactData,
                            TravelContactDataActivity.HOTEL),
                            REQUEST_CODE_CONTACT_DATA)
                }
            }

        })

        if (hotelBookingPageModel.guestName.isNotEmpty() && hotelBookingPageModel.isForOtherGuest == 1) {
            radio_button_contact_guest.isChecked = true
            tv_guest_input.setText(hotelBookingPageModel.guestName)
            toggleShowGuestForm(true)
        }
        radio_group_contact.setOnCheckedChangeListener { _, checkedId ->
            if (radio_button_contact_guest.id == checkedId) {
                toggleShowGuestForm(true)
                hotelBookingPageModel.isForOtherGuest = 1
            } else {
                toggleShowGuestForm(false)
                hotelBookingPageModel.isForOtherGuest = 0
            }
        }

        til_guest.setLabel(getString(R.string.hotel_booking_guest_form_title))
        til_guest.setErrorTextAppearance(R.style.ErrorTextAppearance)
        til_guest.setHelperTextAppearance(R.style.HelperTextAppearance)
        toggleGuestFormError(false)
    }

    private fun renderContactData() {
        val contactData = hotelBookingPageModel.contactData
        widget_traveller_info.setContactName(contactData.name)
        widget_traveller_info.setContactEmail(contactData.email)
        widget_traveller_info.setContactPhoneNum(getString(R.string.hotel_booking_contact_detail_phone_number,
                contactData.phoneCode, contactData.phone))
        widget_traveller_info.invalidate()
    }

    private fun toggleShowGuestForm(value: Boolean) {
        when (value) {
            true -> guest_form_container.visibility = View.VISIBLE
            false -> guest_form_container.visibility = View.GONE
        }
    }

    private fun toggleGuestFormError(value: Boolean) {
        val noticeString = getString(R.string.hotel_booking_guest_form_notice)
        when (value) {
            true -> {
                til_guest.setHelper(null)
                til_guest.error = noticeString
            }
            false -> {
                til_guest.setHelper(noticeString)
                til_guest.error = null
            }
        }
    }

    private fun setupPayNowPromoTicker(cart: HotelCart,
                                       appliedVoucher: HotelCart.AppliedVoucher) {
        if (cart.property.rooms.isNotEmpty() && cart.property.isDirectPayment) {
            booking_pay_now_promo_container.visibility = View.VISIBLE

            promoCode = appliedVoucher.code
            if (promoCode.isNotEmpty()) {
                setupPromoTicker(TickerCheckoutView.State.ACTIVE,
                        appliedVoucher.titleDescription,
                        appliedVoucher.message)
            } else {
                setupPromoTicker(TickerCheckoutView.State.EMPTY,
                        "",
                        "")
            }

            booking_pay_now_promo_ticker.actionListener = object : TickerPromoStackingCheckoutView.ActionListener {
                override fun onClickUsePromo() {
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_LIST_HOTEL)
                    intent.putExtra(COUPON_EXTRA_COUPON_ACTIVE, appliedVoucher.isCoupon)
                    intent.putExtra(COUPON_EXTRA_CART_ID, hotelCart.cartID)
                    startActivityForResult(intent, COUPON_EXTRA_LIST_ACTIVITY_RESULT)
                }

                override fun onResetPromoDiscount() {
                    setupPromoTicker(TickerCheckoutView.State.EMPTY, "", "")
                    promoCode = ""
                }

                override fun onClickDetailPromo() {
                    val intent = RouteManager.getIntent(activity, ApplinkConstInternalPromo.PROMO_DETAIL_HOTEL)
                    intent.putExtra(COUPON_EXTRA_COUPON_CODE, promoCode)
                    intent.putExtra(COUPON_EXTRA_CART_ID, hotelCart.cartID)
                    intent.putExtra(COUPON_EXTRA_IS_USE, true)
                    startActivityForResult(intent, COUPON_EXTRA_DETAIL_ACTIVITY_RESULT)
                }

                override fun onDisablePromoDiscount() {
                    Toast.makeText(context, "onDisablePromoDiscount", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupPromoTicker(state: TickerCheckoutView.State,
                                 title: String,
                                 description: String) {

        if (state == TickerCheckoutView.State.EMPTY) {
            booking_pay_now_promo_ticker.title = title
            booking_pay_now_promo_ticker.state = TickerPromoStackingCheckoutView.State.EMPTY
        } else if (state == TickerCheckoutView.State.ACTIVE) {
            booking_pay_now_promo_ticker.title = title
            booking_pay_now_promo_ticker.desc = description
            booking_pay_now_promo_ticker.state = TickerPromoStackingCheckoutView.State.ACTIVE
        }
    }


    private fun setupInvoiceSummary(cart: HotelCartData, property: HotelPropertyData) {
        cart.fares.find { it.type == "base_price" }?.let {
            tv_room_price_label.text = it.description
            tv_room_price.text = if (cart.localCurrency.isEmpty()) it.price else it.localPrice
        }
        cart.fares.find { it.type == "tax" }?.let {
            tv_room_tax_label.text = it.description
            tv_room_tax.text = if (cart.localCurrency.isEmpty()) it.price else it.localPrice
        }

        val priceLabelResId = if (!property.isDirectPayment) R.string.hotel_booking_invoice_estimate_pay_at_hotel else R.string.hotel_booking_invoice_estimate_pay_now
        val price: String
        if (cart.localCurrency.isEmpty()) {
            price = cart.totalPrice
        } else {
            price = cart.localTotalPrice

            // Show price estimate in IDR
            tv_invoice_foreign_currency.visibility = View.VISIBLE
            val spannableString = SpannableString(getString(R.string.hotel_booking_invoice_foreign_currency_label, cart.totalPrice))
            spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - cart.totalPrice.length, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_invoice_foreign_currency.text = spannableString
        }
        tv_room_estimated_price_label.text = getString(priceLabelResId)
        tv_room_estimated_price.text = price
        context?.run { tv_room_estimated_price.setTextColor(ContextCompat.getColor(this, com.tokopedia.design.R.color.orange_607)) }
    }

    private fun setupImportantNotes(property: HotelPropertyData) {
        if (property.rooms.isNotEmpty() && property.paymentNote.isNotEmpty()) {
            hotel_booking_important_notes.visibility = View.VISIBLE

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
                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.design.R.color.green_200)),
                        spannableString.length - expandNotesLabel.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            tv_booking_important_notes.text = spannableString
            tv_booking_important_notes.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onImportantNotesClicked(notes: String) {
        context?.run {
            val importantNotesBottomSheets = HotelBookingBottomSheets()
            val textView = TextViewCompat(this)
            textView.text = notes
            importantNotesBottomSheets.title = getString(R.string.hotel_important_info_title)
            importantNotesBottomSheets.addContentView(textView)
            activity?.let {
                importantNotesBottomSheets.show(it.supportFragmentManager, TAG_HOTEL_IMPORTANT_NOTES)
            }
        }
    }

    private fun onBookingButtonClicked() {
        progressDialog.show()
        if (validateData()) {
            if (radio_button_contact_guest.isChecked && tv_guest_input.text.toString().isNotEmpty())
                hotelBookingPageModel.guestName = tv_guest_input.text.toString()
            else hotelBookingPageModel.guestName = hotelBookingPageModel.contactData.name
            hotelBookingPageModel.roomRequest = tv_room_request_input.text.toString()
            trackingHotelUtil.hotelClickNext(hotelCart, destinationType, destinationName,
                    hotelBookingPageModel.isForOtherGuest == 0)

            hotelBookingPageModel.promoCode = promoCode

            val hotelCheckoutParam = HotelCheckoutParam(
                    cartId = hotelBookingPageModel.cartId,
                    contact = mapToCheckoutContact(hotelBookingPageModel.contactData),
                    guestName = hotelBookingPageModel.guestName,
                    promoCode = hotelBookingPageModel.promoCode,
                    specialRequest = hotelBookingPageModel.roomRequest
            )
            bookingViewModel.checkoutCart(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_checkout), hotelCheckoutParam)
        } else {
            progressDialog.dismiss()
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        if ((tv_room_request_input.text?.length ?: 0) > roomRequestMaxCharCount) isValid = false
        if (radio_button_contact_guest.isChecked && tv_guest_input.text.isEmpty()) {
            toggleGuestFormError(true)
            isValid = false
        } else if (tv_guest_input.text.isNotEmpty() && !validateNameIsAlphabetOnly(tv_guest_input.text.toString())) {
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
        bookingViewModel.getCartData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_cart), hotelBookingPageModel.cartId)
    }

    companion object {
        const val ARG_CART_ID = "arg_cart_id"
        const val ARG_DESTINATION_TYPE = "arg_destination_type"
        const val ARG_DESTINATION_NAME = "arg_destination_name"
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
        const val COUPON_EXTRA_COUPON_CODE = "EXTRA_KUPON_CODE"
        const val COUPON_EXTRA_IS_USE = "EXTRA_IS_USE"
        const val COUPON_EXTRA_LIST_ACTIVITY_RESULT = 3121
        const val COUPON_EXTRA_DETAIL_ACTIVITY_RESULT = 3122
        const val COUPON_EXTRA_PROMO_DATA = "EXTRA_PROMO_DATA"

        private const val REGEX_IS_ALPHANUMERIC_ONLY = "^[a-zA-Z\\s]*$"


        fun getInstance(cartId: String, destinationType: String, destinationName: String): HotelBookingFragment =
                HotelBookingFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CART_ID, cartId)
                        putString(ARG_DESTINATION_TYPE, destinationType)
                        putString(ARG_DESTINATION_NAME, destinationName)
                    }
                }
    }
}