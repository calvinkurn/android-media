package com.tokopedia.home.account.domain;

import android.text.TextUtils;

import com.tokopedia.affiliatecommon.domain.CheckAffiliateUseCase;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.home.account.AccountConstants;
import com.tokopedia.home.account.data.model.tokopointshortcut.ShortcutResponse;
import com.tokopedia.home.account.presentation.viewmodel.base.BuyerViewModel;
import com.tokopedia.home.account.revamp.domain.data.mapper.BuyerAccountMapper;
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel;
import com.tokopedia.navigation_common.model.SaldoModel;
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
    private GetBuyerWalletBalanceUseCase getBuyerWalletBalanceUseCase;
    private WalletPref walletPref;
    private UserSession userSession;
    private CheckAffiliateUseCase checkAffiliateUseCase;

    @Inject
    public GetBuyerAccountUseCase(GraphqlUseCase graphqlUseCase,
                                  GetBuyerWalletBalanceUseCase getBuyerWalletBalanceUseCase,
                                  BuyerAccountMapper mapper,
                                  WalletPref walletPref,
                                  UserSession userSession,
                                  CheckAffiliateUseCase checkAffiliateUseCase) {
        this.graphqlUseCase = graphqlUseCase;
        this.getBuyerWalletBalanceUseCase = getBuyerWalletBalanceUseCase;
        this.mapper = mapper;
        this.walletPref = walletPref;
        this.userSession = userSession;
        this.checkAffiliateUseCase = checkAffiliateUseCase;
    }

    @Override
    public Observable<BuyerViewModel> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getAccountData(requestParams),
                getBuyerWalletBalanceUseCase.createObservable(RequestParams.EMPTY),
                checkIsAffiliate(requestParams),
                (accountDataModel, walletModel, isAffiliate) -> {
                    accountDataModel.setWallet(walletModel);
                    accountDataModel.setAffiliate(isAffiliate);
                    return accountDataModel;
                })
                .doOnNext(this::savePhoneVerified)
                .doOnNext(this::saveIsAffiliateStatus)
                .doOnNext(this::saveDebitInstantData)
                .map(mapper);
    }

    private Observable<AccountDataModel> getAccountData(RequestParams requestParams) {
        return Observable
                .just(requestParams)
                .flatMap((Func1<RequestParams, Observable<GraphqlResponse>>) request -> {
                    String query = request.getString(AccountConstants.QUERY, "");
                    String saldoQuery = request.getString(AccountConstants.SALDO_QUERY, "");
                    String rewardQuery = request.getString(AccountConstants.REWARD_SHORTCUT_QUERY, "");
                    Map<String, Object> variables = (Map<String, Object>) request.getObject(VARIABLES);

                    if (!TextUtils.isEmpty(query) && variables != null) {
                        GraphqlRequest requestGraphql = new GraphqlRequest(query,
                                AccountDataModel.class, variables, false);
                        graphqlUseCase.clearRequest();
                        graphqlUseCase.addRequest(requestGraphql);

                        GraphqlRequest saldoGraphql = new GraphqlRequest(saldoQuery,
                                SaldoModel.class);
                        graphqlUseCase.addRequest(saldoGraphql);

                        GraphqlRequest rewardGraphql = new GraphqlRequest(rewardQuery, ShortcutResponse.class);
                        graphqlUseCase.addRequest(rewardGraphql);

                        return graphqlUseCase.createObservable(null);
                    }

                    return Observable.error(new Exception("Query and/or variable are empty."));

                })
                .map(graphqlResponse -> graphqlResponse.getData(AccountDataModel.class));
    }

    private Observable<Boolean> checkIsAffiliate(RequestParams requestParams) {
        if (userSession.isAffiliate()) {
            return Observable.just(userSession.isAffiliate());
        } else {
            return checkAffiliateUseCase.createObservable(requestParams)
                    .subscribeOn(Schedulers.io());
        }
    }

    private void savePhoneVerified(AccountDataModel accountDataModel) {
        if (accountDataModel.getProfile() != null) {
            userSession.setIsMSISDNVerified(accountDataModel.getProfile().isPhoneVerified());
        }
    }

    private void saveIsAffiliateStatus(AccountDataModel accountDataModel) {
        if (accountDataModel != null) {
            userSession.setIsAffiliateStatus(accountDataModel.isAffiliate());
        }
    }

    private void saveDebitInstantData(AccountDataModel accountDataModel) {
        if (accountDataModel.getDebitInstant() != null) {
            walletPref.saveDebitInstantUrl(accountDataModel.getDebitInstant().getData().getRedirectUrl());
        }
    }
}