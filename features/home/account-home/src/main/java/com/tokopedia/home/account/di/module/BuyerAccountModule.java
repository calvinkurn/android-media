package com.tokopedia.home.account.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.AccountHomeRouter;
import com.tokopedia.home.account.data.mapper.BuyerAccountMapper;
import com.tokopedia.home.account.di.scope.BuyerAccountScope;
import com.tokopedia.home.account.domain.GetBuyerAccountUseCase;
import com.tokopedia.home.account.presentation.BuyerAccount;
import com.tokopedia.home.account.presentation.presenter.BuyerAccountPresenter;
import com.tokopedia.navigation_common.model.WalletPref;

import dagger.Module;
import dagger.Provides;

/**
 * @author okasurya on 7/17/18.
 */
@Module
public class BuyerAccountModule {
    @Provides
    @BuyerAccountScope
    BuyerAccount.Presenter provideBuyerAccountPresenter(GetBuyerAccountUseCase getBuyerAccountUseCase) {
        return new BuyerAccountPresenter(getBuyerAccountUseCase);
    }

    @Provides
    WalletPref provideWalletPref(@ApplicationContext Context context, Gson gson){
        return new WalletPref(context, gson);
    }

    @Provides
    GetBuyerAccountUseCase provideGetBuyerAccountUseCase(@ApplicationContext Context context,
                                                         GraphqlUseCase graphqlUseCase,
                                                         BuyerAccountMapper buyerAccountMapper,
                                                         WalletPref walletPref) {
        return new GetBuyerAccountUseCase(
                graphqlUseCase,
                ((AccountHomeRouter) context).getTokoCashAccountBalance(),
                buyerAccountMapper,
                walletPref
        );
    }
}
