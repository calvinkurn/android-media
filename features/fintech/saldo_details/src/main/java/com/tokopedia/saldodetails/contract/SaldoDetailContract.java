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

        void showSaldoBalanceSeparator();

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

        float getTotalSaldoBalance();

        void showWithdrawalNoPassword();

        void setBalance(float totalBalance, String summaryUsableDepositIdr);

        void setWithdrawButtonState(boolean state);

        void showHoldWarning(String warningText);

        void setBuyerSaldoBalance(float amount, String text);

        void showSaldoPrioritasFragment(GqlDetailsResponse sellerDetails);

        void hideSaldoPrioritasFragment();

        void showTickerMessage(String withdrawalTicker);

        void hideTickerMessage();

        boolean isSellerEnabled();

        void hideSellerSaldoRL();

        void showSellerSaldoRL();

        void setSellerSaldoBalance(float amount, String formattedAmount);

        void hideBuyerSaldoRL();

        void showBuyerSaldoRL();

        void hideSaldoBalanceSeparator();
    }

    interface Presenter extends CustomerPresenter<SaldoDetailContract.View> {
        void getSaldoBalance();
        void getTickerWithdrawalMessage();
        void getMerchantSaldoDetails();
        void onDrawClicked(Intent intent);
        void onRefresh();
    }
}
