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
import com.tokopedia.saldodetails.interactor.DepositCacheInteractor;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractorImpl;
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
    private DepositCacheInteractor depositCacheInteractor;

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
        depositCacheInteractor = new DepositCacheInteractorImpl(context);
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

    /*@Override
    public void setFirstDateParameter() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        String startDate = dateFormat.format(cal.getTime());
        String endDate = dateFormat.format(date);
        getView().setStartDate(startDate);
        getView().setEndDate(endDate);
        paramStartDate = startDate;
        paramEndDate = endDate;

    }*/

   /* @Override
    public void setCache() {
        depositCacheInteractor.getSummaryDepositCache(new DepositCacheInteractor.GetSummaryDepositCacheListener() {
            @Override
            public void onSuccess(GqlDepositSummaryResponse cache) {
                setData(cache);
                getSummaryDeposit();

            }

            @Override
            public void onError(Throwable e) {
                getSummaryDeposit();
            }
        });
    }*/

    @Override
    public void getMerchantSaldoDetails() {
//        getView().setLoading();
        GetMerchantSaldoDetails getMerchantSaldoDetails =
                new GetMerchantSaldoDetails(getView().getContext());

        GetMerchantSaldoDetailsSubscriber getMerchantSaldoDetailsSubscriber =
                new GetMerchantSaldoDetailsSubscriber(this);

        getMerchantSaldoDetails.execute(getMerchantSaldoDetailsSubscriber);
    }

    /*@Override
    public void onSearchClicked() {
        paramStartDate = getView().getStartDate();
        paramEndDate = getView().getEndDate();
        getSummaryDeposit();
    }*/

    /*@Override
    public void onEndDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getEndDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setEndDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, SEARCH_DELAY);
        });

    }*/

    /*private String getDate(int year, int month, int day) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        return dateFormat.format(cal.getTime());
    }*/

    /*@Override
    public void onStartDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getStartDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setStartDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, SEARCH_DELAY);

        });
    }*/

    /*private String dateFormatter(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedStart = null;
        try {
            formattedStart = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf_ws.format(formattedStart);

    }*/

    /*@Override
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (paging.CheckNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && canLoadMore()) {
            paging.nextPage();
            getSummaryDeposit();
        }
    }*/


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

                if (isSeller()) {
                    depositCacheInteractor.setUsableSellerSaldoBalanceCache(
                            gqlSaldoBalanceResponse.getUsableSellerSaldo());
                    getView().setSellerSaldoBalance(gqlSaldoBalanceResponse.getUsableSellerSaldo().getDeposit(),
                            gqlSaldoBalanceResponse.getUsableSellerSaldo().getFormattedAmount());
                    getView().showSellerSaldoRL();

                    depositCacheInteractor.setUsableBuyerSaldoBalanceCache(gqlSaldoBalanceResponse.
                            getUsableBuyerSaldo());
                    getView().setBuyerSaldoBalance(gqlSaldoBalanceResponse.getUsableBuyerSaldo().getDeposit(),
                            gqlSaldoBalanceResponse.getUsableBuyerSaldo().getFormattedAmount());
                    getView().showBuyerSaldoRL();

                    getView().showSaldoBalanceSeparator();
                } else {
                    getView().hideSellerSaldoRL();
                    getView().hideBuyerSaldoRL();
                    getView().hideSaldoBalanceSeparator();
                }

                long totalBalance = gqlSaldoBalanceResponse.getUsableBuyerSaldo().getDeposit() +
                        gqlSaldoBalanceResponse.getUsableSellerSaldo().getDeposit();

                getView().setBalance(totalBalance, CurrencyFormatUtil.
                        convertPriceValueToIdrFormat(totalBalance, false));


                getView().setWithdrawButtonState(totalBalance != 0);

                long holdBalance = gqlSaldoBalanceResponse.getHoldSellerSaldo().getDeposit() +
                        gqlSaldoBalanceResponse.getHoldBuyerSaldo().getDeposit();

                if (holdBalance > 0) {
                    getView().showHoldWarning(gqlSaldoBalanceResponse.getHoldSellerSaldo().
                            getFormattedAmount());
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


    /*@Override
    public void getSummaryDeposit() {
        if (!isViewAttached()) {
            return;
        }
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);

            getDepositSummaryUseCase.setRequesting(true);
            getDepositSummaryUseCase.setRequestVariables(getSummaryDepositParam());

            getDepositSummaryUseCase.execute(new Subscriber<GraphqlResponse>() {
                @Override
                public void onCompleted() {
                    getDepositSummaryUseCase.setRequesting(false);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, e.toString());
                    hideLoading();
                    ErrorHandler.getErrorMessage(getView().getContext(), e);
                    if (e instanceof UnknownHostException ||
                            e instanceof SocketTimeoutException) {
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else {
                        getView().setActionsEnabled(true);
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
                        } else {
                            getView().setRetry(getView().getString(R.string.sp_empty_state_error));
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {
                    hideLoading();
                    onDepositSummaryFetched(graphqlResponse);
                }
            });

        } else {
            getView().finishLoading();
        }
    }*/

    /*private void onDepositSummaryFetched(GraphqlResponse graphqlResponse) {
        if (!isViewAttached()) {
            return;
        }
        getView().setActionsEnabled(true);
        if (graphqlResponse != null &&
                graphqlResponse.getData(GqlDepositSummaryResponse.class) != null) {

            GqlDepositSummaryResponse gqlDepositSummaryResponse =
                    graphqlResponse.getData(GqlDepositSummaryResponse.class);

            if (gqlDepositSummaryResponse != null &&
                    !gqlDepositSummaryResponse.getDepositActivityResponse().isHaveError()) {

                if (paging.getPage() == 1) {
                    getView().getAdapter().clearAllElements();
                    depositCacheInteractor.setSummaryDepositCache(gqlDepositSummaryResponse);
                }
                paging.setHasNext(gqlDepositSummaryResponse.getDepositActivityResponse()
                        .isHaveNextPage());
                setData(gqlDepositSummaryResponse);
            } else {
                if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.
                        getDepositActivityResponse() != null) {
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState(gqlDepositSummaryResponse.
                                getDepositActivityResponse().getMessage());
                    } else {
                        getView().setRetry(gqlDepositSummaryResponse.
                                getDepositActivityResponse().getMessage());
                    }
                }
            }

        } else {
            if (getView().getAdapter().getItemCount() == 0) {
                getView().showEmptyState(getView().getString(R.string.sp_empty_state_error));
            } else {
                getView().setRetry(getView().getString(R.string.sp_empty_state_error));
            }

        }
        finishLoading();
    }*/

    @Override
    public void onRefresh() {

//        paging.resetPage();
//        getSummaryDeposit();
    }


    /*private Map<String, Object> getSummaryDepositParam() {
        SummaryDepositParam param = new SummaryDepositParam();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat(DATE_FORMAT_WS);
        try {
            Date formattedStart = sdf.parse(paramStartDate);
            Date formattedEnd = sdf.parse(paramEndDate);
            param.setStartDate(getDateParam(sdf_ws.format(formattedStart)));
            param.setEndDate(getDateParam(sdf_ws.format(formattedEnd)));
        } catch (ParseException e) {
            getView().showErrorMessage(getView().getString(R.string.sp_error_invalid_date));
        }

        param.setPage(paging.getPage());
        param.setSeller(isSeller());
        return param.getParamSummaryDeposit();

    }*/

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

            long sellerBalance = getView().getSellerSaldoBalance();
            long buyerBalance = getView().getBuyerSaldoBalance();

            if (sellerBalance < minSaldoLimit && buyerBalance < minSaldoLimit) {
                getView().showErrorMessage(getView().getString(R.string.saldo_min_withdrawal_error));
                return;
            } else {
                withdrawActivityBundle.putBoolean(IS_SELLER, isSeller());
                withdrawActivityBundle.putLong(BUNDLE_SALDO_BUYER_TOTAL_BALANCE_INT, getView().getBuyerSaldoBalance());
                withdrawActivityBundle.putLong(BUNDLE_SALDO_SELLER_TOTAL_BALANCE_INT, getView().getSellerSaldoBalance());
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

    /*private void showLoading() {
        if (isViewAttached()) {
            getView().getAdapter().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().getAdapter().hideLoading();
            getView().finishLoading();
        }
    }*/


   /* private boolean canLoadMore() {
        return !getDepositSummaryUseCase.isRequesting();
    }

    private boolean isOnLastPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }

    private int getStartYear(String date) {
        String year = date.substring(6, 10);
        return Integer.parseInt(year);
    }

    private int getStartMonth(String date) {
        String month = date.substring(3, 5);
        return Integer.parseInt(month);
    }

    private int getDay(String date) {
        String day = date.substring(0, 2);
        return Integer.parseInt(day);
    }*/

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

    /*@Override
    public void finishLoading() {
        if (!isViewAttached()) {
            return;
        }
        getView().finishLoading();
    }*/

    public boolean isSeller() {
        return isSeller;
    }

    public void setSeller(boolean seller) {
        isSeller = seller;
    }
}
