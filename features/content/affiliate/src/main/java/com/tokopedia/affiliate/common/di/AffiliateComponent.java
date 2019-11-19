package com.tokopedia.affiliate.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.common.domain.usecase.CheckQuotaUseCase;

import dagger.Component;

/**
 * @author by yfsx on 13/09/18.
 */
@AffiliateScope
@Component(modules = AffiliateModule.class, dependencies = BaseAppComponent.class)
public interface AffiliateComponent {

    @ApplicationContext
    Context getContext();

    CheckQuotaUseCase provideCheckQuotaUseCase();

    AffiliateAnalytics provideAffiliateAnalytics();
}
