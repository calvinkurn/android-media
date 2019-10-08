package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.deposit.listener.MerchantFinancialStatusActionListener;
import com.tokopedia.saldodetails.response.model.GqlMclLateCountResponse;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlWithdrawalTickerResponse;
import com.tokopedia.saldodetails.subscriber.GetMerchantCreditDetailsSubscriber;
import com.tokopedia.saldodetails.subscriber.GetMerchantFinancialStatusSubscriber;
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber;
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase;
import com.tokopedia.saldodetails.usecase.GetMCLLateCountUseCase;
import com.tokopedia.saldodetails.usecase.GetMerchantCreditDetails;
import com.tokopedia.saldodetails.usecase.GetMerchantFinancialStatus;
import com.tokopedia.saldodetails.usecase.GetMerchantSaldoDetails;
import com.tokopedia.saldodetails.usecase.GetSaldoBalanceUseCase;
import com.tokopedia.saldodetails.usecase.GetTickerWithdrawalMessageUseCase;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT;
import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT;

public class SaldoDetailsPresenter extends BaseDaggerPresenter<SaldoDetailContract.View>
        implements SaldoDetailContract.Presenter, MerchantFinancialStatusActionListener {

    public static final int REQUEST_WITHDRAW_CODE = 1;
    private static final String IS_SELLER = "is_seller";
    private static final String IS_WITHDRAW_LOCK = "is_lock";
    private static final String MCL_LATE_COUNT = "late_count";
    private static final String FIREBASE_FLAG_STATUS="is_on";

    private Bundle withdrawActivityBundle = new Bundle();

    @Inject
    GetDepositSummaryUseCase getDepositSummaryUseCase;
    @Inject
    GetSaldoBalanceUseCase getSaldoBalanceUseCase;
    @Inject
    GetTickerWithdrawalMessageUseCase getTickerWithdrawalMessageUseCase;
    @Inject
    SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;
    @Inject
    GetMerchantSaldoDetails getMerchantSaldoDetails;
    @Inject
    GetMerchantCreditDetails getMerchantCreditDetails;
    @Inject
    GetMerchantFinancialStatus getMerchantFinancialStatus;
    @Inject
    GetMCLLateCountUseCase getMCLLateCountUseCase;

    private boolean isSeller;

    @Inject
    public SaldoDetailsPresenter() {
    }

    @Override
    public void detachView() {
        super.detachView();
        try {
            setMerchantSaldoStatusUseCase.unsubscribe();
            getDepositSummaryUseCase.unsubscribe();
            getSaldoBalanceUseCase.unsubscribe();
            getTickerWithdrawalMessageUseCase.unsubscribe();
            getMerchantSaldoDetails.unsubscribe();
            getMerchantCreditDetails.unsubscribe();
            getMerchantFinancialStatus.unsubscribe();
            getMCLLateCountUseCase.unsubscribe();
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void getUserFinancialStatus() {
        GetMerchantFinancialStatusSubscriber getMerchantFinancialStatusSubscribe =
                new GetMerchantFinancialStatusSubscriber(this);
        getMerchantFinancialStatus.execute(getMerchantFinancialStatusSubscribe);
    }

    @Override
    public void getMerchantSaldoDetails() {
        GetMerchantSaldoDetailsSubscriber getMerchantSaldoDetailsSubscriber =
                new GetMerchantSaldoDetailsSubscriber(this);
        getMerchantSaldoDetails.execute(getMerchantSaldoDetailsSubscriber);
    }

    @Override
    public void getMerchantCreditLineDetails() {
        GetMerchantCreditDetailsSubscriber getMerchantCreditDetailsSubscriber =
                new GetMerchantCreditDetailsSubscriber(this);
        getMerchantCreditDetails.execute(getMerchantCreditDetailsSubscriber);
    }

    @Override
    public void getSaldoBalance() {

        getSaldoBalanceUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                if (e instanceof UnknownHostException) {
                    getView().setRetry();

                } else if (e instanceof SocketTimeoutException) {
                    getView().setRetry();
                } else {
                    getView().setRetry(getView().getString(com.tokopedia.saldodetails.R.string.sp_empty_state_error));
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (!isViewAttached()) {
                    return;
                }

                GqlSaldoBalanceResponse gqlSaldoBalanceResponse =
                        graphqlResponse.getData(GqlSaldoBalanceResponse.class);

                if (gqlSaldoBalanceResponse == null) {
                    return;
                }

                getView().setSellerSaldoBalance(gqlSaldoBalanceResponse.getSaldo().getSellerUsable(),
                        gqlSaldoBalanceResponse.getSaldo().getSellerUsableFmt());
                getView().showSellerSaldoRL();

                getView().setBuyerSaldoBalance(gqlSaldoBalanceResponse.getSaldo().getBuyerUsable(),
                        gqlSaldoBalanceResponse.getSaldo().getBuyerUsableFmt());
                getView().showBuyerSaldoRL();

                long totalBalance = gqlSaldoBalanceResponse.getSaldo().getBuyerUsable() +
                        gqlSaldoBalanceResponse.getSaldo().getSellerUsable();

                getView().setBalance(totalBalance, CurrencyFormatUtil.
                        convertPriceValueToIdrFormat(totalBalance, false));


                getView().setWithdrawButtonState(totalBalance != 0);

                float holdBalance = gqlSaldoBalanceResponse.getSaldo().getBuyerHold() +
                        gqlSaldoBalanceResponse.getSaldo().getSellerHold();

                if (holdBalance > 0) {
                    getView().showHoldWarning(CurrencyFormatUtil.convertPriceValueToIdrFormat(holdBalance, false));
                } else {
                    getView().hideWarning();
                }
            }
        });

    }


    @Override
    public void getMCLLateCount() {

        getMCLLateCountUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

                if (!isViewAttached()) {
                    return;
                }
                getView().hideWithdrawTicker();
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (graphqlResponse != null) {
                    GqlMclLateCountResponse gqlMclLateCountResponse = graphqlResponse.getData(GqlMclLateCountResponse.class);
                    if (gqlMclLateCountResponse != null) {
                        getView().setLateCount(gqlMclLateCountResponse.getMclGetLatedetails().getLateCount());
                    } else
                        getView().hideWithdrawTicker();
                }
            }
        });
    }

    @Override
    public void getTickerWithdrawalMessage() {
        getTickerWithdrawalMessageUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideTickerMessage();
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                if (graphqlResponse != null) {
                    GqlWithdrawalTickerResponse gqlWithdrawalTickerResponse =
                            graphqlResponse.getData(GqlWithdrawalTickerResponse.class);
                    if (gqlWithdrawalTickerResponse != null &&
                            !TextUtils.isEmpty(gqlWithdrawalTickerResponse.getWithdrawalTicker().getTickerMessage())) {
                        getView().showTickerMessage(gqlWithdrawalTickerResponse.getWithdrawalTicker().getTickerMessage());
                    } else {
                        getView().hideTickerMessage();
                    }
                }
            }
        });
    }

    @Override
    public void onDrawClicked(Intent intent, int statusWithDrawLock,int mclLateCount,boolean showMclBlockTickerFirebaseFlag) {
        if (!isViewAttached()) {
            return;
        }
        Context context = getView().getContext();
        UserSessionInterface userSession = new UserSession(context);
        if (userSession.hasPassword()) {

            float sellerBalance = getView().getSellerSaldoBalance();
            float buyerBalance = getView().getBuyerSaldoBalance();

            long minSaldoLimit = 10000;
            if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
                getView().showErrorMessage(getView().getString(com.tokopedia.saldodetails.R.string.saldo_min_withdrawal_error));
            } else {
                withdrawActivityBundle.putBoolean(FIREBASE_FLAG_STATUS,showMclBlockTickerFirebaseFlag);
                withdrawActivityBundle.putInt(IS_WITHDRAW_LOCK, statusWithDrawLock);
                withdrawActivityBundle.putInt(MCL_LATE_COUNT, mclLateCount);
                withdrawActivityBundle.putBoolean(IS_SELLER, isSeller());
                withdrawActivityBundle.putFloat(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, getView().getBuyerSaldoBalance());
                withdrawActivityBundle.putFloat(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT, getView().getSellerSaldoBalance());
                launchWithdrawActivity(intent);
            }
        } else {
            getView().showWithdrawalNoPassword();
        }
    }


    private void launchWithdrawActivity(Intent intent) {
        intent.putExtras(withdrawActivityBundle);
        getView().getActivity().startActivityForResult(intent, REQUEST_WITHDRAW_CODE);
    }

    @Override
    public void hideSaldoPrioritasFragment() {
        if (!isViewAttached()) {
            return;
        }
        getView().hideSaldoPrioritasFragment();
    }

    @Override
    public void hideUserFinancialStatusLayout() {
        if (!isViewAttached()) {
            return;
        }
        getView().hideUserFinancialStatusLayout();
    }

    @Override
    public void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails) {
        if (!isViewAttached()) {
            return;
        }
        getView().showSaldoPrioritasFragment(sellerDetails);
    }

    @Override
    public void hideMerchantCreditLineFragment() {
        if (!isViewAttached()) {
            return;
        }
        getView().hideMerchantCreditLineFragment();
    }

    @Override
    public void showMerchantCreditLineFragment(GqlMerchantCreditResponse response) {
        if (!isViewAttached()) {
            return;
        }
        getView().showMerchantCreditLineFragment(response);
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }

}


