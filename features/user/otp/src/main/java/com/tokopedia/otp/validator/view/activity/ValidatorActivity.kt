package com.tokopedia.otp.validator.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.otp.validator.di.DaggerValidatorComponent
import com.tokopedia.otp.validator.di.ValidatorComponent
import com.tokopedia.otp.validator.view.fragment.ValidatorFragment

/**
 * Created by Ade Fulki on 2019-10-20.
 * ade.hadian@tokopedia.com
 */

class ValidatorActivity: BaseSimpleActivity(), HasComponent<ValidatorComponent> {

    override fun getComponent(): ValidatorComponent {
        return DaggerValidatorComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ValidatorFragment.createInstance(bundle)
    }
}