package com.tokopedia.affiliate.feature.onboarding.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.GetUsernameSuggestionUseCase;
import com.tokopedia.affiliate.feature.onboarding.domain.usecase.RegisterUsernameUseCase;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.listener.UsernameInputContract;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.RecommendProductPresenter;
import com.tokopedia.affiliate.feature.onboarding.view.presenter.UsernameInputPresenter;
import com.tokopedia.affiliatecommon.domain.GetProductAffiliateGqlUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by milhamj on 10/4/18.
 */
@Module
public class OnboardingModule {
    @OnboardingScope
    @Provides
    UsernameInputContract.Presenter provideUsernameInputPresenter(
            GetUsernameSuggestionUseCase getUsernameSuggestionUseCase,
            RegisterUsernameUseCase registerUsernameUseCase) {
        return new UsernameInputPresenter(getUsernameSuggestionUseCase, registerUsernameUseCase);
    }

    @OnboardingScope
    @Provides
    RecommendProductContract.Presenter provideRecommendProductPresenter(
            GetProductAffiliateGqlUseCase getProductAffiliateGqlUseCase) {
        return new RecommendProductPresenter(getProductAffiliateGqlUseCase);
    }

    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }
}
