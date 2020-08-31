package com.tokopedia.core.base.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.gcm.GCMHandler;

import dagger.Module;
import dagger.Provides;

/**
 * @author normansyahputa on 5/16/17.
 */
@Module
public class UtilModule {

    @Provides
    public GCMHandler provideGcmHandler(@ApplicationContext Context context){
        return new GCMHandler(context);
    }
}
