package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.deposit.listener.MerchantSaldoDetailsActionListener;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlWithdrawalTickerResponse;
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber;
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase;
import com.tokopedia.saldodetails.usecase.GetMerchantSaldoDetails;
import com.tokopedia.saldodetails.usecase.GetSaldoBalanceUseCase;
import com.tokopedia.saldodetails.usecase.GetTickerWithdrawalMessageUseCase;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT;
import static com.tokopedia.saldodetails.view.fragment.SaldoDepositFragment.BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT;

public class SaldoDetailsPresenter extends BaseDaggerPresenter<SaldoDetailContract.View>
        implements SaldoDetailContract.Presenter, MerchantSaldoDetailsActionListener {

    public static final int REQUEST_WITHDRAW_CODE = 1;
    private static final String IS_SELLER = "is_seller";

    private Bundle withdrawActivityBundle = new Bundle();

    private final long minSaldoLimit = 10000;
//    private DepositCacheInteractor depositCacheInteractor;

    @Inject
    GetDepositSummaryUseCase getDepositSummaryUseCase;
    @Inject
    GetSaldoBalanceUseCase getSaldoBalanceUseCase;
    @Inject
    GetTickerWithdrawalMessageUseCase getTickerWithdrawalMessageUseCase;
    @Inject
    SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;
    private boolean isSeller;

    @Inject
    public SaldoDetailsPresenter(@ApplicationContext Context context) {
//        depositCacheInteractor = new DepositCacheInteractorImpl(context);
//        this.paging = new PagingHandler();
    }

    @Override
    public void detachView() {
        super.detachView();
        try {
            setMerchantSaldoStatusUseCase.unsubscribe();
            getDepositSummaryUseCase.unsubscribe();
            getSaldoBalanceUseCase.unsubscribe();
            getTickerWithdrawalMessageUseCase.unsubscribe();
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void getMerchantSaldoDetails() {
//        getView().setLoading();
        GetMerchantSaldoDetails getMerchantSaldoDetails =
                new GetMerchantSaldoDetails(getView().getContext());

        GetMerchantSaldoDetailsSubscriber getMerchantSaldoDetailsSubscriber =
                new GetMerchantSaldoDetailsSubscriber(this);

        getMerchantSaldoDetails.execute(getMerchantSaldoDetailsSubscriber);
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
                    getView().setRetry(getView().getString(R.string.sp_empty_state_error));
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
                if (isSeller()) {
//                    depositCacheInteractor.setUsableSellerSaldoBalanceCache(gqlSaldoBalanceResponse);

                    getView().setSellerSaldoBalance(gqlSaldoBalanceResponse.getSaldo().getSellerUsable(),
                            gqlSaldoBalanceResponse.getSaldo().getSellerUsableFmt());
                    getView().showSellerSaldoRL();

//                    depositCacheInteractor.setUsableBuyerSaldoBalanceCache(gqlSaldoBalanceResponse);
                    getView().setBuyerSaldoBalance(gqlSaldoBalanceResponse.getSaldo().getBuyerUsable(),
                            gqlSaldoBalanceResponse.getSaldo().getBuyerUsableFmt());
                    getView().showBuyerSaldoRL();

                    getView().showSaldoBalanceSeparator();
                } else {
                    getView().hideSellerSaldoRL();
                    getView().hideBuyerSaldoRL();
                    getView().hideSaldoBalanceSeparator();
                }

                float totalBalance = gqlSaldoBalanceResponse.getSaldo().getBuyerUsable() +
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
    public void onRefresh() {

//        paging.resetPage();
//        getSummaryDeposit();
    }

    /*private String getDateParam(String date) {
        return date.replace("/", "-");
    }

    private boolean isValid() {
        Boolean isValid = true;

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        try {
            Date endDate = sdf.parse(paramEndDate);
            Date startDate = sdf.parse(paramStartDate);
            if (endDate.getTime() - startDate.getTime() < 0) {
                isValid = false;
                getView().showInvalidDateError(getView().getString(R.string.sp_error_invalid_date));
            }

            if ((endDate.getTime() - startDate.getTime()) / SEC_TO_DAY_CONVERSION >= MAX_DAYS_DIFFERENCE) {
                isValid = false;
                getView().showInvalidDateError(getView().getString(R.string.sp_title_max_day));
            }
        } catch (ParseException e) {
            isValid = false;
            getView().showInvalidDateError(getView().getString(R.string.sp_error_invalid_date));
        }
        return isValid;
    }*/


   /* private void setData(GqlDepositSummaryResponse data) {
        if (!isViewAttached()) {
            return;
        }
        getView().getAdapter().addElement(data.getDepositActivityResponse().getDepositHistoryList());
        if (getView().getAdapter().getItemCount() == 0) {
            getView().getAdapter().addElement(getView().getDefaultEmptyViewModel());
        }
        if (paging.CheckNextPage()) {
            showLoading();
        } else {
            hideLoading();
        }
    }*/


    @Override
    public void onDrawClicked(Intent intent) {
        if (!isViewAttached()) {
            return;
        }
        Context context = getView().getContext();
        UserSession session = ((AbstractionRouter) context.getApplicationContext()).getSession();
        if (session.isHasPassword()) {

            float sellerBalance = getView().getSellerSaldoBalance();
            float buyerBalance = getView().getBuyerSaldoBalance();

            if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
                getView().showErrorMessage(getView().getString(R.string.saldo_min_withdrawal_error));
            } else {
                withdrawActivityBundle.putBoolean(IS_SELLER, isSeller());
                withdrawActivityBundle.putFloat(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, getView().getBuyerSaldoBalance());
                withdrawActivityBundle.putFloat(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT, getView().getSellerSaldoBalance());
                launchWithdrawActivity(intent);
            }
            /*depositCacheInteractor.getUsableBuyerSaldoBalanceCache(new DepositCacheInteractor.GetUsableSaldoBalanceCacheListener() {
                @Override
                public void onSuccess(GqlSaldoBalanceResponse.Saldo result) {

                    if (result.getDeposit() > 0) {
                        withdrawActivityBundle.putString(BUNDLE_SALDO_BUYER_TOTAL_BALANCE, String.valueOf(result.getFormattedAmount()));
                        withdrawActivityBundle.putString(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, String.valueOf(result.getDeposit()));

                        if (getView().isSellerEnabled()) {
                            fetchSellerSaldoAndLaunchActivity(intent);
                        } else {
                            launchWithdrawActivity(intent);
                        }

                    } else {
                        getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                        *//*if (getView().isSellerFragment()) {
                            getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                        } else {
                            getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                        }*//*
                    }

                }

                @Override
                public void onError(Throwable e) {

                }
            });*/
        } else {
            getView().showWithdrawalNoPassword();
        }
    }

    /*private void fetchSellerSaldoAndLaunchActivity(Intent intent) {
        depositCacheInteractor.getUsableSellerSaldoBalanceCache(new DepositCacheInteractor.GetUsableSaldoBalanceCacheListener() {
            @Override
            public void onSuccess(GqlSaldoBalanceResponse.Saldo result) {
                if (result.getDeposit() > 0) {
                    withdrawActivityBundle.putString(BUNDLE_SALDO_SELLER_TOTAL_BALANCE,
                            String.valueOf(result.getFormattedAmount()));
                    withdrawActivityBundle.putString(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT,
                            String.valueOf(result.getDeposit()));
                    launchWithdrawActivity(intent);
                } else {
                    getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                    *//*if (getView().isSellerFragment()) {
                        getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                    } else {
                        getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                    }*//*
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }*/

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
    public void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails) {
        if (!isViewAttached()) {
            return;
        }
        getView().showSaldoPrioritasFragment(sellerDetails);
    }

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }
}
