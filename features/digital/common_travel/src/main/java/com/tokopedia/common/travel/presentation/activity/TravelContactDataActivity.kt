package com.tokopedia.common.travel.presentation.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.di.CommonTravelComponent
import com.tokopedia.common.travel.presentation.fragment.TravelContactDataFragment
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.common.travel.utils.CommonTravelUtils

class TravelContactDataActivity: BaseSimpleActivity(), HasComponent<CommonTravelComponent> {

    override fun getNewFragment(): Fragment =
            TravelContactDataFragment.getInstance(
                    intent.getParcelableExtra(EXTRA_INITIAL_CONTACT_DATA)
            )

    override fun getComponent(): CommonTravelComponent =
            CommonTravelUtils.getTrainComponent(application)

    companion object {

        const val EXTRA_INITIAL_CONTACT_DATA = "EXTRA_INITIAL_CONTACT_DATA"

        fun getCallingIntent(context: Context, contactData: TravelContactData): Intent =
                Intent(context, TravelContactDataActivity::class.java)
                        .putExtra(EXTRA_INITIAL_CONTACT_DATA, contactData)
    }
}