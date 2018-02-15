package com.tokopedia.tkpd.flight.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;

/**
 * @author by alvarisi on 2/15/18.
 */

public class FlightPhoneVerificationActivity extends BaseSimpleActivity implements HasComponent<AppComponent>, PhoneVerificationFragment.PhoneVerificationFragmentListener {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightPhoneVerificationActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightPhoneVerificationFragment.newInstance();
    }

    @Override
    public void onSkipVerification() {
        FlightPhoneVerificationActivity.this.setResult(Activity.RESULT_CANCELED);
        FlightPhoneVerificationActivity.this.finish();
    }

    @Override
    public void onSuccessVerification() {
        FlightPhoneVerificationActivity.this.setResult(Activity.RESULT_OK);
        FlightPhoneVerificationActivity.this.finish();
    }

    @Override
    public AppComponent getComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }
}
