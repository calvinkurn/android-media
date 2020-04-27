package com.tokopedia.hotel.globalsearch.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import com.tokopedia.hotel.globalsearch.presentation.fragment.HotelGlobalSearchFragment

class HotelGlobalSearchActivity : HotelBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTitle(intent.getStringExtra(EXTRA_TITLE))
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment? =
            HotelGlobalSearchFragment.getInstance(
                    intent.getStringExtra(EXTRA_CHECK_IN_DATE),
                    intent.getStringExtra(EXTRA_CHECK_OUT_DATE),
                    intent.getIntExtra(EXTRA_NUM_OF_GUESTS, 0),
                    intent.getIntExtra(EXTRA_NUM_OF_ROOMS, 0))

    companion object {

        const val EXTRA_CHECK_IN_DATE = "EXTRA_CHECK_IN_DATE"
        const val EXTRA_CHECK_OUT_DATE = "EXTRA_CHECK_OUT_DATE"
        const val EXTRA_NUM_OF_GUESTS = "EXTRA_NUM_OF_GUESTS"
        const val EXTRA_NUM_OF_ROOMS = "EXTRA_NUM_OF_ROOMS"
        const val EXTRA_TITLE = "EXTRA_TITLE"

        const val CHECK_IN_DATE = "CHECK_IN_DATE"
        const val CHECK_OUT_DATE = "CHECK_OUT_DATE"
        const val NUM_OF_GUESTS = "NUM_OF_GUESTS"
        const val NUM_OF_ROOMS = "NUM_OF_ROOMS"

        fun getIntent(context: Context, checkInDate: String, checkOutDate: String, numOfGuests: Int, numOfRooms: Int, title: String = "") =
                Intent(context, HotelGlobalSearchActivity::class.java)
                        .putExtra(EXTRA_CHECK_IN_DATE, checkInDate)
                        .putExtra(EXTRA_CHECK_OUT_DATE, checkOutDate)
                        .putExtra(EXTRA_NUM_OF_GUESTS, numOfGuests)
                        .putExtra(EXTRA_NUM_OF_ROOMS, numOfRooms)
                        .putExtra(EXTRA_TITLE, title)
    }

}
