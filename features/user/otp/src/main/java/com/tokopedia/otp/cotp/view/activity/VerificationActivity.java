package com.tokopedia.otp.cotp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.common.di.DaggerOtpComponent;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.cotp.view.viewlistener.Verification;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author by nisie on 11/29/17.
 */

public class VerificationActivity extends BaseSimpleActivity {

    public static final String PASS_MODEL = "VerificationPassModel";

    public static final String PARAM_REQUEST_OTP_MODE = "fragmentType";

    protected static final String FIRST_FRAGMENT_TAG = "first";
    protected static final String CHOOSE_FRAGMENT_TAG = "choose";
    protected static final String IS_SHOW_CHOOSE_METHOD = "is_show_choose_method";
    protected static final String REGEX_MASK_PHONE_NUMBER =
            "(0...|62...|\\+62...)(\\d{3,4})(\\d{3,4})(\\d{0,4})";

    protected VerificationPassModel passModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
    }

    protected void initInjector() {
        OtpComponent otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication())
                        .getBaseAppComponent()).build();

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this);

    }

    @Override
    protected void setupFragment(Bundle savedInstance) {
        setupPassdata();
        inflateFragment();
    }

    @Override
    protected void inflateFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (passModel.canUseOtherMethod()
                && getIntent().getExtras()!= null
                && getIntent().getExtras().getBoolean(IS_SHOW_CHOOSE_METHOD, true)) {
            fragment = ChooseVerificationMethodFragment.createInstance(passModel);
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

    protected void setupPassdata() {
        VerificationPassModel tempPassModel = getIntent().getParcelableExtra(PASS_MODEL);
        if (tempPassModel != null) {
            passModel = tempPassModel;
        } else {
            Log.d(VerificationActivity.class.getSimpleName(), "Error no pass data");
            finish();
        }
    }

    protected Fragment getDefaultFragment(String mode, VerificationPassModel passModel) {
        Fragment fragment;
        switch (mode) {
            case RequestOtpUseCase.MODE_SMS: {
                String phoneNumber = passModel.getPhoneNumber();
                int otpType = passModel.getOtpType();
                fragment = VerificationFragment.createInstance(createSmsBundle(phoneNumber, otpType));
                break;
            }
            case RequestOtpUseCase.MODE_EMAIL: {
                String email = passModel.getEmail();
                fragment = VerificationFragment.createInstance(createEmailBundle(email));
                break;
            }
            default: {
                fragment = ChooseVerificationMethodFragment.createInstance(passModel);
                break;
            }
        }
        return fragment;
    }

    @Override
    public String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT;
    }

    public void goToSelectVerificationMethod() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                ChooseVerificationMethodFragment)) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = ChooseVerificationMethodFragment.createInstance(passModel);
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0,
                    R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    /**
     * @param methodItem should be from {@link com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment}
     *                   Use this for dynamic otp.
     */
    public void goToVerificationPage(MethodItem methodItem) {
        if (!(getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                VerificationFragment)) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = VerificationFragment.createInstance(createDynamicBundle(methodItem));
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    protected VerificationViewModel createSmsBundle(String phoneNumber, int otpType) {

        return new VerificationViewModel(
                passModel.getPhoneNumber(),
                passModel.getEmail(),
                passModel.getOtpType(),
                RequestOtpUseCase.MODE_SMS,
                R.drawable.ic_verification_sms,
                createSmsMessage(phoneNumber, otpType),
                OTPAnalytics.Screen.SCREEN_COTP_SMS,
                passModel.canUseOtherMethod()
        );
    }

    private VerificationViewModel createEmailBundle(String email) {

        return new VerificationViewModel(
                passModel.getPhoneNumber(),
                passModel.getEmail(),
                passModel.getOtpType(),
                RequestOtpUseCase.MODE_EMAIL,
                R.drawable.ic_verification_email,
                createEmailMessage(email),
                OTPAnalytics.Screen.SCREEN_COTP_EMAIL,
                passModel.canUseOtherMethod()
        );
    }

    protected VerificationViewModel createDynamicBundle(MethodItem methodItem) {


        return new VerificationViewModel(
                passModel.getPhoneNumber(),
                passModel.getEmail(),
                passModel.getOtpType(),
                methodItem.getModeName(),
                methodItem.getImageUrl(),
                methodItem.getVerificationText(),
                getDynamicAppScreen(methodItem.getModeName()),
                passModel.canUseOtherMethod()
        );
    }

    private String getDynamicAppScreen(String mode) {
        return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT + mode;
    }

    private String getMaskedPhone(String phoneNumber, int otpType) {
        if (otpType == RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION) {
            return MethodItem.getMaskedPhoneNumber(phoneNumber);
        } else {
            final Pattern pattern = Pattern.compile(REGEX_MASK_PHONE_NUMBER);
            final Matcher matcher = pattern.matcher(phoneNumber);
            String masked = matcher.replaceAll("$1-$2-$3-$4");

            if (masked.endsWith("-")) {
                masked = masked.substring(0, masked.length() - 1);
            }
            return masked;
        }
    }

    private String createSmsMessage(String phoneNumber, int otpType) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return getString(R.string.verification_code_sms_sent_to) + "<br/>" + getMaskedPhone
                    (phoneNumber, otpType);
        } else {
            return "";
        }
    }

    private String createEmailMessage(String email) {
        if (!TextUtils.isEmpty(email)) {
            return getString(R.string.verification_code_email_sent_to)
                    + "<br/><b>" + email + "</b>";
        } else {
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }

        if (getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                VerificationFragment) {

            ((Verification.View) getSupportFragmentManager().findFragmentById(R.id.parent_view))
                    .trackOnBackPressed();
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    /**
     * Default method to go to choose verification method page.
     *
     * @param context     either activity context or fragment context
     * @param otpType     use OTP_TYPE from @see{@link RequestOtpUseCase}. If you have new otp
     *                    type, please add there.
     * @param phoneNumber user phone number
     * @param email       user email
     * @return Intent
     */
    public static Intent getShowChooseVerificationMethodIntent(Context context,
                                                               int otpType,
                                                               String phoneNumber,
                                                               String email) {

        VerificationPassModel passModel = new VerificationPassModel(
                phoneNumber, email,
                otpType, true
        );

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        bundle.putBoolean(IS_SHOW_CHOOSE_METHOD, true);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Default method to immediately go to OTP Page.
     * Default supported otp mode is only SMS and EMAIL. If you want to add other method, please
     * add a case in {@link #getDefaultFragment}
     *
     * @param context            either activity context or fragment context
     * @param phoneNumber        user phone number
     * @param email              user email
     * @param otpType            use OTP_TYPE from @see{@link RequestOtpUseCase}. If you have new otp
     *                           type, please add there.
     * @param canUseOtherMethod  set true if user can use other method for otp.
     * @param defaultRequestMode default mode (sms/email).Use MODE
     *                           from@see{@link RequestOtpUseCase}.
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, String phoneNumber, String email,
                                          int otpType, boolean canUseOtherMethod,
                                          String defaultRequestMode) {
        VerificationPassModel passModel = new VerificationPassModel(
                phoneNumber,
                email,
                otpType,
                canUseOtherMethod);

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        bundle.putString(PARAM_REQUEST_OTP_MODE, defaultRequestMode);
        bundle.putBoolean(IS_SHOW_CHOOSE_METHOD, false);
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * Only use this method when you don't need OTP to email.
     * Default supported otp mode is only SMS. If you want to add other method, please add a case
     * in {@link #getDefaultFragment}
     *
     * @param context            either activity context or fragment context
     * @param phoneNumber        user phone number
     * @param otpType            use OTP_TYPE from @see{@link RequestOtpUseCase}. If you have new otp
     *                           type, please add there.
     * @param canUseOtherMethod  set true if user can use other method for otp.
     * @param defaultRequestMode default mode (sms/etc).Use MODE from@see{@link RequestOtpUseCase}.
     * @return Intent
     */
    public static Intent getCallingIntent(Context context, String phoneNumber, int otpType,
                                          boolean canUseOtherMethod,
                                          String defaultRequestMode) {
        VerificationPassModel passModel = new VerificationPassModel(phoneNumber,
                otpType,
                canUseOtherMethod);

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PASS_MODEL, passModel);
        bundle.putString(PARAM_REQUEST_OTP_MODE, defaultRequestMode);
        bundle.putBoolean(IS_SHOW_CHOOSE_METHOD, false);
        intent.putExtras(bundle);
        return intent;
    }


}
