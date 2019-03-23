package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.data.mapper.BuyerAccountMapper;
import com.tokopedia.home.account.data.model.AccountModel;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.navigation_common.model.SaldoModel;
import com.tokopedia.navigation_common.model.WalletModel;
import com.tokopedia.navigation_common.model.WalletPref;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    private CheckAffiliateUseCase checkAffiliateUseCase;

    @Inject
    public GetBuyerAccountUseCase(GraphqlUseCase graphqlUseCase,
                                  Observable<WalletModel> tokocashAccountBalance,
                                  BuyerAccountMapper mapper,
                                  WalletPref walletPref,
                                  UserSession userSession,
                                  CheckAffiliateUseCase checkAffiliateUseCase) {
        this.graphqlUseCase = graphqlUseCase;
        this.tokocashAccountBalance = tokocashAccountBalance;
        this.mapper = mapper;
        this.walletPref = walletPref;
        this.userSession = userSession;
        this.checkAffiliateUseCase = checkAffiliateUseCase;
    }

    @Override
    public Observable<BuyerViewModel> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getAccountData(requestParams),
                tokocashAccountBalance,
                checkIsAffiliate(requestParams),
                (accountModel, walletModel, isAffiliate) -> {
                    accountModel.setWallet(walletModel);
                    accountModel.setAffiliate(isAffiliate);
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
                    String saldoQuery = request.getString(AccountConstants.SALDO_QUERY, "");
                    Map<String, Object> variables = (Map<String, Object>) request.getObject(VARIABLES);

                    if (!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest requestGraphql = new GraphqlRequest(query,
                                AccountModel.class, variables, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(requestGraphql);

                        GraphqlRequest saldoGraphql = new GraphqlRequest(saldoQuery,
                                SaldoModel.class);
                        graphqlUseCase.addRequest(saldoGraphql);


                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));

                })
                .map(graphqlResponse -> {
                    AccountModel accountModel = graphqlResponse.getData(AccountModel.class);
                    SaldoModel saldoModel = graphqlResponse.getData(SaldoModel.class);
                    accountModel.setSaldoModel(saldoModel);
                    return accountModel;
                });
    }

    private Observable<Boolean> checkIsAffiliate(RequestParams requestParams) {
        return checkAffiliateUseCase.createObservable(requestParams).subscribeOn(Schedulers.io());
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
        if (accountModel != null) {
            userSession.setIsAffiliateStatus(accountModel.isAffiliate());
        }
    }
}
