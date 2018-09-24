package com.tokopedia.train.homepage.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.train.homepage.domain.GetTrainPromoUseCase;
import com.tokopedia.train.homepage.presentation.TrainHomepageCache;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvarisi on 3/1/18.
 */
@Module
public class TrainHomepageModule {

    @Provides
    TrainHomepageCache provideTrainHomepageCache(@ApplicationContext Context context) {
        return new TrainHomepageCache(context);
    }

    @Provides
    GetTrainPromoUseCase provideGetTrainPromoUseCase() {
        return new GetTrainPromoUseCase();
    }

}