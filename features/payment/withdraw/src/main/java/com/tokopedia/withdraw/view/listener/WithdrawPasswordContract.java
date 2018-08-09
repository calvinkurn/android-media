package com.tokopedia.withdraw.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.withdraw.view.viewmodel.BankAccountViewModel;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPasswordContract {

    public interface View extends CustomerView {

        Context getContext();


    }

    public interface Presenter extends CustomerPresenter<View> {

        void doWithdraw(int withdrawal, BankAccountViewModel o, String password);
    }
}
