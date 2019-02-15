package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.withdraw.view.activity.WithdrawActivity;
import com.tokopedia.withdraw.view.activity.WithdrawPasswordActivity;
import com.tokopedia.withdraw.view.fragment.WithdrawFragment;
import com.tokopedia.withdraw.view.fragment.WithdrawPasswordFragment;

import dagger.Component;

/**
 * @author by StevenFredian on 30/07/18.
 */


@WithdrawScope
@Component(modules = WithdrawModule.class, dependencies = BaseAppComponent.class)
public interface WithdrawComponent {

    @ApplicationContext
    Context getApplicationContext();

   /* Retrofit.Builder retrofitBuilder();

    @WithdrawQualifier
    OkHttpClient provideOkHttpClient();*/

    void inject(WithdrawFragment fragment);

    void inject(WithdrawActivity withdrawActivity);

    void inject(WithdrawPasswordFragment withdrawPasswordFragment);

    void inject(WithdrawPasswordActivity withdrawPasswordActivity);
}
