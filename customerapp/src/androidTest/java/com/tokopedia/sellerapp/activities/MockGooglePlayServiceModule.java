package com.tokopedia.sellerapp.activities;

import android.content.Context;

import com.tokopedia.core.gcm.GCMHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

/**
 * Created by normansyahputa on 9/11/16.
 */
@Module
public class MockGooglePlayServiceModule {
    @Provides
    @Singleton
    public GCMHandler provideGcmHandler(Context context) {
        return mock(GCMHandler.class);
    }
}
