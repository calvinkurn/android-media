package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.hoteldetail.di.DaggerHotelDetailComponent
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailFragment
import java.util.*

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailActivity : HotelBaseActivity(), HasComponent<HotelDetailComponent> {

    override fun getParentViewResourceID(): Int = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    private var checkInDate: String = ""
    private var checkOutDate: String = ""
    private var propertyId: Int = 0
    private var roomCount: Int = 1
    private var adultCount: Int = 1
    private var destinationType: String = ""
    private var destinationName: String = ""
    private var isDirectPayment: Boolean =  true

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            propertyId = uri.lastPathSegment.toInt()
            if (!uri.getQueryParameter(PARAM_CHECK_IN).isNullOrEmpty()) {
                checkInDate = uri.getQueryParameter(PARAM_CHECK_IN)
                checkOutDate = uri.getQueryParameter(PARAM_CHECK_OUT)
                roomCount = uri.getQueryParameter(PARAM_ROOM_COUNT).toInt()
                adultCount = uri.getQueryParameter(PARAM_ADULT_COUNT).toInt()
            } else if (!uri.getQueryParameter(PARAM_SHOW_ROOM).isNullOrEmpty() && uri.getQueryParameter(PARAM_SHOW_ROOM).toInt() == 1) {
                    val todayWithoutTime = TravelDateUtil.removeTime(TravelDateUtil.getCurrentCalendar().time)
                    val tomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 1)
                    val dayAfterTomorrow = TravelDateUtil.addTimeToSpesificDate(todayWithoutTime, Calendar.DATE, 2)
                    checkInDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, tomorrow)
                    checkOutDate = TravelDateUtil.dateToString(TravelDateUtil.YYYY_MM_DD, dayAfterTomorrow)
            }
        } else {
            with(intent) {
                checkInDate = getStringExtra(EXTRA_CHECK_IN_DATE)
                checkOutDate = getStringExtra(EXTRA_CHECK_OUT_DATE)
                propertyId = getIntExtra(EXTRA_PROPERTY_ID, 0)
                roomCount = getIntExtra(EXTRA_ROOM_COUNT, 1)
                adultCount = getIntExtra(EXTRA_ADULT_COUNT, 1)
                destinationType = getStringExtra(EXTRA_DESTINATION_TYPE)
                destinationName = getStringExtra(EXTRA_DESTINATION_NAME)
                isDirectPayment =  getBooleanExtra(EXTRA_IS_DIRECT_PAYMENT, true)
            }
        }

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun shouldShowOptionMenu(): Boolean = true

    override fun shouldShowMenuWhite(): Boolean = true

    override fun getNewFragment(): Fragment =
            HotelDetailFragment.getInstance(checkInDate, checkOutDate, propertyId, roomCount,
                    adultCount, destinationType, destinationName, isDirectPayment)

    override fun getComponent(): HotelDetailComponent =
            DaggerHotelDetailComponent.builder()
                    .hotelComponent(HotelComponentInstance.getHotelComponent(application))
                    .build()

    override fun getScreenName(): String = ""

    companion object {

        const val EXTRA_PROPERTY_ID = "EXTRA_PROPERTY_ID"
        const val EXTRA_ROOM_COUNT = "EXTRA_ROOM_COUNT"
        const val EXTRA_ADULT_COUNT = "EXTRA_ADULT_COUNT"
        const val EXTRA_CHECK_IN_DATE = "EXTRA_CHECK_IN_DATE"
        const val EXTRA_CHECK_OUT_DATE = "EXTRA_CHECK_OUT_DATE"
        const val EXTRA_DESTINATION_TYPE = "EXTRA_DESTINATION_TYPE"
        const val EXTRA_DESTINATION_NAME = "EXTRA_DESTINATION_NAME"
        const val EXTRA_IS_DIRECT_PAYMENT = "EXTRA_IS_DIRECT_PAYMENT"

        const val PARAM_CHECK_IN = "check_in"
        const val PARAM_CHECK_OUT = "check_out"
        const val PARAM_SHOW_ROOM = "show_room"
        const val PARAM_ROOM_COUNT = "room"
        const val PARAM_ADULT_COUNT = "adult"

        fun getCallingIntent(context: Context, checkInDate: String, checkOutDate: String, propertyId: Int, roomCount: Int,
                             adultCount: Int, destinationType: String, destinationName: String, isDirectPayment: Boolean = true): Intent =
                Intent(context, HotelDetailActivity::class.java)
                        .putExtra(EXTRA_CHECK_IN_DATE, checkInDate)
                        .putExtra(EXTRA_CHECK_OUT_DATE, checkOutDate)
                        .putExtra(EXTRA_PROPERTY_ID, propertyId)
                        .putExtra(EXTRA_ROOM_COUNT, roomCount)
                        .putExtra(EXTRA_ADULT_COUNT, adultCount)
                        .putExtra(EXTRA_DESTINATION_TYPE, destinationType)
                        .putExtra(EXTRA_DESTINATION_NAME, destinationName)
                        .putExtra(EXTRA_IS_DIRECT_PAYMENT, isDirectPayment)

    }
}
