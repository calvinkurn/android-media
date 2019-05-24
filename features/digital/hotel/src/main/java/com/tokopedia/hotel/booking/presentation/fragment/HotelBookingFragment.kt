package com.tokopedia.hotel.booking.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelCartData
import com.tokopedia.hotel.booking.data.model.HotelCheckoutParam
import com.tokopedia.hotel.booking.data.model.HotelPropertyData
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.booking.presentation.widget.HotelBookingBottomSheets
import com.tokopedia.hotel.common.presentation.widget.InfoTextView
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_booking.*
import kotlinx.android.synthetic.main.widget_info_text_view.view.*
import javax.inject.Inject


class HotelBookingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    lateinit var hotelCart: HotelCart
    lateinit var cartId: String

    var roomRequest = ""
    var roomRequestMaxCharCount = ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT
    var guestName = ""
    var promoCode = ""

    lateinit var saveInstanceCacheManager: SaveInstanceCacheManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(HotelBookingViewModel::class.java)
        }

        arguments?.let {
            cartId = it.getString(ARG_CART_ID, "")
        }

        saveInstanceCacheManager = SaveInstanceCacheManager(context!!, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.hotelCartResult.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    hotelCart = it.data
                    initView()
                }
                is Fail -> {}
            }
        })

        bookingViewModel.hotelCheckoutResult.observe(this, android.arch.lifecycle.Observer {
            when (it) {
                is Success -> {
                    // Start activity
                }
                is Fail -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_booking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_HOTEL_CART_ID)) {
            cartId = savedInstanceState.getString(EXTRA_HOTEL_CART_ID,"")
            roomRequest = savedInstanceState.getString(EXTRA_ROOM_REQUEST,"")
            guestName = savedInstanceState.getString(EXTRA_GUEST_NAME,"")
        }

        bookingViewModel.getCartData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_cart), cartId,
                GraphqlHelper.loadRawString(resources, R.raw.dummy_hotel_cart))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HOTEL_CART_ID, cartId)
        outState.putString(EXTRA_ROOM_REQUEST, roomRequest)
        outState.putString(EXTRA_GUEST_NAME, guestName)
    }

    private fun initView() {
        setupHotelInfo(hotelCart.property)
        setupRoomDuration(hotelCart.cart)
        setupRoomInfo(hotelCart.property, hotelCart.cart)
        setupRoomRequestForm(hotelCart.cart)
        setupContactDetail(hotelCart.cart)
        setupInvoiceSummary(hotelCart.cart, hotelCart.property)

        booking_button.setOnClickListener { onBookingButtonClicked() }
    }

    private fun setupHotelInfo(property: HotelPropertyData) {
        tv_hotel_info_name.text = property.name
        tv_hotel_info_property_type.text = property.type
        for (i in 1..property.star) {
            hotel_info_rating_container.addView(RatingStarView(context!!))
        }
        tv_hotel_info_address.text = property.address
        iv_hotel_info_image.loadImage(property.image, R.drawable.ic_failed_load_image)
    }

    private fun setupRoomDuration(cart: HotelCartData) {
        booking_room_duration_info.setRoomDates(cart.checkIn, cart.checkOut)
    }

    private fun setupRoomInfo(property: HotelPropertyData, cart: HotelCartData) {
        if (property.rooms.isNotEmpty()) {
            tv_booking_room_info_title.text = property.rooms[0].roomName

            if (property.rooms[0].isDirectPayment) {
                tv_booking_room_info_pay_at_hotel.visibility = View.VISIBLE
                tv_booking_room_info_pay_at_hotel.setDrawableLeft(R.drawable.ic_hotel_16)
            }
            val spannableString = SpannableString(getString(R.string.hotel_booking_pay_at_hotel_label))
            spannableString.setSpan(StyleSpan(Typeface.BOLD), 1, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            cart.fares.find { it.type == "base_price" }?.let {
                tv_booking_room_info_occupancy.text = it.description.replace("x", "•")
            }
            if (!property.rooms[0].isBreakFastIncluded) tv_booking_room_info_breakfast.visibility = View.GONE

            val cancellationPolicy = property.rooms[0].cancellationPolicies
            tv_cancellation_policy_ticker.truncateDescription = false
            tv_cancellation_policy_ticker.resetMaxLineCount()
            tv_cancellation_policy_ticker.info_title.setFontSize(TextViewCompat.FontSize.MICRO)

            var cancellationDesc: CharSequence = cancellationPolicy.content
            if (cancellationPolicy.isClickable) {
                val moreInfoString = getString(R.string.hotel_booking_cancellation_policy_more_info)
                val spannableString = SpannableString("$cancellationDesc $moreInfoString")
                val moreInfoSpan = object : ClickableSpan() {
                    override fun onClick(textView: View) {
                        onCancellationPolicyClicked(property)
                    }
                }
                spannableString.setSpan(moreInfoSpan,spannableString.length - moreInfoString.length, spannableString.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context!!, R.color.green_200)),
                        spannableString.length - moreInfoString.length, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                cancellationDesc = spannableString
            }
            tv_cancellation_policy_ticker.setTitleAndDescription(cancellationPolicy.policyType, cancellationDesc)
            tv_cancellation_policy_ticker.info_desc.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private fun onTaxPolicyClicked() {
        val hotelTaxPolicyBottomSheets = HotelBookingBottomSheets()
        val textView = TextViewCompat(context!!)
        textView.text = "Efektif tanggal 1 September 2017, setiap tamu hotel diluar warga negara Malaysia akan dikenakan pajak turis sebesar MYR 10 net./malam/orang pada hotel-hotel yang terdaftar di Pemerintah malaysia. Penambahan ini akan ditagihkan pada saat check-in. Mohon diperhatikan ketika tiba di hotel dan melakukan check-in. Deposit mungkin diwajibkan pihak hotel dengan menggunakan uang tunai."
        hotelTaxPolicyBottomSheets.addContentView(textView)
        hotelTaxPolicyBottomSheets.show(activity!!.supportFragmentManager, TAG_HOTEL_TAX_POLICY)
    }

    private fun onCancellationPolicyClicked(property: HotelPropertyData) {
        if (property.rooms.isNotEmpty()) {
            val hotelCancellationPolicyBottomSheets = HotelBookingBottomSheets()
            hotelCancellationPolicyBottomSheets.title = getString(R.string.hotel_booking_cancellation_policy_title)

            for (policy in property.rooms[0].cancellationPolicies.details) {
                val policyView = InfoTextView(context!!)
                policyView.setTitleAndDescription(policy.title, policy.content)
                policyView.truncateDescription = false
                policyView.resetMaxLineCount()
                policyView.info_title.setFontSize(TextViewCompat.FontSize.SMALL)
                policyView.info_container.setMargin(0,0,0, policyView.info_container.getDimens(R.dimen.dp_16))
                hotelCancellationPolicyBottomSheets.addContentView(policyView)
            }

            hotelCancellationPolicyBottomSheets.show(activity!!.supportFragmentManager, TAG_HOTEL_CANCELLATION_POLICY)
        }
    }

    private fun setupRoomRequestForm(cart: HotelCartData) {
        if (cart.specialRequest.isNotEmpty()) roomRequest = cart.specialRequest
        if (roomRequest.isNotEmpty()) {
            showRequestForm()
            tv_room_request_input.setText(roomRequest)
        } else {
            add_request_container.setOnClickListener {
                showRequestForm()
            }
        }

        til_room_request.setLabel(getString(R.string.hotel_booking_request_form_title))
        til_room_request.setErrorTextAppearance(R.style.ErrorTextAppearance)
        til_room_request.counterMaxLength = roomRequestMaxCharCount
        til_room_request.editText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                KeyboardHandler.hideSoftKeyboard(activity)
            }
        }
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
        val contactData = cart.contact
        tv_contact_name.text = contactData.name
        tv_contact_email.text = contactData.email
        tv_contact_phone_number.text = getString(R.string.hotel_booking_contact_detail_phone_number,
                contactData.phoneCode, contactData.phone)

        if (guestName.isNotEmpty()) {
            radio_button_contact_guest.isChecked = true
            tv_guest_input.setText(guestName)
            toggleShowGuestForm(true)
        }
        radio_group_contact.setOnCheckedChangeListener { _, checkedId ->
            toggleShowGuestForm(radio_button_contact_guest.id == checkedId)
        }

        til_guest.setLabel(getString(R.string.hotel_booking_guest_form_title))
        til_guest.setErrorTextAppearance(R.style.ErrorTextAppearance)
        til_guest.setHelperTextAppearance(R.style.HelperTextAppearance)
        toggleGuestFormError(false)
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

    private fun setupInvoiceSummary(cart: HotelCartData, property: HotelPropertyData) {
        if (property.rooms.isNotEmpty() && property.rooms[0].importantNote.isNotEmpty()) {
            invoice_tax_deposit_info_container.visibility = View.VISIBLE
            tv_invoice_tax_deposit_info.text = property.rooms[0].importantNote
        }

        cart.fares.find { it.type == "base_price" }?.let {
            tv_room_price_label.text = it.description
            tv_room_price.text = it.localPrice
        }
        cart.fares.find { it.type == "tax" }?.let {
            tv_room_tax_label.text = it.description
            tv_room_tax.text = it.localPrice
        }
        val priceLabelResId = if (property.rooms[0].isDirectPayment) R.string.hotel_booking_invoice_estimate_pay_at_hotel else R.string.hotel_booking_invoice_estimate_pay_now
        tv_room_estimated_price_label.text = getString(priceLabelResId)
        tv_room_estimated_price.text = cart.localTotalPrice
        tv_room_estimated_price.setTextColor(ContextCompat.getColor(context!!, R.color.orange_607))

        if (cart.currency != "IDR") {
            tv_invoice_foreign_currency.visibility = View.VISIBLE
            val spannableString = SpannableString(getString(R.string.hotel_booking_invoice_foreign_currency_label, cart.totalPrice))
            spannableString.setSpan(StyleSpan(Typeface.BOLD), spannableString.length - cart.totalPrice.length, spannableString.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_invoice_foreign_currency.text = spannableString
        }
    }

    private fun onBookingButtonClicked() {
        if (validateData()) {
            val hotelCheckoutParam = HotelCheckoutParam(
                cartId = cartId,
                contact = hotelCart.cart.contact,
                guestName = tv_guest_input.text.toString(),
                promoCode = promoCode,
                specialRequest = roomRequest
            )
//            bookingViewModel.checkoutCart(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_checkout), hotelCheckoutParam)
        }
//        onTaxPolicyClicked()
        onCancellationPolicyClicked(hotelCart.property)
    }

    private fun validateData(): Boolean {
        var isValid = true
        if (tv_room_request_input.text.isNullOrBlank() || tv_room_request_input.text.length <= roomRequestMaxCharCount) isValid = false
        if (!radio_button_contact_guest.isSelected || til_guest.editText.text.isNotEmpty()) {
            toggleGuestFormError(true)
            isValid = false
        }
        return isValid
    }

    override fun getScreenName(): String = "Pembayaran"

    override fun initInjector() {
        getComponent(HotelBookingComponent::class.java).inject(this)
    }

    companion object {
        const val ARG_CART_ID = "arg_cart_id"
        const val EXTRA_HOTEL_CART_ID = "extra_hotel_cart_id"
        const val EXTRA_ROOM_REQUEST = "extra_room_request"
        const val EXTRA_GUEST_NAME = "extra_guest_name"
        const val TAG_HOTEL_CANCELLATION_POLICY = "hotel_cancellation_policy"
        const val TAG_HOTEL_TAX_POLICY = "hotel_tax_policy"
        const val ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT = 250

        fun getInstance(cartId: String): HotelBookingFragment =
                HotelBookingFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CART_ID, cartId)
                    }
                }
    }
}