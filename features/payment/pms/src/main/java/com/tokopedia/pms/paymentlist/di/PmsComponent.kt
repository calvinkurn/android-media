package com.tokopedia.pms.paymentlist.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.pms.bankaccount.view.bottomsheet.BankDestinationBottomSheet
import com.tokopedia.pms.bankaccount.view.fragment.ChangeBankAccountFragment
import com.tokopedia.pms.clickbca.view.ChangeClickBcaFragment
import com.tokopedia.pms.paymentlist.di.module.PmsModule
import com.tokopedia.pms.paymentlist.di.module.ViewModelModule
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import dagger.Component

@PmsScope
@Component(
    modules = [PmsModule::class, ViewModelModule::class],
    dependencies = [BaseAppComponent::class]
)
interface PmsComponent {
    fun inject(deferredPaymentListFragment: DeferredPaymentListFragment)
    fun inject(deferredPaymentListFragment: ChangeClickBcaFragment)
    fun inject(deferredPaymentListFragment: ChangeBankAccountFragment)
    fun inject(deferredPaymentListBottomSheet: BankDestinationBottomSheet)
}