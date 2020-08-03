package com.tokopedia.oneclickcheckout.preference.edit.view.payment.creditcard

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.DaggerPreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditModule

class CreditCardPickerActivity: BaseSimpleActivity(), HasComponent<PreferenceEditComponent> {

    override fun getNewFragment(): Fragment? {
        return CreditCardPickerFragment()
    }

    override fun getComponent(): PreferenceEditComponent {
        return DaggerPreferenceEditComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .preferenceEditModule(PreferenceEditModule(this))
                .build()
    }
}