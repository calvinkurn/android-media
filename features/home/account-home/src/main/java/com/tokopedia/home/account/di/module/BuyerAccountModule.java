package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.data.mapper.BuyerAccountMapper;
import com.tokopedia.home.account.di.scope.BuyerAccountScope;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.presenter.BuyerAccountPresenter;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/17/18.
 */
@Module
public class BuyerAccountModule {
    @Provides
    @BuyerAccountScope
    BuyerAccount.Presenter provideBuyerAccountPresenter(GetBuyerAccountUseCase getBuyerAccountUseCase,
                                                        GetRecommendationUseCase getRecommendationUseCase) {
        return new BuyerAccountPresenter(getBuyerAccountUseCase, getRecommendationUseCase);
    }

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }

    @Provides
    GetRecommendationUseCase provideGetRecomendationUseCase(@Named("recommendationQuery") String recomQuery,
                                                            GraphqlUseCase graphqlUseCase,
                                                            UserSessionInterface userSession){
        return new GetRecommendationUseCase(recomQuery, graphqlUseCase, userSession);
    }

    @Provides
    @Named("recommendationQuery")
    String provideRecommendationRawQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.query_recommendation_widget);
    }

    @Provides
    GetBuyerAccountUseCase provideGetBuyerAccountUseCase(@ApplicationContext Context context,
                                                         GraphqlUseCase graphqlUseCase,
                                                         BuyerAccountMapper buyerAccountMapper,
                                                         WalletPref walletPref,
                                                         UserSession userSession,
                                                         CheckAffiliateUseCase checkAffiliateUseCase) {
        return new GetBuyerAccountUseCase(
                graphqlUseCase,
                ((AccountHomeRouter) context).getTokoCashAccountBalance(),
                buyerAccountMapper,
                walletPref,
                userSession,
                checkAffiliateUseCase
        );
    }

    @Provides
    CheckAffiliateUseCase provideCheckAffiliateUseCase(@ApplicationContext Context context,
                                                       GraphqlUseCase graphqlUseCase) {
        return new CheckAffiliateUseCase(context, graphqlUseCase);
    }

    @Provides
    UserSession provideUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
