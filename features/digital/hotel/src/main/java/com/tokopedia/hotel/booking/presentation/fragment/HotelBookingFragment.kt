package com.tokopedia.hotel.booking.presentation.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.common.presentation.widget.RatingStarView
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_hotel_booking.*
import kotlinx.android.synthetic.main.widget_hotel_room_duration.view.*
import javax.inject.Inject

class HotelBookingFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    lateinit var hotelCart: HotelCart
    lateinit var cartId: String

    var roomRequest = ""
    var roomRequestMaxCharCount = ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT

    var guestName: String = ""

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
                }
                is Fail -> {}
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_hotel_booking, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookingViewModel.getcartData(GraphqlHelper.loadRawString(resources, R.raw.gql_query_hotel_get_cart),
                cartId, false)

        initView()
    }

    private fun initView() {
        setupHotelInfo()
        setupRoomDuration()
        setupRoomInfo()
        setupRoomRequestForm()
        setupContactDetail()
        setupInvoiceSummary()
        setupBookingButton()
    }

    private fun setupHotelInfo() {
        tv_hotel_info_name.text = hotelCart.property.name
        tv_hotel_info_property_type.text = hotelCart.property.type
        for (i in 1..hotelCart.property.star) {
            hotel_info_rating_container.addView(RatingStarView(context!!))
        }
        tv_hotel_info_address.text = hotelCart.property.address
        iv_hotel_info_image.loadImage(hotelCart.property.image, R.drawable.ic_failed_load_image)
    }

    private fun setupRoomDuration() {
        val checkInDate = hotelCart.cart.checkIn
        val checkOutDate = hotelCart.cart.checkOut
        booking_room_duration_info.hotel_check_in_date.text = checkInDate
        booking_room_duration_info.hotel_check_out_date.text = checkOutDate
        booking_room_duration_info.hotel_room_night_count.text = getString(R.string.hotel_room_night_count,
                HotelUtils.countNightDifference(checkInDate, checkOutDate))
    }

    private fun setupRoomInfo() {
        if (hotelCart.property.rooms.isNotEmpty())
            tv_booking_room_info_title.text = hotelCart.property.rooms[0].roomName
            tv_booking_room_info_pay_at_hotel.visibility = View.VISIBLE
            tv_booking_room_info_pay_at_hotel.setDrawableLeft(R.drawable.ic_hotel)
            hotelCart.cart.fares.find { it.type == "base_price" }?.let {
                tv_booking_room_info_occupancy.text = it.description.replace("x", "•")
            }
            if (!hotelCart.property.rooms[0].isBreakFastIncluded) tv_booking_room_info_breakfast.visibility = View.GONE
    }

    private fun setupRoomRequestForm() {
        if (roomRequest.isNotEmpty()) {
            showRequestForm()
            tv_room_request_input.setText(roomRequest)
            updateRoomRequestCounter(roomRequest.length)
        } else {
            add_request_container.setOnClickListener {
                showRequestForm()
            }
            updateRoomRequestCounter(0)
        }

        tv_room_request_char_count_error.text = getString(R.string.hotel_booking_request_char_count_error, roomRequestMaxCharCount)
        tv_room_request_input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable) {
                val roomRequestCharCount = s.length
                updateRoomRequestCounter(roomRequestCharCount)
                if (roomRequestCharCount > roomRequestMaxCharCount) {
                    toggleRoomRequestError(false)
                } else {
                    toggleRoomRequestError(true)
                }
            }
        })
    }

    private fun showRequestForm() {
        add_request_container.visibility = View.GONE
        room_request_form_container.visibility = View.VISIBLE
    }

    private fun updateRoomRequestCounter(count: Int) {
        tv_room_request_char_count.text = getString(R.string.hotel_booking_request_char_count, count, roomRequestMaxCharCount)
    }

    private fun toggleRoomRequestError(value: Boolean) {
        if (value) {
            if (tv_room_request_char_count_error.visibility == View.INVISIBLE) {
                tv_room_request_char_count.visibility = View.INVISIBLE
                tv_room_request_char_count_error.visibility = View.VISIBLE
                tv_room_request_input.background.mutate().setColorFilter(ContextCompat.getColor(context!!, R.color.pink_property), PorterDuff.Mode.SRC_ATOP)
            }
        } else {
            if (tv_room_request_char_count.visibility == View.INVISIBLE) {
                tv_room_request_char_count.visibility = View.VISIBLE
                tv_room_request_char_count_error.visibility = View.INVISIBLE
                tv_room_request_input.background.mutate().colorFilter = null
            }
        }
    }

    private fun setupContactDetail() {
        val contactData = hotelCart.cart.contact
        tv_contact_name.text = contactData.name
        tv_contact_email.text = contactData.email
        tv_contact_phone_number.text = getString(R.string.hotel_booking_contact_detail_phone_number,
                contactData.phoneCode, contactData.phone)

        if (guestName.isNotEmpty()) radio_button_contact_guest.isChecked = true
        if (radio_button_contact_guest.isChecked) {
            guest_form_container.visibility = View.VISIBLE
        } else {
            guest_form_container.visibility = View.GONE
        }
        tv_guest_form_input.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && tv_guest_form_input.text.isNullOrEmpty()) toggleGuestFormError(true)
        }
    }

    private fun toggleGuestFormError(value: Boolean) {
        if (value) {
            tv_room_request_input.background.mutate().setColorFilter(ContextCompat.getColor(context!!, R.color.pink_property), PorterDuff.Mode.SRC_ATOP)
        } else {
            tv_room_request_input.background.mutate().colorFilter = null
        }
    }

    private fun setupInvoiceSummary() {
        var totalPrice = 0.0
        var localTotalPrice = 0.0
        hotelCart.cart.fares.find { it.type == "base_price" }?.let {
            tv_room_price_label.text = it.description
            tv_room_price.text = it.price
            totalPrice += it.priceAmount
            localTotalPrice += it.localPriceAmount
        }
        hotelCart.cart.fares.find { it.type == "tax" }?.let {
            tv_room_tax_label.text = it.description
            tv_room_tax.text = it.price
            totalPrice += it.priceAmount
            localTotalPrice += it.localPriceAmount
        }
        tv_room_estimated_price.text = totalPrice.toString()
        tv_invoice_foreign_currency.text = getString(R.string.hotel_booking_invoice_foreign_currency_label, localTotalPrice.toString())
    }

    private fun setupBookingButton() {
        booking_button.setOnClickListener {
            // Validate request & guest
            if (validateRoomRequest() && validateGuestName()) {
                // Continue to payment
            }
        }
    }

    private fun validateRoomRequest(): Boolean {
        return tv_room_request_input.text.isEmpty() || (
                tv_room_request_input.text.isNotEmpty() && tv_room_request_input.text.length <= roomRequestMaxCharCount)
    }

    private fun validateGuestName(): Boolean {
        return !radio_button_contact_guest.isSelected || (
                radio_button_contact_guest.isSelected && tv_guest_form_input.text.isNotEmpty())
    }

    override fun getScreenName(): String = "Pembayaran"

    override fun initInjector() {
        getComponent(HotelBookingComponent::class.java).inject(this)
    }

    companion object {
        const val ARG_CART_ID = "arg_cart_id"
        const val ROOM_REQUEST_DEFAULT_MAX_CHAR_COUNT = 250

        fun getInstance(cartId: String): HotelBookingFragment =
                HotelBookingFragment().also {
                    it.arguments = Bundle().apply {
                        putString(ARG_CART_ID, cartId)
                    }
                }
    }
}