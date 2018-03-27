package com.tokopedia.sellerapp;

import android.content.Context;

import com.tkpd.library.utils.image.ImageHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by normansyahputa on 8/17/16.
 */

@Module
public class TestUtilModules {

    @Provides
    @Singleton
    public ImageHandler provideImageHandler(Context context) {
        return new ImageHandler(context);
    }
}
