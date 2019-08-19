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
import com.tokopedia.home.account.data.util.BuyerEmptyMapper;
import com.tokopedia.home.account.di.scope.BuyerAccountScope;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.presenter.BuyerAccountPresenter;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule;
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/17/18.
 */
@Module(includes = {TopAdsWishlistModule.class})
public class BuyerAccountModule {
    @Provides
    @BuyerAccountScope
    BuyerAccount.Presenter provideBuyerAccountPresenter(GetBuyerAccountUseCase getBuyerAccountUseCase,
                                                        GetRecommendationUseCase getRecommendationUseCase,
                                                        TopAdsWishlishedUseCase topAdsWishlishedUseCase,
                                                        AddWishListUseCase addWishListUseCase,
                                                        RemoveWishListUseCase removeWishListUseCase,
                                                        UserSession userSession) {
        return new BuyerAccountPresenter(
                getBuyerAccountUseCase,
                getRecommendationUseCase,
                topAdsWishlishedUseCase,
                addWishListUseCase,
                removeWishListUseCase,
                userSession);
    }

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }

    @Provides
    AddWishListUseCase provideAddWishlistUseCase(@ApplicationContext Context context) {
        return new AddWishListUseCase(context);
    }

    @Provides
    RemoveWishListUseCase provideRemoveWishlistUseCase(@ApplicationContext Context context) {
        return new RemoveWishListUseCase(context);
    }

    @Provides
    GetRecommendationUseCase provideGetRecomendationUseCase(@Named("recommendationQuery") String recomQuery,
                                                            GraphqlUseCase graphqlUseCase,
                                                            UserSession userSession){
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
                                                         BuyerEmptyMapper buyerEmptyMapper,
                                                         WalletPref walletPref,
                                                         UserSession userSession,
                                                         CheckAffiliateUseCase checkAffiliateUseCase) {
        return new GetBuyerAccountUseCase(
                graphqlUseCase,
                ((AccountHomeRouter) context).getTokoCashAccountBalance(),
                buyerAccountMapper,
                buyerEmptyMapper,
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

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
