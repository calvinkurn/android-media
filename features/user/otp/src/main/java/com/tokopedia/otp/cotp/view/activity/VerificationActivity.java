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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
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
import com.tokopedia.otp.cotp.view.viewmodel.VerificationViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author by nisie on 11/29/17.
 * * For navigate: use {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.COTP}
 * please pass :
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_OTP_TYPE} //refer to type in {@link RequestOtpUseCase}
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD} //true if user can pick other method
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_EMAIL}
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_MSISDN}
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE} // Use MODE from@see{@link RequestOtpUseCase}. Default is sms
 * {@link com.tokopedia.applink.internal.ApplinkConstInternalGlobal.IS_SHOW_CHOOSE_METHOD} //true if showing choose method page first
 */

public class VerificationActivity extends BaseSimpleActivity {

    public static final String PASS_MODEL = "VerificationPassModel";

    protected static final String FIRST_FRAGMENT_TAG = "first";
    protected static final String CHOOSE_FRAGMENT_TAG = "choose";
    protected static final String REGEX_MASK_PHONE_NUMBER =
            "(0...|62...|\\+62...)(\\d{3,4})(\\d{3,4})(\\d{0,4})";

    private String phoneNumber;
    private String email;
    private int otpType;
    private boolean canUseOtherMethod;
    private boolean isShowChooseMethod;
    private String requestOtpMode;


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
        setupPassdata(savedInstance);
        inflateFragment();
    }

    @Override
    protected void inflateFragment() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (canUseOtherMethod && isShowChooseMethod) {
            fragment = ChooseVerificationMethodFragment.createInstance(phoneNumber, email, otpType, canUseOtherMethod);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        } else {
            fragment = getDefaultFragment(requestOtpMode);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        }

        fragmentTransaction.commit();
    }

    protected void setupPassdata(Bundle savedInstanceState) {

        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState != null) {
            canUseOtherMethod = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false);
            phoneNumber = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "");
            email = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "");
            otpType = savedInstanceState.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0);
            isShowChooseMethod = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true);
            requestOtpMode = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        } else if (bundle != null) {
            canUseOtherMethod = bundle.getBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false);
            phoneNumber = bundle.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "");
            email = bundle.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "");
            otpType = bundle.getInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, 0);
            isShowChooseMethod = bundle.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true);
            requestOtpMode = bundle.getString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        } else {
            Log.d(VerificationActivity.class.getSimpleName(), "Error no pass data");
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, canUseOtherMethod);
        outState.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        outState.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        outState.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType);
        outState.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, isShowChooseMethod);
        outState.putString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, requestOtpMode);
    }

    protected Fragment getDefaultFragment(String mode) {
        Fragment fragment;
        switch (mode) {
            case RequestOtpUseCase.MODE_SMS: {
                fragment = VerificationFragment.createInstance(createSmsBundle());
                break;
            }
            case RequestOtpUseCase.MODE_EMAIL: {
                fragment = VerificationFragment.createInstance(createEmailBundle());
                break;
            }
            default: {
                fragment = ChooseVerificationMethodFragment.createInstance(phoneNumber, email, otpType, canUseOtherMethod);
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

            Fragment fragment = ChooseVerificationMethodFragment.createInstance(phoneNumber, email, otpType, canUseOtherMethod);
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

    protected VerificationViewModel createSmsBundle() {

        return new VerificationViewModel(
                phoneNumber,
                email,
                otpType,
                RequestOtpUseCase.MODE_SMS,
                R.drawable.ic_verification_sms,
                createSmsMessage(phoneNumber, otpType),
                OTPAnalytics.Screen.SCREEN_COTP_SMS,
                canUseOtherMethod
        );
    }

    private VerificationViewModel createEmailBundle() {

        return new VerificationViewModel(
                phoneNumber,
                email,
                otpType,
                RequestOtpUseCase.MODE_EMAIL,
                R.drawable.ic_verification_email,
                createEmailMessage(email),
                OTPAnalytics.Screen.SCREEN_COTP_EMAIL,
                canUseOtherMethod
        );
    }

    protected VerificationViewModel createDynamicBundle(MethodItem methodItem) {


        return new VerificationViewModel(
                phoneNumber,
                email,
                otpType,
                methodItem.getModeName(),
                methodItem.getImageUrl(),
                methodItem.getVerificationText(),
                getDynamicAppScreen(methodItem.getModeName()),
                canUseOtherMethod
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

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType);
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true);

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

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, canUseOtherMethod);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email);
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType);

        bundle.putString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, defaultRequestMode);
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false);
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

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();

        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, canUseOtherMethod);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber);
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "");
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, otpType);

        bundle.putString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, defaultRequestMode);
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false);
        intent.putExtras(bundle);
        return intent;
    }


}
