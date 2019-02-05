package com.tokopedia.saldodetails.contract;

import android.app.Activity;
import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

public class SaldoHistoryContract {

    public interface View extends CustomerView {
        Context getContext();

        void setStartDate(String startDate);

        void setEndDate(String endDate);

        String getStartDate();

        String getEndDate();

        Visitable getDefaultEmptyViewModel();

        Activity getActivity();

        void finishLoading();

        SaldoDepositAdapter getAdapter();

        SaldoDepositAdapter getAllHistoryAdapter();

        SaldoDepositAdapter getBuyerHistoryAdapter();

        SaldoDepositAdapter getSellerHistoryAdapter();

        SaldoDepositAdapter getSingleTabAdapter();

        void showErrorMessage(String s);

        void showInvalidDateError(String s);

        String getString(int resId);

        void removeError();

        void setActionsEnabled(Boolean isEnabled);

        void setLoading();

        void refresh();

        void showEmptyState();

        void setRetry();

        void showEmptyState(String error);

        void setRetry(String error);

        boolean isSellerEnabled();

    }

    public interface Presenter extends CustomerPresenter<SaldoHistoryContract.View> {
        void setFirstDateParameter();

        void onSearchClicked();

        void onEndDateClicked(SaldoDatePickerUtil datePicker);

        void onStartDateClicked(SaldoDatePickerUtil datePicker);

        void loadMore(int lastItemPosition, int visibleItem);

        void getSummaryDeposit();

        void onRefresh();
    }
}
