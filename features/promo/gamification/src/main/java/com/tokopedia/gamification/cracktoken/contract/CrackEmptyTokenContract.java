package com.tokopedia.gamification.cracktoken.contract;

import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by nabillasabbaha on 4/2/18.
 */

public interface CrackEmptyTokenContract {

    interface View extends CustomerView {

        void updateRewards(int points, int coupons, int loyalty);

        Resources getResources();
    }

    interface Presenter extends CustomerPresenter<View> {
    }
}
