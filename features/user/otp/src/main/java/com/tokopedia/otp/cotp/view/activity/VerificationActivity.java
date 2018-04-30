package com.tokopedia.otp.cotp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.otp.R;
import com.tokopedia.otp.common.OTPAnalytics;
import com.tokopedia.otp.common.di.DaggerOtpComponent;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
import com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;

import javax.inject.Inject;


/**
 * @author by nisie on 11/29/17.
 * <p>
 * Please build VerificationPassModel and put it to cachemanager with key PASS_MODEL.
 * This is to prevent TransactionTooLarge.
 */

public class VerificationActivity extends BaseSimpleActivity {

    public static final String PASS_MODEL = "VerificationPassModel";

    public static final String PARAM_REQUEST_OTP_MODE = "fragmentType";

    public static final String PARAM_IMAGE = "image";
    public static final String PARAM_IMAGE_URL = "image_url";
    public static final String PARAM_PHONE_NUMBER = "phone";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_APP_SCREEN = "app_screen";
    public static final String PARAM_MESSAGE = "message";

    private static final String FIRST_FRAGMENT_TAG = "first";
    private static final String CHOOSE_FRAGMENT_TAG = "choose";

    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    private VerificationPassModel passModel;

    @Inject
    CacheManager cacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInjector();
        initView();
    }

    private void initInjector() {
        OtpComponent otpComponent = DaggerOtpComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication())
                        .getBaseAppComponent()).build();

        DaggerCotpComponent.builder()
                .otpComponent(otpComponent)
                .build().inject(this);

    }

    @Override
    protected void setupFragment(Bundle savedInstance) {

    }

    private void initView() {

        setupPassdata();

        Bundle bundle = new Bundle();
        if (getIntent().getExtras() != null)
            bundle.putAll(getIntent().getExtras());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment;

        if (passModel.canUseOtherMethod()) {
//            fragment = InterruptVerificationFragment.createInstance(bundle);
            fragment = ChooseVerificationMethodFragment.createInstance(bundle);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        } else {
            fragment =
                    getDefaultFragment(getIntent().getExtras().getString(PARAM_REQUEST_OTP_MODE, ""), bundle);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
        }

        fragmentTransaction.commit();

    }

    private void setupPassdata() {
        Gson gson = new Gson();
        VerificationPassModel tempPassModel = gson.fromJson(cacheManager
                .get(VerificationActivity.PASS_MODEL), VerificationPassModel.class);

        if (cacheManager != null
                && tempPassModel != null) {
            passModel = tempPassModel;
        } else {
            Log.d(VerificationActivity.class.getSimpleName(), "Error no pass data");
            finish();
        }
    }

    private Fragment getDefaultFragment(String mode, Bundle bundle) {
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
                fragment = ChooseVerificationMethodFragment.createInstance(bundle);
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

            Fragment fragment = ChooseVerificationMethodFragment.createInstance(getIntent().getExtras());
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0,
                    R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(CHOOSE_FRAGMENT_TAG);
            fragmentTransaction.commit();
        }
    }

    /**
     * @param mode should be from {@link com.tokopedia.otp.cotp.domain.interactor.RequestOtpUseCase}
     *             Do not use this for dynamic verification page. This should only be used for default page.
     */
    public void goToDefaultVerificationPage(String mode) {
        if (!(getSupportFragmentManager().findFragmentById(R.id.parent_view) instanceof
                VerificationFragment)) {

            getSupportFragmentManager().popBackStack(FIRST_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Fragment fragment = getDefaultFragment(mode, getIntent().getExtras());
            fragmentTransaction.setCustomAnimations(R.animator.slide_in_left, 0, 0,
                    R.animator.slide_out_right);
            fragmentTransaction.add(R.id.parent_view, fragment, FIRST_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(FIRST_FRAGMENT_TAG);
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

    private Bundle createSmsBundle(String phoneNumber, int otpType) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_sms);
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putString(PARAM_MESSAGE, createSmsMessage(phoneNumber, otpType));
        if (otpType == RequestOtpUseCase.OTP_TYPE_REGISTER_PHONE_NUMBER) {
            bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_PHONE_NUMBER_VERIFICATION);
        } else {
            bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_SMS);
        }
        return bundle;
    }

    private Bundle createEmailBundle(String email) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_EMAIL);
        bundle.putInt(PARAM_IMAGE, R.drawable.ic_verification_email);
        bundle.putString(PARAM_EMAIL, email);
        bundle.putString(PARAM_MESSAGE, createEmailMessage(email));
        bundle.putString(PARAM_APP_SCREEN, OTPAnalytics.Screen.SCREEN_COTP_EMAIL);
        return bundle;
    }

    private Bundle createDynamicBundle(MethodItem methodItem) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, methodItem.getModeName());
        bundle.putInt(PARAM_IMAGE, 0);
        bundle.putString(PARAM_IMAGE_URL, methodItem.getImageUrl());
        bundle.putString(PARAM_PHONE_NUMBER, passModel.getPhoneNumber());
        bundle.putString(PARAM_EMAIL, passModel.getEmail());
        bundle.putString(PARAM_MESSAGE, methodItem.getVerificationText());
        bundle.putString(PARAM_APP_SCREEN, getDynamicAppScreen(methodItem.getModeName()));
        return bundle;
    }

    private String getDynamicAppScreen(String mode) {
        return OTPAnalytics.Screen.SCREEN_COTP_DEFAULT + mode;
    }

    private String getMaskedPhone(String phoneNumber, int otpType) {
        if (otpType == RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION) {
            return MethodItem.getMaskedPhoneNumber(phoneNumber);
        } else {
            String masked = String.valueOf(phoneNumber).replaceFirst("(\\d{4})(\\d{4})(\\d+)",
                    "$1-$2-$3");
            return String.format(
                    ("<b>%s</b>"), masked);
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
    }

    public static Intent getSecurityQuestionVerificationIntent(Context context, int
            typeSecurityQuestion, String email, String phone) {

        GlobalCacheManager cacheManager = new GlobalCacheManager();

        VerificationPassModel passModel = new
                VerificationPassModel(phone, email,
                RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                typeSecurityQuestion == VerificationActivity.TYPE_SQ_PHONE
        );
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        if (typeSecurityQuestion == TYPE_SQ_PHONE) {
            bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_SMS);
        } else {
            bundle.putString(PARAM_REQUEST_OTP_MODE, RequestOtpUseCase.MODE_EMAIL);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent getCallingIntent(Context context, String phoneNumber, int otpType,
                                          boolean canUseOtherMethod, String requestMode) {
        VerificationPassModel passModel = new VerificationPassModel(phoneNumber,
                otpType,
                canUseOtherMethod);

        GlobalCacheManager cacheManager = new GlobalCacheManager();
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, requestMode);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    public static Intent getCallingIntent(Context context, String phoneNumber, String email,
                                          int otpType, boolean canUseOtherMethod,
                                          String requestMode) {
        VerificationPassModel passModel = new VerificationPassModel(phoneNumber,
                email,
                otpType,
                canUseOtherMethod);

        GlobalCacheManager cacheManager = new GlobalCacheManager();
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = new Intent(context, VerificationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_REQUEST_OTP_MODE, requestMode);
        intent.putExtras(bundle);
        return intent;
    }
}
