package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.data.mapper.BuyerAccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

import static com.tokopedia.home.account.AccountConstants.VARIABLES;

/**
 * @author by alvinatin on 10/08/18.
 */

public class GetBuyerAccountUseCase extends UseCase<BuyerViewModel> {

    private GraphqlUseCase graphqlUseCase;
    private BuyerAccountMapper mapper;
    private Observable<WalletModel> tokocashAccountBalance;
    private WalletPref walletPref;
    private UserSession userSession;

    @Inject
    public GetBuyerAccountUseCase(GraphqlUseCase graphqlUseCase,
                                  Observable<WalletModel> tokocashAccountBalance,
                                  BuyerAccountMapper mapper,
                                  WalletPref walletPref,
                                  UserSession userSession) {
        this.graphqlUseCase = graphqlUseCase;
        this.tokocashAccountBalance = tokocashAccountBalance;
        this.mapper = mapper;
        this.walletPref = walletPref;
        this.userSession = userSession;
    }

    @Override
    public Observable<BuyerViewModel> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getAccountData(requestParams),
                tokocashAccountBalance,
                (accountModel, walletModel) -> {
                    accountModel.setWallet(walletModel);
                    return accountModel;
                })
                .doOnNext(this::saveLocallyWallet)
                .doOnNext(this::saveLocallyVccUserStatus)
                .doOnNext(this::savePhoneVerified)
                .doOnNext(this::saveIsAffiliateStatus)
                .map(mapper);
    }

    private Observable<AccountModel> getAccountData(RequestParams requestParams) {
        return Observable
                .just(requestParams)
                .flatMap((Func1<RequestParams, Observable<GraphqlResponse>>) request -> {
                    String query = request.getString(AccountConstants.QUERY, "");
                    Map<String, Object> variables = (Map<String, Object>) request.getObject(VARIABLES);

                    if (!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest requestGraphql = new GraphqlRequest(query,
                                AccountModel.class, variables);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(requestGraphql);
                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));
                }).map(graphqlResponse -> graphqlResponse.getData(AccountModel.class));
    }

    private void saveLocallyWallet(AccountModel accountModel) {
        walletPref.saveWallet(accountModel.getWallet());
        if (accountModel.getVccUserStatus() != null) {
            walletPref.setTokoSwipeUrl(accountModel.getVccUserStatus().getRedirectionUrl());
        }
    }

    private void saveLocallyVccUserStatus(AccountModel accountModel) {
        if (accountModel.getVccUserStatus() != null) {
            walletPref.saveVccUserStatus(accountModel.getVccUserStatus());
        }
    }

    private void savePhoneVerified(AccountModel accountModel) {
        if (accountModel.getProfile() != null) {
            userSession.setIsMSISDNVerified(accountModel.getProfile().isPhoneVerified());
        }
    }

    private void saveIsAffiliateStatus(AccountModel accountModel) {
        if (accountModel.getAffiliateModel() != null) {
            userSession.setIsAffiliateStatus(accountModel.getAffiliateModel().isAffiliate());
        }
    }
}
