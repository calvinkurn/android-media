package com.tokopedia.sellerapp;

import android.content.Context;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.sellerapp.activities.TestRxWrapper;
import com.tokopedia.sellerapp.utils.RxWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by normansyahputa on 8/17/16.
 */

@Module
public class TestUtilModules {

//    @Provides
//    @Singleton
//    public RxWrapper<LoginResponseParser> provideRxWrapper(){
//        return new TestRxWrapper<>();
//    }
//
//    @Provides
//    @Singleton
//    public Printer providePrinter(){
//        return mock(Printer.class);
////        return new Printer();
//    }
//
    @Provides
    @Singleton
    public ImageHandler provideImageHandler(Context context){
        return new ImageHandler(context);
    }
}
