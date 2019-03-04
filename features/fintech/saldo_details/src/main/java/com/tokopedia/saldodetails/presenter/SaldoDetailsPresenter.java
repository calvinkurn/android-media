package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.deposit.listener.MerchantSaldoDetailsActionListener;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractor;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractorImpl;
import com.tokopedia.saldodetails.response.model.GqlDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlHoldSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlWithdrawalTickerResponse;
import com.tokopedia.saldodetails.response.model.SummaryDepositParam;
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber;
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase;
import com.tokopedia.saldodetails.usecase.GetMerchantSaldoDetails;
import com.tokopedia.saldodetails.usecase.GetSaldoBalanceUseCase;
import com.tokopedia.saldodetails.usecase.GetTickerWithdrawalMessageUseCase;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

import static android.content.ContentValues.TAG;

public class SaldoDetailsPresenter extends BaseDaggerPresenter<SaldoDetailContract.View>
        implements SaldoDetailContract.Presenter, MerchantSaldoDetailsActionListener {

    private static final long SEC_TO_DAY_CONVERSION = 24 * 60 * 60 * 1000;
    private static final long MAX_DAYS_DIFFERENCE = 31;
    private static final java.lang.String DATE_FORMAT_VIEW = "dd MMM yyyy";
    public static final int REQUEST_WITHDRAW_CODE = 1;
    private String paramStartDate;
    private String paramEndDate;
    private static final String BUNDLE_TOTAL_BALANCE = "total_balance";
    private static final String BUNDLE_TOTAL_BALANCE_INT = "total_balance_int";
    private static final java.lang.String DATE_FORMAT_WS = "yyyy/MM/dd";

    private PagingHandler paging;
    private DepositCacheInteractor depositCacheInteractor;

    @Inject
    GetDepositSummaryUseCase getDepositSummaryUseCase;
    @Inject
    GetSaldoBalanceUseCase getSaldoBalanceUseCase;
    @Inject
    GetTickerWithdrawalMessageUseCase getTickerWithdrawalMessageUseCase;
    @Inject
    SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;

    @Inject
    public SaldoDetailsPresenter(@ApplicationContext Context context) {
        depositCacheInteractor = new DepositCacheInteractorImpl(context);
        this.paging = new PagingHandler();
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

    }

    @Override
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
    }

    @Override
    public void getMerchantSaldoDetails() {
        getView().setLoading();
        GetMerchantSaldoDetails getMerchantSaldoDetails =
                new GetMerchantSaldoDetails(getView().getContext());

        GetMerchantSaldoDetailsSubscriber getMerchantSaldoDetailsSubscriber =
                new GetMerchantSaldoDetailsSubscriber(this);

        getMerchantSaldoDetails.execute(getMerchantSaldoDetailsSubscriber);
    }

    @Override
    public void onSearchClicked() {
        paramStartDate = getView().getStartDate();
        paramEndDate = getView().getEndDate();
        getSummaryDeposit();
    }

    @Override
    public void onEndDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getEndDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setEndDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, 500);
        });

    }

    private String getDate(int year, int month, int day) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month - 1);
        return dateFormat.format(cal.getTime());
    }

    @Override
    public void onStartDateClicked(SaldoDatePickerUtil datePicker) {
        String date = dateFormatter(getView().getStartDate());
        datePicker.setDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar((year, month, day) -> {
            String selectedDate = getDate(year, month, day);
            getView().setStartDate(selectedDate);
            new android.os.Handler().postDelayed(this::onSearchClicked, 500);

        });
    }

    private String dateFormatter(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat("dd/MM/yyyy");
        Date formattedStart = null;
        try {
            formattedStart = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf_ws.format(formattedStart);

    }

    @Override
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (paging.CheckNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && canLoadMore()) {
            paging.nextPage();
            getSummaryDeposit();
        }
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
                getView().finishLoading();
                if (e instanceof UnknownHostException) {
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }

                } else if (e instanceof SocketTimeoutException) {
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
                if (!isViewAttached()) {
                    return;
                }

                GqlSaldoBalanceResponse usableSaldoBalanceResponse;
                GqlHoldSaldoBalanceResponse holdSaldoBalanceResponse;

                usableSaldoBalanceResponse = graphqlResponse.getData(GqlSaldoBalanceResponse.class);
                holdSaldoBalanceResponse = graphqlResponse.getData(GqlHoldSaldoBalanceResponse.class);

                depositCacheInteractor.setUsableSaldoBalanceCache(usableSaldoBalanceResponse);

                getView().setBalance(usableSaldoBalanceResponse.getSaldo().getFormattedAmount());
                getView().setWithdrawButtonState(usableSaldoBalanceResponse.getSaldo().getDeposit() != 0);
                if ((holdSaldoBalanceResponse.getSaldo().getDeposit() > 0)) {
                    getView().showHoldWarning(holdSaldoBalanceResponse.getSaldo().getFormattedAmount());
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
    }

    private void onDepositSummaryFetched(GraphqlResponse graphqlResponse) {
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
    }

    @Override
    public void onRefresh() {
        paging.resetPage();
        getSummaryDeposit();
    }


    private Map<String, Object> getSummaryDepositParam() {
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
        return param.getParamSummaryDeposit();

    }

    private String getDateParam(String date) {
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
    }


    private void setData(GqlDepositSummaryResponse data) {
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
    }


    @Override
    public void onDrawClicked(Intent intent) {
        if (!isViewAttached()) {
            return;
        }
        Context context = getView().getContext();
        UserSessionInterface userSession = new UserSession(context);
        if (userSession.hasPassword()) {
            depositCacheInteractor.getUsableSaldoBalanceCache(new DepositCacheInteractor.GetUsableSaldoBalanceCacheListener() {
                @Override
                public void onSuccess(GqlSaldoBalanceResponse result) {

                    if (result.getSaldo().getDeposit() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(BUNDLE_TOTAL_BALANCE, String.valueOf(result.getSaldo().getFormattedAmount()));
                        bundle.putString(BUNDLE_TOTAL_BALANCE_INT, String.valueOf(result.getSaldo().getDeposit()));
                        intent.putExtras(bundle);
                        getView().getActivity().startActivityForResult(intent, REQUEST_WITHDRAW_CODE);
                    } else {
                        getView().showErrorMessage(getView().getString(R.string.sp_error_no_amount_deposit));
                    }
                }

                @Override
                public void onError(Throwable e) {

                }
            });
        } else {
            getView().showWithdrawalNoPassword();
        }
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().getAdapter().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().getAdapter().hideLoading();
            getView().finishLoading();
        }
    }


    private boolean canLoadMore() {
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

    @Override
    public void finishLoading() {
        if (!isViewAttached()) {
            return;
        }
        getView().finishLoading();
    }

}
