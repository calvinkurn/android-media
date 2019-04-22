package com.tokopedia.loginphone.verifyotptokocash.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.ChooseTokocashVerificationMethodFragment;
import com.tokopedia.loginphone.verifyotptokocash.view.fragment.TokoCashVerificationFragment;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.loginphone.R;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;

/**
 * @author by nisie on 11/29/17.
 */
@Deprecated
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

    @Override
    public void goToSelectVerificationMethod() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                ChooseTokocashVerificationMethodFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = ChooseTokocashVerificationMethodFragment.createInstance(passModel);
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

//    /**
//     * @param context            either activity context or fragment context
//     * @param phoneNumber        user phone number
//     * @param canUseOtherMethod  set true if user can use other method for otp.
//     * @param defaultRequestMode default mode (sms/etc).Use MODE from@see{@link RequestOtpUseCase}.
//     * @return Intent
//     */
//    public static Intent getCallingIntent(Context context, String phoneNumber,
//                                          boolean canUseOtherMethod,
//                                          String defaultRequestMode) {
//        VerificationPassModel passModel = new VerificationPassModel(phoneNumber,
//                RequestOtpUseCase.OTP_TYPE_TOKOCASH,
//                canUseOtherMethod);
//
//        Intent intent = new Intent(context, TokoCashOtpActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(PASS_MODEL, passModel);
//        bundle.putString(PARAM_REQUEST_OTP_MODE, defaultRequestMode);
//        bundle.putBoolean(IS_SHOW_CHOOSE_METHOD, false);
//        intent.putExtras(bundle);
//        return intent;
//    }


    /**
     * @param methodItem should be from {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     *                   Use this for dynamic otp.
     */
    public void goToVerificationPage(MethodItem methodItem) {
        if (!(getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                TokoCashVerificationFragment)) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = TokoCashVerificationFragment.createInstance(createDynamicBundle(methodItem));
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }
}
