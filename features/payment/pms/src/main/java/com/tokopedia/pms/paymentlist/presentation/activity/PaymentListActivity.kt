package com.tokopedia.pms.paymentlist.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.paymentlist.di.DaggerPaymentListComponent
import com.tokopedia.pms.paymentlist.di.PaymentListComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import com.tokopedia.pms.paymentlist.presentation.listeners.PaymentListActionListener

class PaymentListActivity : BaseSimpleActivity(), HasComponent<PaymentListComponent>, PaymentListActionListener {

    private lateinit var component: PaymentListComponent

    override fun getNewFragment(): Fragment {
        return DeferredPaymentListFragment.createInstance()
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

    override fun cancelSingleTransaction(transactionId: String, merchantCode: String) {
        (fragment as DeferredPaymentListFragment).invokeCancelSingleTransaction(transactionId, merchantCode)
    }

    override fun cancelCombinedTransaction(model: BasePaymentModel) {
        (fragment as DeferredPaymentListFragment).showCombinedTransactionDetail(model)
    }
}