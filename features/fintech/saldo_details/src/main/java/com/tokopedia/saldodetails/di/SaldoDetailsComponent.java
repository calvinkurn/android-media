package com.tokopedia.saldodetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.saldodetails.view.activity.SaldoDepositActivity;
import com.tokopedia.saldodetails.view.fragment.MerchantCreditDetailFragment;
import com.tokopedia.saldodetails.view.fragment.MerchantSaldoPriorityFragment;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;
import com.tokopedia.saldodetails.view.fragment.SaldoHoldInfoFragment;
import com.tokopedia.saldodetails.view.fragment.SaldoTransactionHistoryFragment;

import dagger.Component;
@SaldoDetailsScope
@Component(modules = SaldoDetailsModule.class, dependencies = BaseAppComponent.class)
public interface SaldoDetailsComponent {

    @ApplicationContext
    Context context();

    void inject(MerchantSaldoPriorityFragment fragment);

    void inject(SaldoDepositFragment fragment);

    void inject(MerchantCreditDetailFragment fragment);

    void inject(SaldoDepositActivity saldoDepositActivity);

    void inject(SaldoTransactionHistoryFragment saldoTransactionHistoryFragment);

    void inject(SaldoHoldInfoFragment saldoHoldInfoFragment);
}
