package com.tokopedia.saldodetails.contract;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.saldodetails.response.model.GqlDetailsResponse;
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditResponse;

public interface SaldoDetailContract {
    interface View extends CustomerView {
        Context getContext();

        Activity getActivity();

        void showErrorMessage(String s);

        String getString(int resId);

        void hideWarning();

        void refresh();

        void showEmptyState();

        void setRetry();

        void setRetry(String error);

        float getSellerSaldoBalance();

        float getBuyerSaldoBalance();

        void showWithdrawalNoPassword();

        void setBalance(long totalBalance, String summaryUsableDepositIdr);

        void setWithdrawButtonState(boolean state);

        void showHoldWarning(String warningText);

        void setBuyerSaldoBalance(float amount, String text);

        void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails);

        void hideSaldoPrioritasFragment();

        void hideUserFinancialStatusLayout();

        void hideMerchantCreditLineFragment();

        void showMerchantCreditLineFragment(GqlMerchantCreditResponse response);

        void showTickerMessage(String withdrawalTicker);

        void hideTickerMessage();

        int getLateCount(int count);

        boolean isSellerEnabled();

        void showSellerSaldoRL();

        void setSellerSaldoBalance(float amount, String formattedAmount);

        void showBuyerSaldoRL();

    }

    interface Presenter extends CustomerPresenter<SaldoDetailContract.View> {
        void getSaldoBalance();

        void getTickerWithdrawalMessage();

        void getMerchantSaldoDetails();

        void getUserFinancialStatus();

        void getMerchantCreditLineDetails();

        void onDrawClicked(Intent intent, int statusWithDrawLock,int mclLateCount);

        void getMCLLateCount();
    }
}
