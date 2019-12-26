package com.tokopedia.travel.country_code.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EXTRA_TITLE)) {
            updateTitle(intent.getStringExtra(EXTRA_TITLE))
        }
    }

    override fun getNewFragment(): Fragment =
            PhoneCodePickerFragment()

    override fun getComponent(): TravelCountryCodeComponent = TravelCountryCodeComponentUtils.getTravelCountryCodeComponent(application)

    companion object {
        const val EXTRA_TITLE = "EXTRA_TITLE"

        fun getCallingIntent(context: Context): Intent =
                Intent(context, PhoneCodePickerActivity::class.java)

        fun getCallingIntent(context: Context, title: String): Intent =
                Intent(context, PhoneCodePickerActivity::class.java).putExtra(EXTRA_TITLE, title)
    }
}