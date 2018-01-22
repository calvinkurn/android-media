package com.tokopedia.tkpd.campaign.view;


import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by sandeepgoyal on 18/12/17.
 */

public interface BarCodeScannerContract {

    public interface View extends CustomerView {
        String getBarCodeData();
        public void finish();
        void startActivity(Intent intent);
    }

    public interface Presenter extends CustomerPresenter<View> {
        public void onBarCodeScanComplete();
    }
}
