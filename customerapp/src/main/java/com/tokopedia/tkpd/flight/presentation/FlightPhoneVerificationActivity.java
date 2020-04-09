package com.tokopedia.tkpd.flight.presentation;

import android.app.Activity;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;

/**
 * @author by alvarisi on 2/15/18.
 */

public class FlightPhoneVerificationActivity extends BaseSimpleActivity implements HasComponent<AppComponent>, PhoneVerificationFragment.PhoneVerificationFragmentListener {

    @Override
    protected Fragment getNewFragment() {
        return FlightPhoneVerificationFragment.newInstance();
    }

    @Override
    public void onSkipVerification() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onSuccessVerification() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
