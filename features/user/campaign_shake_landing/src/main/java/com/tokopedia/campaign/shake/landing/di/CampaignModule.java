package com.tokopedia.campaign.shake.landing.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module
public class CampaignModule {

    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }
}
