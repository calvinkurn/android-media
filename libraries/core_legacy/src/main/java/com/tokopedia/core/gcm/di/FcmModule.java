package com.tokopedia.core.gcm.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tokopedia.fcmcommon.FirebaseMessagingManager;
import com.tokopedia.fcmcommon.FirebaseMessagingManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FcmModule {

    private Context context;

    public FcmModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    FirebaseMessagingManager provideFcmManager(SharedPreferences sharedPreferences) {
        return new FirebaseMessagingManagerImpl(context, sharedPreferences);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
