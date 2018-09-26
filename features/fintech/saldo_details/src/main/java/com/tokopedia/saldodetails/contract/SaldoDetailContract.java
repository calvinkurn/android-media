package com.tokopedia.saldodetails.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

public interface SaldoDetailContract {
    interface View extends CustomerView {
        void showProgressLoading();

        void hideProgressLoading();

        Context getContext();

        void setStartDate(String startDate);

        void setEndDate(String endDate);

        String getStartDate();

        String getEndDate();

        Activity getActivity();

        void finishLoading();

        SaldoDepositAdapter getAdapter();

        void showErrorMessage(String s);

        String getString(int resId);

        void removeError();

        void hideWarning();

        void setActionsEnabled(Boolean isEnabled);

        boolean isRefreshing();

        void setLoading();

        void showRefreshing();

        void refresh();

        void showEmptyState();

        void setRetry();

        void showEmptyState(String error);

        void setRetry(String error);

        void showWithdrawalNoPassword();

        void setBalance(String summaryUseableDepositIdr);

        void showHoldWarning(String warningText);

    }

    interface Presenter extends CustomerPresenter<SaldoDetailContract.View> {
        void onDestroyView();

        void setFirstDateParameter();

        void setCache();

        void onSearchClicked();

        void onEndDateClicked(SaldoDatePickerUtil datePicker);

        void onStartDateClicked(SaldoDatePickerUtil datePicker);

        void loadMore(int lastItemPosition, int visibleItem);

        void onDrawClicked(Intent intent);

        void getSummaryDeposit();

        void onRefresh();
    }
}
