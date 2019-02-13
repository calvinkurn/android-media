package com.tokopedia.saldodetails.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.saldodetails.activity.SaldoDepositActivity;
import com.tokopedia.saldodetails.view.fragment.MerchantSaldoPriorityFragment;
import com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment;

import dagger.Component;
@SaldoDetailsScope
@Component(modules = SaldoDetailsModule.class, dependencies = BaseAppComponent.class)
public interface SaldoDetailsComponent {

    @ApplicationContext
    Context context();

    void inject(MerchantSaldoPriorityFragment fragment);

    void inject(SaldoDepositFragment fragment);

    void inject(SaldoDepositActivity saldoDepositActivity);
}
