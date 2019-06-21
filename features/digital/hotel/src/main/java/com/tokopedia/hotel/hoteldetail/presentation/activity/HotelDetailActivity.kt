package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.hoteldetail.di.DaggerHotelDetailComponent
import com.tokopedia.hotel.hoteldetail.di.HotelDetailComponent
import com.tokopedia.hotel.hoteldetail.presentation.fragment.HotelDetailFragment

/**
 * @author by furqan on 22/04/19
 */
class HotelDetailActivity : HotelBaseActivity(), HasComponent<HotelDetailComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }

    override fun shouldShowOptionMenu(): Boolean = true

    override fun getNewFragment(): Fragment =
            with(intent) {
                HotelDetailFragment.getInstance(
                        getStringExtra(EXTRA_CHECK_IN_DATE),
                        getStringExtra(EXTRA_CHECK_OUT_DATE),
                        getIntExtra(EXTRA_PROPERTY_ID, 0),
                        getIntExtra(EXTRA_ROOM_COUNT, 1),
                        getIntExtra(EXTRA_ADULT_COUNT, 1),
                        getBooleanExtra(EXTRA_ENABLE_BUTTON, true))
            }

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
        const val EXTRA_ENABLE_BUTTON = "EXTRA_ENABLE_BUTTON"

        fun getCallingIntent(context: Context, checkInDate: String, checkOutDate: String, propertyId: Int, roomCount: Int,
                             adultCount: Int, enableButton: Boolean = true): Intent =
                Intent(context, HotelDetailActivity::class.java)
                        .putExtra(EXTRA_CHECK_IN_DATE, checkInDate)
                        .putExtra(EXTRA_CHECK_OUT_DATE, checkOutDate)
                        .putExtra(EXTRA_PROPERTY_ID, propertyId)
                        .putExtra(EXTRA_ROOM_COUNT, roomCount)
                        .putExtra(EXTRA_ADULT_COUNT, adultCount)
                        .putExtra(EXTRA_ENABLE_BUTTON, enableButton)

    }
}
/*

*/
/**
 * eg : tokopedia://hotel/detail/{id}?check_in={checkin_date}&check_out={checkout_date}&room={#}&adult={#}&enable_book={0/1}
 *
 * check_in = check in date
 * check_out = check out date
 * room = number of room
 * adult = number of adult
 * enable_book = enable book 1 (true) user can see room list etc. enable book 0 (false) user can't see room list and button book room will not shown
 *//*

@DeepLink(ApplinkConstant.HOTEL_DETAIL)
fun getCallingIntent(context: Context, extras: Bundle): Intent {
    val uri: Uri.Builder = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
    return HotelDetailActivity.getCallingIntent(context,
            extras.getString("check_in", ""),
            extras.getString("check_out", ""),
            extras.getInt("id"),
            extras.getInt("room", 1),
            extras.getInt("adult", 1),extras.getInt("enable_book") == 1)
            .setData(uri.build())
}*/
