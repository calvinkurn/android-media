package com.tokopedia.saldodetails.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.saldodetails.adapter.SaldoDepositAdapter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.util.SaldoDatePickerUtil;

public interface SaldoDetailContract {
    interface View extends CustomerView {
        Context getContext();

        void setStartDate(String startDate);

        void setEndDate(String endDate);

        String getStartDate();

        String getEndDate();

        Visitable getDefaultEmptyViewModel();

        Activity getActivity();

        void finishLoading();

        SaldoDepositAdapter getAdapter();

        void showErrorMessage(String s);

        void showInvalidDateError(String s);

        String getString(int resId);

        void removeError();

        void hideWarning();

        void setActionsEnabled(Boolean isEnabled);

        void setLoading();

        void refresh();

        void showEmptyState();

        void setRetry();

        void showEmptyState(String error);

        void setRetry(String error);

        void showWithdrawalNoPassword();

        void setBalance(String summaryUseableDepositIdr);

        void showHoldWarning(String warningText);

        void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails);

        void hideSaldoPrioritasFragment();
    }

    interface Presenter extends CustomerPresenter<SaldoDetailContract.View> {
        void onDestroyView();

        void setFirstDateParameter();

        void setCache();

        void getSaldoBalance();

        void getMerchantSaldoDetails();

        void onSearchClicked();

        void onEndDateClicked(SaldoDatePickerUtil datePicker);

        void onStartDateClicked(SaldoDatePickerUtil datePicker);

        void loadMore(int lastItemPosition, int visibleItem);

        void onDrawClicked(Intent intent);

        void getSummaryDeposit();

        void onRefresh();
    }
}
