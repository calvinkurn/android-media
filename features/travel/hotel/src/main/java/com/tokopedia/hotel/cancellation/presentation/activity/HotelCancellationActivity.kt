package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationFragment
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationReasonFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import kotlinx.android.synthetic.main.activity_hotel_cancellation.*

/**
 * @author by jessica on 27/04/20
 */

class HotelCancellationActivity : HotelBaseActivity(), HasComponent<HotelCancellationComponent> {
    private var invoiceId: String = ""

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelCancellationFragment.getInstance(invoiceId)

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        val uri = intent.data
        if (uri != null) {
            invoiceId = uri.lastPathSegment ?: ""
        }

        super.onCreate(savedInstanceState)
        hotel_cancellation_header.title = getString(R.string.hotel_cancellation_page_title)
    }

    fun updateSubtitle(subtitle: String) {
        hotel_cancellation_header.subtitle = subtitle
    }

    fun showCancellationReasonFragment() {
        supportFragmentManager.beginTransaction().replace(R.id.hotel_cancellation_parent_view,
                HotelCancellationReasonFragment.getInstance(invoiceId), CANCELLATION_REASON_FRAGMENT_TAG)
                .addToBackStack(null).commit()
    }

    override fun getLayoutRes(): Int = R.layout.activity_hotel_cancellation
    override fun getToolbarResourceID(): Int = R.id.hotel_cancellation_header
    override fun getParentViewResourceID(): Int = R.id.hotel_cancellation_parent_view

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, HotelCancellationActivity::class.java)
        const val CANCELLATION_REASON_FRAGMENT_TAG = "CANCELLATION_REASON"
    }
}