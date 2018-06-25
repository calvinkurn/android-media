package com.tokopedia.paymentmanagementsystem.changebankaccount.view;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 6/25/18.
 */

public class ChangeBankAccountFragment extends BaseDaggerFragment {

    @Inject
    ChangeBankAccountPresenter changeBankAccountPresenter;

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
