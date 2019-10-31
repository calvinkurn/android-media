package com.tokopedia.common.travel.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.di.CommonTravelComponent
import com.tokopedia.common.travel.presentation.fragment.TravelContactDataFragment
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.utils.CommonTravelUtils

class TravelContactDataActivity: BaseSimpleActivity(), HasComponent<CommonTravelComponent> {

    override fun getNewFragment(): Fragment =
            TravelContactDataFragment.getInstance(
                    intent.getParcelableExtra(EXTRA_INITIAL_CONTACT_DATA),
                    intent.getStringExtra(EXTRA_TRAVEL_PRODUCT)
            )

    override fun getComponent(): CommonTravelComponent {
        return CommonTravelUtils.getTrainComponent(application)
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