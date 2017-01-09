package com.tokopedia.sellerapp.daggerModules;

import android.content.Context;

import com.tokopedia.sellerapp.home.utils.ImageHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by normansyahputa on 8/28/16.
 */
@Module
public class UtilModules {

    @Provides
    @Singleton
    public ImageHandler provideImageHandler(Context context){
        return new ImageHandler(context);
    }
}
