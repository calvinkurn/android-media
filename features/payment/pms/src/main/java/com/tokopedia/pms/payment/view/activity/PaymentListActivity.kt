package com.tokopedia.pms.payment.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.payment.view.fragment.PaymentListFragmentK
import com.tokopedia.pms.paymentlist.di.DaggerPaymentListComponent
import com.tokopedia.pms.paymentlist.di.PaymentListComponent

class PaymentListActivity : BaseSimpleActivity(), HasComponent<PaymentListComponent> {

    private lateinit var component: PaymentListComponent

    override fun getNewFragment(): Fragment? {
        return PaymentListFragmentK.createInstance()
    }

    override fun getComponent(): PaymentListComponent {
        if (!::component.isInitialized)
            component = DaggerPaymentListComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
        return component
    }
}