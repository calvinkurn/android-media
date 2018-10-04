package com.tokopedia.tokocash.historytokocash.presentation.contract;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tokocash.balance.view.BalanceTokoCash;

/**
 * Created by nabillasabbaha on 2/6/18.
 */

public interface HomeTokoCashContract {

    interface View extends CustomerView {
        void showProgressLoading();

        void hideProgressLoading();

        void showErrorMessage();

        void renderBalanceTokoCash(BalanceTokoCash balanceTokoCash);

        void showEmptyPage(Throwable throwable);

        void addAutoSweepFragment(Bundle bundle);

        void navigatePageToActivateTokocash();

        void navigateToLoginPage();
    }

    interface Presenter extends CustomerPresenter<View> {
        void getHistoryTokocashForRefreshingTokenWallet();

        void processGetBalanceTokoCash();

        void onDestroyView();
    }
}
