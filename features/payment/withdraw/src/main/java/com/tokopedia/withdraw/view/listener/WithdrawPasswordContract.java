package com.tokopedia.withdraw.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * @author by StevenFredian on 30/07/18.
 */

public class WithdrawPasswordContract {

    public interface View extends CustomerView {

        Context getContext();


    }

    public interface Presenter extends CustomerPresenter<View> {

    }
}
