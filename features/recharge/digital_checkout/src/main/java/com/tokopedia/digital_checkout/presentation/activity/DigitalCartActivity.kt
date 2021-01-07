package com.tokopedia.digital_checkout.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponentInstance

/**
 * @author by jessica on 07/01/21
 */

class DigitalCartActivity: BaseSimpleActivity(), HasComponent<DigitalCheckoutComponent> {
    override fun getNewFragment(): Fragment? = null
    override fun getComponent(): DigitalCheckoutComponent {
        return DigitalCheckoutComponentInstance.getDigitalCheckoutComponent(application)
    }
}