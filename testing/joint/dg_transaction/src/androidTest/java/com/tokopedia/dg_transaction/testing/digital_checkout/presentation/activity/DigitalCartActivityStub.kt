package com.tokopedia.dg_transaction.testing.digital_checkout.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common_digital.atc.data.response.DigitalSubscriptionParams
import com.tokopedia.common_digital.cart.view.model.DigitalCheckoutPassData
import com.tokopedia.common_digital.common.constant.DigitalExtraParam
import com.tokopedia.dg_transaction.testing.di.DaggerStubDigitalCheckoutComponent
import com.tokopedia.dg_transaction.testing.di.DaggerStubDigitalCommonComponent
import com.tokopedia.digital_checkout.di.DigitalCheckoutComponent
import com.tokopedia.digital_checkout.presentation.activity.DigitalCartActivity
import com.tokopedia.digital_checkout.presentation.fragment.DigitalCartFragment

class DigitalCartActivityStub: DigitalCartActivity() {

    override fun getComponent(): DigitalCheckoutComponent {
        val digitalCommonComponent = DaggerStubDigitalCommonComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()
        return DaggerStubDigitalCheckoutComponent.builder()
            .stubDigitalCommonComponent(digitalCommonComponent)
            .build()
    }

    override fun getNewFragment(): Fragment {
        val cartPassData: DigitalCheckoutPassData? =
            intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_CART_DATA)
        val subParams: DigitalSubscriptionParams? =
            intent.getParcelableExtra(DigitalExtraParam.EXTRA_PASS_DIGITAL_SUBSCRIPTION_DATA)

        return DigitalCartFragment.newInstance(cartPassData, subParams)
    }
}