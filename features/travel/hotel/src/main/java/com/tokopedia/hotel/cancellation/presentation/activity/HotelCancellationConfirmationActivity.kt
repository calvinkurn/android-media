package com.tokopedia.hotel.cancellation.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.hotel.HotelComponentInstance
import com.tokopedia.hotel.R
import com.tokopedia.hotel.cancellation.data.HotelCancellationSubmitModel
import com.tokopedia.hotel.cancellation.di.DaggerHotelCancellationComponent
import com.tokopedia.hotel.cancellation.di.HotelCancellationComponent
import com.tokopedia.hotel.cancellation.presentation.fragment.HotelCancellationConfirmationFragment
import com.tokopedia.hotel.common.presentation.HotelBaseActivity
import kotlinx.android.synthetic.main.activity_hotel_cancellation_confirmation.*
import kotlinx.android.synthetic.main.fragment_hotel_cancellation_confirmation.*

/**
 * @author by jessica on 08/05/20
 */

class HotelCancellationConfirmationActivity: HotelBaseActivity(), HasComponent<HotelCancellationComponent> {

    lateinit var cancellationSubmitModel: HotelCancellationSubmitModel

    override fun onCreate(savedInstanceState: Bundle?) {
        with(intent) {
            cancellationSubmitModel = getParcelableExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA) ?: HotelCancellationSubmitModel()
        }
        super.onCreate(savedInstanceState)

        hotel_cancellation_confirmation_header.setTitle(if (cancellationSubmitModel.success) R.string.hotel_cancellation_success else R.string.hotel_cancellation_failed)
        hotel_cancellation_confirmation_header.isShowBackButton = false
    }

    override fun shouldShowOptionMenu(): Boolean = false

    override fun getNewFragment(): Fragment = HotelCancellationConfirmationFragment.getInstance(cancellationSubmitModel)

    override fun getComponent(): HotelCancellationComponent = DaggerHotelCancellationComponent.builder()
            .hotelComponent(HotelComponentInstance.getHotelComponent(application))
            .build()

    override fun getLayoutRes(): Int = R.layout.activity_hotel_cancellation_confirmation
    override fun getToolbarResourceID(): Int = R.id.hotel_cancellation_confirmation_header
    override fun getParentViewResourceID(): Int = R.id.hotel_cancellation_confirmation_parent_view

    companion object {
        fun getCallingIntent(context: Context, hotelCancellationSubmitModel: HotelCancellationSubmitModel): Intent =
                Intent(context, HotelCancellationConfirmationActivity::class.java)
                        .putExtra(EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA, hotelCancellationSubmitModel)
        const val EXTRA_HOTEL_CANCELLATION_SUBMIT_DATA = "extra_cancellation_submit_data"
    }
}