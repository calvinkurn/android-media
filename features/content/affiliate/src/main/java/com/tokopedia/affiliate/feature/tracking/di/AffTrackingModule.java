package com.tokopedia.affiliate.feature.tracking.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.tracking.view.presenter.AffTrackingPresenter;
import com.tokopedia.affiliate.feature.tracking.domain.interactor.GetByMeUseCase;

import dagger.Module;
import dagger.Provides;

@AffTrackingScope
@Module
public class AffTrackingModule {

    @AffTrackingScope
    @Provides
    GetByMeUseCase getByMeUseCase(@ApplicationContext Context context) {
        return new GetByMeUseCase(context);
    }

    @AffTrackingScope
    @Provides
    AffTrackingPresenter affTrackingPresenter(){
        return new AffTrackingPresenter();
    }
}
