package com.tokopedia.saldodetails.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;

public interface SaldoDetailContract {
    interface View extends CustomerView {
        Context getContext();

        /*void setStartDate(String startDate);

        void setEndDate(String endDate);

        String getStartDate();

        String getEndDate();*/

        void showSaldoBalanceSeparator();

//        Visitable getDefaultEmptyViewModel();

        Activity getActivity();

//        void finishLoading();

//        SaldoDepositAdapter getAdapter();

        void showErrorMessage(String s);

//        void showInvalidDateError(String s);

        String getString(int resId);

//        void removeError();

        void hideWarning();

//        void setActionsEnabled(Boolean isEnabled);

//        void setLoading();

        void refresh();

        void showEmptyState();

        void setRetry();

//        void showEmptyState(String error);

        void setRetry(String error);

        void showWithdrawalNoPassword();

        void setBalance(String summaryUsableDepositIdr);

        void setWithdrawButtonState(boolean state);

        void showHoldWarning(String warningText);

        void setBuyerSaldoBalance(String text);

        void setSellerSaldoBalance(String formattedAmount);

        void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails);

        void hideSaldoPrioritasFragment();

        void showTickerMessage(String withdrawalTicker);

        void hideTickerMessage();

        boolean isSellerEnabled();

        void hideSellerSaldoRL();

        void showSellerSaldoRL();

        void hideBuyerSaldoRL();

        void showBuyerSaldoRL();

        void hideSaldoBalanceSeparator();
    }

    interface Presenter extends CustomerPresenter<SaldoDetailContract.View> {
//        void setFirstDateParameter();

//        void setCache();

        void getSaldoBalance();

        void getTickerWithdrawalMessage();

        void getMerchantSaldoDetails();

//        void onSearchClicked();

//        void onEndDateClicked(SaldoDatePickerUtil datePicker);

//        void onStartDateClicked(SaldoDatePickerUtil datePicker);

//        void loadMore(int lastItemPosition, int visibleItem);

        void onDrawClicked(Intent intent);

//        void getSummaryDeposit();

        void onRefresh();
    }
}
