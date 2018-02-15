package com.tokopedia.tkpd.flight.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;

/**
 * @author by alvarisi on 2/15/18.
 */

public class FlightPhoneVerificationActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightPhoneVerificationActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return PhoneVerificationFragment.createInstance
                (getPhoneVerificationListener(), false);
    }

    private PhoneVerificationFragment.PhoneVerificationFragmentListener getPhoneVerificationListener() {
        return new PhoneVerificationFragment.PhoneVerificationFragmentListener() {
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
        };
    }
}
