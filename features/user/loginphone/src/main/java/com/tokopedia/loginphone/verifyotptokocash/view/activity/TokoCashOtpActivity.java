package com.tokopedia.loginphone.verifyotptokocash.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.ChooseTokocashVerificationMethodFragment;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.loginphone.R;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;

/**
 * @author by nisie on 11/29/17.
 */

public class TokoCashOtpActivity extends VerificationActivity {


    @Override
    protected void initInjector() {

    }

    @Override
    protected void inflateFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (passModel.canUseOtherMethod()
                && getIntent().getExtras()!= null
                && getIntent().getExtras().getBoolean(IS_SHOW_CHOOSE_METHOD, true)) {
            fragment = ChooseTokocashVerificationMethodFragment.createInstance(passModel);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        } else {
            fragment = getDefaultFragment(
                    getIntent().getExtras().getString(PARAM_REQUEST_OTP_MODE, ""),
                    passModel);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        }

        fragmentTransaction.commit();
    }

    @Override
    protected Fragment getDefaultFragment(String mode, VerificationPassModel passModel) {
        if (mode.equals(RequestOtpUseCase.MODE_SMS)) {
            String phoneNumber = passModel.getPhoneNumber();
            return TokoCashVerificationFragment.createInstance(createSmsBundle(phoneNumber,
                    RequestOtpUseCase.OTP_TYPE_TOKOCASH));
        } else {
            return super.getDefaultFragment(mode, passModel);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}
