package com.tokopedia.saldodetails.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.contract.SaldoDetailContract;
import com.tokopedia.saldodetails.deposit.listener.MerchantSaldoDetailsActionListener;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractor;
import com.tokopedia.saldodetails.interactor.DepositCacheInteractorImpl;
import com.tokopedia.saldodetails.interactor.DepositRetrofitInteractor;
import com.tokopedia.saldodetails.interactor.DepositRetrofitInteractorImpl;
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse;
import com.tokopedia.saldodetails.response.model.SummaryDepositParam;
import com.tokopedia.saldodetails.response.model.SummaryWithdraw;
import com.tokopedia.saldodetails.subscriber.GetMerchantSaldoDetailsSubscriber;
import com.tokopedia.saldodetails.usecase.GetMerchantSaldoDetails;
import com.tokopedia.saldodetails.usecase.GetSaldoSummaryUseCase;
import com.tokopedia.saldodetails.usecase.SetMerchantSaldoStatus;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.inject.Inject;

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

    DepositRetrofitInteractor networkInteractor;
    PagingHandler paging;
    DepositCacheInteractor depositCacheInteractor;


    @Inject
    public SaldoDetailsPresenter(@ApplicationContext Context context,
                                 @NonNull SetMerchantSaldoStatus setMerchantSaldoStatus,
                                 GetSaldoSummaryUseCase getSaldoSummaryUseCase) {
        this.setMerchantSaldoStatusUseCase = setMerchantSaldoStatus;
        depositCacheInteractor = new DepositCacheInteractorImpl(context);
        this.paging = new PagingHandler();
        networkInteractor = new DepositRetrofitInteractorImpl(context, getSaldoSummaryUseCase);
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
            public void onSuccess(SummaryWithdraw cache) {
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
    public void getSummaryDeposit() {
        getView().removeError();
        if (isValid()) {
            showLoading();
            getView().setActionsEnabled(false);
            networkInteractor.getSummaryDeposit(getView().getActivity(), getSummaryDepositParam(), new DepositRetrofitInteractor.DepositListener() {
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
                    if (getView().getAdapter().getItemCount() == 0) {
                        getView().showEmptyState();
                    } else {
                        getView().setRetry();
                    }
                }

            });
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

        param.setPage(String.valueOf(paging.getPage()));
        return param.getParamSummaryDeposit();

    }

    private String getDateParam(String date) {
        return date.replace("/", "");
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


    private void setData(SummaryWithdraw data) {
        getView().setBalance(data.getSummary().getSummaryUseableDepositIdr());
        if ((data.getSummary().getSummaryHoldDeposit()) > 0) {
            getView().showHoldWarning(data.getWarningHoldDeposit());
        } else {
            getView().hideWarning();
        }
        getView().getAdapter().addElement(data.getList());

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
            depositCacheInteractor.getSummaryDepositCache(new DepositCacheInteractor.GetSummaryDepositCacheListener() {
                @Override
                public void onSuccess(SummaryWithdraw result) {
                    if (result.getSummary().getSummaryUseableDeposit() > 0) {
                        Bundle bundle = new Bundle();
                        bundle.putString(BUNDLE_TOTAL_BALANCE, String.valueOf(result.getSummary().getSummaryUseableDepositIdr()));
                        bundle.putString(BUNDLE_TOTAL_BALANCE_INT, String.valueOf(result.getSummary().getSummaryUseableDeposit()));
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
        return !networkInteractor.isRequesting();
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
    public void showSaldoPrioritasFragment(GqlMerchantSaldoDetailsResponse.Details sellerDetails) {
        getView().showSaldoPrioritasFragment(sellerDetails);
    }

    @Override
    public void finishLoading() {
        getView().finishLoading();
    }
}
