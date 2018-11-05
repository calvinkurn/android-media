package com.tokopedia.affiliate.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.common.domain.usecase.CheckQuotaUseCase;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;

/**
 * @author by yfsx on 13/09/18.
 */
@Module
public class AffiliateModule {

    @AffiliateScope
    @Provides
    CheckQuotaUseCase provideCheckQuotaUseCase(@ApplicationContext Context context) {
        return new CheckQuotaUseCase(context);
    }

    @AffiliateScope
    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @AffiliateScope
    @Provides
    AffiliateAnalytics provideAffiliateAnalytics(@ApplicationContext Context context, UserSession userSession) {
        return new AffiliateAnalytics((AbstractionRouter)context.getApplicationContext(), userSession);
    }

}
