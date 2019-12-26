package com.tokopedia.travel.country_code.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.travel.country_code.di.TravelCountryCodeComponent
import com.tokopedia.travel.country_code.di.TravelCountryCodeComponentUtils
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment

/**
 * @author by furqan on 23/12/2019
 */
class PhoneCodePickerActivity : BaseSimpleActivity(), HasComponent<TravelCountryCodeComponent> {
    override fun getNewFragment(): Fragment =
            PhoneCodePickerFragment()

    override fun getComponent(): TravelCountryCodeComponent = TravelCountryCodeComponentUtils.getTravelCountryCodeComponent(application)

    companion object {
        fun getCallingIntent(context: Context): Intent =
                Intent(context, PhoneCodePickerActivity::class.java)
    }
}