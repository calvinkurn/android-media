package com.tokopedia.loginphone.choosetokocashaccount.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 10/22/18.
 */
@Module
public class ChooseAccountModule {

    @ChooseAccountScope
    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

}
