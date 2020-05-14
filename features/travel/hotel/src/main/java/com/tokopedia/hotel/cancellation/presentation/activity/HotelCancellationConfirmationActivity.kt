package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitParam
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationConfirmationFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import kotlinx.android.synthetic.main.activity_hotel_cancellation_confirmation.*

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationActivity: HotelBaseActivity(), HasComponent<HotelCancellationComponent> {

    lateinit var cancellationSubmitParam: HotelCancellationSubmitParam

    override fun onCreate(savedInstanceState: Bundle?) {
        with(intent) {
            cancellationSubmitParam = getParcelableExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA) ?: HotelCancellationSubmitParam()
        }
        super.onCreate(savedInstanceState)

        hotel_cancellation_confirmation_header.isShowBackButton = false
    }

    fun setPageTitle(title: String) {
        hotel_cancellation_confirmation_header.title = title
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelCancellationConfirmationFragment.getInstance(cancellationSubmitParam)

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_cancellation_confirmation
    override fun getToolbarResourceID(): Int = R.id.hotel_cancellation_confirmation_header
    override fun getParentViewResourceID(): Int = R.id.hotel_cancellation_confirmation_parent_view

    companion object {
        fun getCallingIntent(context: Context, hotelCancellationSubmitParam: HotelCancellationSubmitParam): Intent =
                Intent(context, HotelCancellationConfirmationActivity::class.java)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA, hotelCancellationSubmitParam)
        const val EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA = "extra_cancellation_submit_data"
    }
}