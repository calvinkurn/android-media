package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase;
import com.tokopedia.common_wallet.balance.domain.GetWalletBalanceUseCase;
import com.tokopedia.common_wallet.pendingcashback.domain.GetPendingCasbackUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.R;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.domain.GetBuyerWalletBalanceUseCase;
import com.tokopedia.home.account.revamp.domain.data.mapper.BuyerAccountMapper;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.recommendation_widget_common.di.RecommendationModule;
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule;
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
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson) {
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
    GetBuyerAccountUseCase provideGetBuyerAccountUseCase(@ApplicationContext Context context,
                                                         GraphqlUseCase graphqlUseCase,
                                                         BuyerAccountMapper buyerAccountMapper,
                                                         GetBuyerWalletBalanceUseCase getBuyerWalletBalanceUseCase,
                                                         WalletPref walletPref,
                                                         UserSession userSession,
                                                         CheckAffiliateUseCase checkAffiliateUseCase) {
        return new GetBuyerAccountUseCase(
                graphqlUseCase,
                getBuyerWalletBalanceUseCase,
                buyerAccountMapper,
                walletPref,
                userSession,
                checkAffiliateUseCase
        );
    }

    @Provides
    GetBuyerWalletBalanceUseCase provideBuyerWalletBalance(GetWalletBalanceUseCase getWalletBalanceUseCase,
                                                           GetPendingCasbackUseCase getPendingCasbackUseCase) {
        return new GetBuyerWalletBalanceUseCase(getWalletBalanceUseCase, getPendingCasbackUseCase);
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

    @Provides
    RemoteConfig provideRemoteConfig(@ApplicationContext Context context) {
        return new FirebaseRemoteConfigImpl(context);
    }
}
