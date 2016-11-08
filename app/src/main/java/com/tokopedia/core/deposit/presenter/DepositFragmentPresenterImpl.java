package com.tokopedia.core.deposit.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.ui.utilities.DatePickerUtil;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.R;
import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.deposit.activity.WithdrawActivity;
import com.tokopedia.core.deposit.fragment.DepositFragment;
import com.tokopedia.core.deposit.interactor.DepositCacheInteractor;
import com.tokopedia.core.deposit.interactor.DepositCacheInteractorImpl;
import com.tokopedia.core.deposit.interactor.DepositRetrofitInteractor;
import com.tokopedia.core.deposit.interactor.DepositRetrofitInteractorImpl;
import com.tokopedia.core.deposit.listener.DepositFragmentView;
import com.tokopedia.core.deposit.model.SummaryWithdraw;
import com.tokopedia.core.deposit.model.SummaryDepositParam;
import com.tokopedia.core.util.PagingHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by Nisie on 3/30/16.
 */
public class DepositFragmentPresenterImpl implements DepositFragmentPresenter {

    public static final String BUNDLE_TOTAL_BALANCE = "total_balance";
    public static final String BUNDLE_TOTAL_BALANCE_INT = "total_balance_int";
    private static final java.lang.String DATE_FORMAT_VIEW = "dd/MM/yyyy";
    private static final java.lang.String DATE_FORMAT_WS = "yyyy/MM/dd";
    public static final int REQUEST_WITHDRAW_CODE = 1;
    DepositFragmentView viewListener;
    DepositRetrofitInteractor networkInteractor;
    PagingHandler paging;
    DepositCacheInteractor depositCacheInteractor;

    String paramStartDate;
    String paramEndDate;

    public DepositFragmentPresenterImpl(DepositFragment viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new DepositRetrofitInteractorImpl();
        this.paging = new PagingHandler();
        this.depositCacheInteractor = new DepositCacheInteractorImpl();
    }

    @Override
    public void onDrawClicked() {

        depositCacheInteractor.getSummaryDepositCache(new DepositCacheInteractor.GetSummaryDepositCacheListener() {
            @Override
            public void onSuccess(SummaryWithdraw result) {
                if (result.getSummary().getSummaryUseableDeposit() > 0) {
                    Intent intent = new Intent(viewListener.getContext(), WithdrawActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BUNDLE_TOTAL_BALANCE, String.valueOf(result.getSummary().getSummaryUseableDepositIdr()));
                    bundle.putString(BUNDLE_TOTAL_BALANCE_INT, String.valueOf(result.getSummary().getSummaryUseableDeposit()));
                    intent.putExtras(bundle);
                    viewListener.getActivity().startActivityForResult(intent, REQUEST_WITHDRAW_CODE);
                } else {
                    viewListener.showErrorMessage(viewListener.getString(R.string.error_no_amount_deposit));
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(DepositFragmentPresenterImpl.class.getSimpleName(), e.toString());
            }
        });

    }

    @Override
    public void onSearchClicked() {
        viewListener.getAdapter().getList().clear();
        paramStartDate = viewListener.getStartDate();
        paramEndDate = viewListener.getEndDate();
        paging.resetPage();
        getSummaryDeposit();
    }

    @Override
    public void onEndDateClicked(DatePickerUtil datePicker) {
        String date = viewListener.getEndDate();
        datePicker.SetDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int day) {
                viewListener.setEndDate(checkNumber(day) + "/" + checkNumber(month) + "/" + checkNumber(year));

            }
        });

    }

    @Override
    public void onStartDateClicked(DatePickerUtil datePicker) {
        String date = viewListener.getStartDate();
        datePicker.SetDate(getDay(date), getStartMonth(date), getStartYear(date));
        datePicker.DatePickerCalendar(new DatePickerUtil.onDateSelectedListener() {

            @Override
            public void onDateSelected(int year, int month, int day) {
                viewListener.setStartDate(checkNumber(day) + "/" + checkNumber(month) + "/" + checkNumber(year));

            }
        });
    }

    @Override
    public void getSummaryDeposit() {
        viewListener.removeError();
        if (isValid()) {
            showLoading();
            viewListener.setActionsEnabled(false);
            networkInteractor.getSummaryDeposit(viewListener.getActivity(), getSummaryDepositParam(), new DepositRetrofitInteractor.DepositListener() {
                @Override
                public void onSuccess(@NonNull SummaryWithdraw data) {
                    viewListener.finishLoading();
                    viewListener.setActionsEnabled(true);

                    if (!data.isErrorDate()) {
                        if (paging.getPage() == 1) {
                            viewListener.getAdapter().getList().clear();
                            depositCacheInteractor.setSummaryDepositCache(data);
                        }
                        paging.setHasNext(PagingHandler.CheckHasNext(data.getPaging()));
                        setData(data);

                    } else {
                        onError(viewListener.getString(R.string.title_max_day));
                    }

                }

                @Override
                public void onTimeout(String message) {
                    viewListener.finishLoading();
                    if (viewListener.getAdapter().getList().size() == 0) {
                        viewListener.showEmptyState();
                    } else {
                        viewListener.setRetry();
                    }
                }

                @Override
                public void onError(String error) {
                    viewListener.finishLoading();
                    viewListener.setActionsEnabled(true);

                    if (viewListener.getAdapter().getList().size() == 0) {
                        viewListener.showEmptyState(error);
                    } else {
                        viewListener.setRetry(error);
                    }
                }

                @Override
                public void onNullData() {
                    viewListener.finishLoading();
                    viewListener.setActionsEnabled(true);
                    viewListener.getAdapter().showEmpty(true);
                }

                @Override
                public void onNoNetworkConnection() {
                    viewListener.finishLoading();
                    if (viewListener.getAdapter().getList().size() == 0) {
                        viewListener.showEmptyState();
                    } else {
                        viewListener.setRetry();
                    }
                }

            });
        } else {
            viewListener.finishLoading();
        }
    }

    private void showLoading() {
        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.setLoading();
        } else if(paging.getPage() == 1){
            viewListener.showRefreshing();
        }else{
            viewListener.getAdapter().showLoading(true);
        }
    }

    @Override
    public void onRefresh() {
        paging.resetPage();
        getSummaryDeposit();
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
        viewListener.setStartDate(startDate);
        viewListener.setEndDate(endDate);
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
    public void loadMore(int lastItemPosition, int visibleItem) {
        if (paging.CheckNextPage()
                && isOnLastPosition(lastItemPosition, visibleItem)
                && canLoadMore()) {
            paging.nextPage();
            getSummaryDeposit();

        }

    }

    @Override
    public RetryDataBinder.OnRetryListener onRetry() {
        return new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                getSummaryDeposit();
            }
        };
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private boolean canLoadMore() {
        return !networkInteractor.isRequesting();
    }

    private boolean isOnLastPosition(int lastItemPosition, int visibleItem) {
        return lastItemPosition == visibleItem;
    }

    private void setData(SummaryWithdraw data) {
        viewListener.setBalance(data.getSummary().getSummaryUseableDepositIdr());
        if ((data.getSummary().getSummaryHoldDeposit()) > 0) {
            viewListener.showHoldWarning(data.getSummary().getSummaryHoldDepositIdr());
        } else {
            viewListener.hideWarning();
        }
        viewListener.getAdapter().addList(data.getList());

        if (viewListener.getAdapter().getList().size() == 0) {
            viewListener.getAdapter().showEmpty(true);
        }

        if (paging.CheckNextPage()) {
            viewListener.getAdapter().showLoading(true);
        }else{
            viewListener.getAdapter().showLoading(false);
        }
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
                viewListener.showErrorMessage(viewListener.getString(R.string.error_invalid_date));
            }

            if ((endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000) > 31) {
                isValid = false;
                viewListener.showErrorMessage(viewListener.getString(R.string.title_max_day));
            }
        } catch (ParseException e) {
            Log.e(DepositFragmentPresenterImpl.class.getSimpleName(), e.toString());
            isValid = false;
            viewListener.showErrorMessage(viewListener.getString(R.string.error_invalid_date));
        }
        return isValid;
    }

    private Map<String, String> getSummaryDepositParam() {
        SummaryDepositParam param = new SummaryDepositParam();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_VIEW);
        SimpleDateFormat sdf_ws = new SimpleDateFormat(DATE_FORMAT_WS);
        try {
            Date formattedStart = sdf.parse(paramStartDate);
            Date formattedEnd = sdf.parse(paramEndDate);
            param.setStartDate(getDateParam(sdf_ws.format(formattedStart)));
            param.setEndDate(getDateParam(sdf_ws.format(formattedEnd)));
        } catch (ParseException e) {
            viewListener.showErrorMessage(viewListener.getString(R.string.error_invalid_date));
        }

        param.setPage(String.valueOf(paging.getPage()));
        return param.getParamSummaryDeposit();

    }

    private String getDateParam(String date) {
        return date.replace("/", "");
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


}
