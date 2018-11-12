package com.tokopedia.useridentification.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.useridentification.view.viewmodel.UserIdentificationStepperModel;

/**
 * @author by alvinatin on 02/11/18.
 */

public class UserIdentificationFormKtpFragment extends BaseUserIdentificationStepperFragment<UserIdentificationStepperModel> {

    public static Fragment createInstance(){
        Fragment fragment = new UserIdentificationFormKtpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void setContentView() {

    }
}
