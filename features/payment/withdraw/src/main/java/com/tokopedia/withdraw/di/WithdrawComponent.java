package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.withdraw.view.activity.WithdrawActivity;
import com.tokopedia.withdraw.view.fragment.SuccessFragmentWithdrawal;
import com.tokopedia.withdraw.view.fragment.WithdrawFragment;

import dagger.Component;

/**
 * @author by StevenFredian on 30/07/18.
 */


@WithdrawScope
@Component(modules = WithdrawModule.class, dependencies = BaseAppComponent.class)
public interface WithdrawComponent {

    @ApplicationContext
    Context getApplicationContext();

    void inject(WithdrawFragment fragment);

    void inject(WithdrawActivity withdrawActivity);

    void inject(SuccessFragmentWithdrawal successFragmentWithdrawal);
}
