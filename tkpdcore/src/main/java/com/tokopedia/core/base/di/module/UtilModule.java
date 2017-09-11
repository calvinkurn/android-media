package com.tokopedia.core.base.di.module;

import android.content.Context;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.util.SessionHandler;

import dagger.Module;
import dagger.Provides;

/**
 * @author normansyahputa on 5/16/17.
 */
@Module
public class UtilModule {

    @Provides
    public SessionHandler provideSessionHandler(@ApplicationContext Context context){
        return new SessionHandler(context);
    }


    @Provides
    public GCMHandler provideGcmHandler(@ApplicationContext Context context){
        return new GCMHandler(context);
    }


    @Provides
    public ImageHandler provideImageHandler(@ApplicationContext Context context){
        return new ImageHandler(context);
    }
}
