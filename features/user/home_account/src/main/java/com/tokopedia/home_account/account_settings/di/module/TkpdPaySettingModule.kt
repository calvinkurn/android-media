package com.tokopedia.home_account.account_settings.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.home_account.account_settings.di.scope.TkpdPaySettingScope;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Module;
import dagger.Provides;

@Module
public class TkpdPaySettingModule {

    @TkpdPaySettingScope
    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }
}
