package com.tokopedia.affiliate.feature.tracking.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.feature.tracking.view.contract.AffContract;
import com.tokopedia.affiliate.feature.tracking.view.presenter.AffTrackingPresenter;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

@Module
public class AffTrackingModule {
    @AffTrackingScope
    @Provides
    AffContract.Presenter affTrackingPresenter(AffTrackingPresenter presenter){
        return presenter;
    }

    @AffTrackingScope
    @Provides
    AffiliateAnalytics provideAffiliateAnalytics(UserSessionInterface userSessionInterface) {
        return new AffiliateAnalytics(userSessionInterface);
    }

    @AffTrackingScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
