package com.tokopedia.withdraw.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author by StevenFredian on 30/07/18.
 */


@WithdrawScope
@Component(modules = WithdrawModule.class, dependencies = BaseAppComponent.class)
public interface WithdrawComponent {

    @ApplicationContext
    Context getApplicationContext();

    Retrofit.Builder retrofitBuilder();

    @WithdrawQualifier
    OkHttpClient provideOkHttpClient();

}
