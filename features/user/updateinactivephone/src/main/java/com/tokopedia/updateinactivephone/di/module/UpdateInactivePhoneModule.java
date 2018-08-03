package com.tokopedia.updateinactivephone.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.updateinactivephone.usecase.CheckPhoneNumberStatusUsecase;
import com.tokpedia.updateinactivephone.di.UpdateInactivePhoneScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UpdateInactivePhoneModule {

    @UpdateInactivePhoneScope
    @Provides
    CheckPhoneNumberStatusUsecase provideCheckPhoneNumberStatusUsecase(@ApplicationContext Context context) {
        return new CheckPhoneNumberStatusUsecase(context);
    }
}
