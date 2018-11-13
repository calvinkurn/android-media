package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.deposit.listener.MerchantSaldoDetailsActionListener;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractor;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractorImpl;
import com.tokopedia.saldodetails.response.model.GqlDepositSummaryResponse;
import com.tokopedia.saldodetails.response.model.GqlHoldSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlSaldoBalanceResponse;
import com.tokopedia.saldodetails.response.model.SummaryDepositParam;
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber;
import com.tokopedia.saldodetails.usecase.GetDepositSummaryUseCase;
import com.tokopedia.saldodetails.usecase.GetMerchantSaldoDetails;
import com.tokopedia.saldodetails.usecase.GetSaldoBalanceUseCase;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

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

    private SetMerchantSaldoStatus setMerchantSaldoStatusUseCase;
    private static final java.lang.String DATE_FORMAT_VIEW = "dd/MM/yyyy";
    public static final int REQUEST_WITHDRAW_CODE = 1;
    private String paramStartDate;
    private String paramEndDate;
    public static final String BUNDLE_TOTAL_BALANCE = "total_balance";
    public static final String BUNDLE_TOTAL_BALANCE_INT = "total_balance_int";
    private static final java.lang.String DATE_FORMAT_WS = "yyyy/MM/dd";

    PagingHandler paging;
    DepositCacheInteractor depositCacheInteractor;

    @Inject
    GetDepositSummaryUseCase getDepositSummaryUseCase;
    @Inject
    GetSaldoBalanceUseCase getSaldoBalanceUseCase;

    @Inject
    public SaldoDetailsPresenter(@ApplicationContext Context context,
                                 @NonNull SetMerchantSaldoStatus setMerchantSaldoStatus) {
        this.setMerchantSaldoStatusUseCase = setMerchantSaldoStatus;
        depositCacheInteractor = new DepositCacheInteractorImpl(context);
        this.paging = new PagingHandler();
    }


    @Override
    public void onDestroyView() {
        if (setMerchantSaldoStatusUseCase != null) {
            setMerchantSaldoStatusUseCase.unsubscribe();
        }

    }

    @Override
    public void setFirstDateParameter() {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_VIEW);
        Date date = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -30);
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
        getView().getAdapter().clearAllElements();
        paramStartDate = getView().getStartDate();
        paramEndDate = getView().getEndDate();
        paging.resetPage();
        getSummaryDeposit();
    }

    @Override
    public void onEndDateClicked(SaldoDatePickerUtil datePicker) {
        String date = getView().getEndDate();
        datePicker.SetDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar(new SaldoDatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int day) {
                getView().setEndDate(checkNumber(day) + "/" + checkNumber(month) + "/" + checkNumber(year));
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onSearchClicked();
                    }
                }, 500);
            }
        });

    }

    @Override
    public void onStartDateClicked(SaldoDatePickerUtil datePicker) {
        String date = getView().getStartDate();
        datePicker.SetDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar(new SaldoDatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int day) {
                getView().setStartDate(checkNumber(day) + "/" + checkNumber(month) + "/" + checkNumber(year));
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onSearchClicked();
                    }
                }, 500);

            }
        });
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
                if (e instanceof UnknownHostException) {
                    getView().finishLoading();
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }

                } else if (e instanceof SocketTimeoutException) {
                    getView().finishLoading();
                    getView().hideRefreshing();
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }
                } else {

                    getView().finishLoading();
                    getView().setActionsEnabled(true);
                    getView().hideRefreshing();
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                    } else {
                        getView().setRetry("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                    }
                }
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {

                GqlSaldoBalanceResponse usableSaldoBalanceResponse;
                GqlHoldSaldoBalanceResponse holdSaldoBalanceResponse;

                usableSaldoBalanceResponse = graphqlResponse.getData(GqlSaldoBalanceResponse.class);
                holdSaldoBalanceResponse = graphqlResponse.getData(GqlHoldSaldoBalanceResponse.class);

                depositCacheInteractor.setUsableSaldoBalanceCache(usableSaldoBalanceResponse);

                getView().setBalance(usableSaldoBalanceResponse.getSaldo().getFormattedAmount());
                if ((holdSaldoBalanceResponse.getSaldo().getDeposit() > 0)) {

                    Toast.makeText(getView().getContext(), "Hold Warning", Toast.LENGTH_LONG).show();
//                    getView().showHoldWarning(data.getWarningHoldDeposit());
                } else {
                    getView().hideWarning();
                }
            }
        });

    }


    @Override
    public void getSummaryDeposit() {
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

                    if (e instanceof UnknownHostException) {
                        getView().finishLoading();
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }

                    } else if (e instanceof SocketTimeoutException) {
                        /*onTimeout("Timeout connection," +
                                " Mohon ulangi beberapa saat lagi");*/
                        getView().finishLoading();
                        getView().hideRefreshing();
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState();
                        } else {
                            getView().setRetry();
                        }
                    } else {

                        getView().finishLoading();
                        getView().setActionsEnabled(true);
                        getView().hideRefreshing();
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                        } else {
                            getView().setRetry("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                        }
                    }
                }

                @Override
                public void onNext(GraphqlResponse graphqlResponse) {

                    if (graphqlResponse != null &&
                            graphqlResponse.getData(GqlDepositSummaryResponse.class) != null) {

                        GqlDepositSummaryResponse gqlDepositSummaryResponse =
                                graphqlResponse.getData(GqlDepositSummaryResponse.class);

                        if (gqlDepositSummaryResponse != null &&
                                !gqlDepositSummaryResponse.getDepositActivityResponse().isHaveError()) {

                            getView().finishLoading();
                            getView().hideRefreshing();
                            getView().setActionsEnabled(true);

                            if (paging.getPage() == 1) {
                                getView().getAdapter().clearAllElements();
                                depositCacheInteractor.setSummaryDepositCache(gqlDepositSummaryResponse);
                            }
                            paging.setHasNext(gqlDepositSummaryResponse.getDepositActivityResponse().isHaveNextPage());
                            setData(gqlDepositSummaryResponse);
                        } else {
                            getView().finishLoading();
                            getView().setActionsEnabled(true);
                            getView().hideRefreshing();
                            if (gqlDepositSummaryResponse != null && gqlDepositSummaryResponse.getDepositActivityResponse() != null) {
                                if (getView().getAdapter().getItemCount() == 0) {
                                    getView().showEmptyState(gqlDepositSummaryResponse.getDepositActivityResponse().getMessage());
                                } else {
                                    getView().setRetry(gqlDepositSummaryResponse.getDepositActivityResponse().getMessage());
                                }
                            }
                        }

                    } else {
                        getView().finishLoading();
                        getView().setActionsEnabled(true);
                        getView().hideRefreshing();
                        if (getView().getAdapter().getItemCount() == 0) {
                            getView().showEmptyState("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                        } else {
                            getView().setRetry("Terjadi Kesalahan, Mohon ulangi beberapa saat lagi");
                        }

                    }
                    finishLoading();
                }
            });


            /*networkInteractor.getSummaryDeposit(getView().getActivity(), getSummaryDepositParam(), new DepositRetrofitInteractor.DepositListener() {
                @Override
                public void onSuccess(@NonNull SummaryWithdraw data) {
                    getView().finishLoading();
                    getView().hideRefreshing();
                    getView().setActionsEnabled(true);

                    if (!data.isErrorDate()) {
                        if (paging.getPage() == 1) {
                            getView().getAdapter().clearAllElements();
                            depositCacheInteractor.setSummaryDepositCache(data);
                        }
                        paging.setHasNext(PagingHandler.CheckHasNext(data.getPaging()));
                        setData(data);

                    } else {
                        onError(getView().getString(R.string.title_max_day));
                    }

                }

                @Override
                public void onTimeout(String message) {
                    getView().finishLoading();
                    getView().hideRefreshing();
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }
                }

                @Override
                public void onError(String error) {
                    getView().finishLoading();
                    getView().setActionsEnabled(true);
                    getView().hideRefreshing();
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState(error);
                    } else {
                        getView().setRetry(error);
                    }
                }

                @Override
                public void onNullData() {
                    getView().finishLoading();
                    getView().setActionsEnabled(true);
                    getView().getAdapter().addElement(getView().getDefaultEmptyViewModel());
//                    getView().getAdapter().showEmpty(true);
                }

                @Override
                public void onNoNetworkConnection() {
                    getView().finishLoading();
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }
                }

            });*/
        } else {
            getView().finishLoading();
        }
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
            getView().showErrorMessage(getView().getString(R.string.error_invalid_date));
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
            CommonUtils.dumper("NISIE " + endDate.getTime() + " - " + startDate.getTime());
            if (endDate.getTime() - startDate.getTime() < 0) {
                isValid = false;
                getView().showErrorMessage(getView().getString(R.string.error_invalid_date));
            }

            if ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000) > 31) {
                isValid = false;
                getView().showErrorMessage(getView().getString(R.string.title_max_day));
            }
        } catch (ParseException e) {
            isValid = false;
            getView().showErrorMessage(getView().getString(R.string.error_invalid_date));
        }
        return isValid;
    }


    private void setData(GqlDepositSummaryResponse data) {
        getView().getAdapter().addElement(data.getDepositActivityResponse().getDepositHistoryList());
        if (getView().getAdapter().getItemCount() == 0) {
            getView().getAdapter().addElement(getView().getDefaultEmptyViewModel());
        }
        if (paging.CheckNextPage()) {
            getView().getAdapter().showLoading();
        } else {
            getView().getAdapter().hideLoading();
        }
    }


    @Override
    public void onDrawClicked(Intent intent) {
        Context context = getView().getContext();
        UserSession session = ((AbstractionRouter) context.getApplicationContext()).getSession();
        if (session.isHasPassword()) {
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
                        getView().showErrorMessage(getView().getString(R.string.error_no_amount_deposit));
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
        if (!getView().isRefreshing() &&
                getView().getAdapter().getItemCount() == 0) {
            getView().setLoading();

        } else if (paging.getPage() == 1) {
            getView().showRefreshing();
        } else {
            getView().getAdapter().showLoading();
        }
    }


    private boolean canLoadMore() {
        return !getDepositSummaryUseCase.isRequesting();
    }

    private boolean isOnLastPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }

    private String checkNumber(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
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
        getView().hideSaldoPrioritasFragment();
    }

    @Override
    public void showSaldoPrioritasFragment(GqlMerchantSaldoDetailsResponse.Details
                                                   sellerDetails) {
        getView().showSaldoPrioritasFragment(sellerDetails);
    }

    @Override
    public void finishLoading() {
        getView().finishLoading();
    }

}
