package com.tokopedia.travel.passenger.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.travel.passenger.di.TravelPassengerComponent
import com.tokopedia.travel.passenger.di.TravelPassengerComponentUtils
import com.tokopedia.travel.passenger.presentation.fragment.TravelContactDataFragment
import com.tokopedia.travel.passenger.presentation.model.TravelContactData

class TravelContactDataActivity : BaseSimpleActivity(), HasComponent<TravelPassengerComponent> {

    override fun getNewFragment(): Fragment =
            TravelContactDataFragment.getInstance(
                    intent.getParcelableExtra(EXTRA_INITIAL_CONTACT_DATA),
                    intent.getStringExtra(EXTRA_TRAVEL_PRODUCT)
            )

    override fun getComponent(): TravelPassengerComponent {
        return TravelPassengerComponentUtils.getTravelPassengerComponent(application)
    }


    companion object {

        const val FLIGHT = "flight"
        const val HOTEL = "hotel"
        const val EXTRA_TRAVEL_PRODUCT = "EXTRA_TRAVEL_PRODUCT"
        const val EXTRA_INITIAL_CONTACT_DATA = "EXTRA_INITIAL_CONTACT_DATA"

        fun getCallingIntent(context: Context, contactData: TravelContactData,
                             travelProduct: String = ""): Intent =
                Intent(context, TravelContactDataActivity::class.java)
                        .putExtra(EXTRA_INITIAL_CONTACT_DATA, contactData)
                        .putExtra(EXTRA_TRAVEL_PRODUCT, travelProduct)
    }
}