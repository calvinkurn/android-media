package com.tokopedia.posapp.auth.login.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.session.data.viewmodel.SecurityDomain;
import com.tokopedia.session.register.view.activity.SmartLockActivity;

/**
 * @author okasurya on 3/10/18.
 */

public abstract class BaseLoginFragment extends BaseDaggerFragment {
    protected static final String COLOR_WHITE = "#FFFFFF";
    protected static final String FACEBOOK = "facebook";
    protected static final String GPLUS = "gplus";
    protected static final String PHONE_NUMBER = "tokocash";

    protected static final int REQUEST_SMART_LOCK = 101;
    protected static final int REQUEST_SAVE_SMART_LOCK = 102;
    protected static final int REQUEST_LOGIN_WEBVIEW = 103;
    protected static final int REQUEST_SECURITY_QUESTION = 104;
    protected static final int REQUEST_LOGIN_PHONE_NUMBER = 105;
    protected static final int REQUESTS_CREATE_PASSWORD = 106;
    protected static final int REQUEST_ACTIVATE_ACCOUNT = 107;
    protected static final int REQUEST_VERIFY_PHONE = 108;

    public static final int TYPE_SQ_PHONE = 1;
    public static final int TYPE_SQ_EMAIL = 2;

    public static final String IS_AUTO_LOGIN = "auto_login";
    public static final String AUTO_LOGIN_METHOD = "method";

    public static final String AUTO_LOGIN_EMAIL = "email";
    public static final String AUTO_LOGIN_PASS = "pw";


    protected void setWrapperError(TkpdHintTextInputLayout wrapper, String s) {
        if (s == null) {
            wrapper.setError(null);
            wrapper.setErrorEnabled(false);
        } else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }

    protected TextWatcher watcher(final TkpdHintTextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    setWrapperError(wrapper, getString(com.tokopedia.core.R.string.error_field_required));
                }
            }
        };
    }

    protected void showSmartLock() {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, SmartLockActivity.RC_READ);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SMART_LOCK);
    }

    protected void saveSmartLock(int state, String email, String password) {
        Intent intent = new Intent(getActivity(), SmartLockActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(SmartLockActivity.STATE, state);
        if (state == SmartLockActivity.RC_SAVE_SECURITY_QUESTION || state == SmartLockActivity.RC_SAVE) {
            bundle.putString(SmartLockActivity.USERNAME, email);
            bundle.putString(SmartLockActivity.PASSWORD, password);
        }
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_SAVE_SMART_LOCK);
    }

    protected void goToSecurityQuestion(SecurityDomain securityDomain,
                                        String fullName,
                                        String email,
                                        String phone, GlobalCacheManager cacheManager) {
        InterruptVerificationViewModel interruptVerificationViewModel;
        if (securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE) {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultSmsInterruptPage(phone);
        } else {
            interruptVerificationViewModel = InterruptVerificationViewModel
                    .createDefaultEmailInterruptPage(email);
        }

        VerificationPassModel passModel = new
                VerificationPassModel(phone, email,
                RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION,
                interruptVerificationViewModel,
                securityDomain.getUserCheckSecurity2() == TYPE_SQ_PHONE
        );
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();

        Intent intent = VerificationActivity.getSecurityQuestionVerificationIntent(getActivity(),
                securityDomain.getUserCheckSecurity2());
        startActivityForResult(intent, REQUEST_SECURITY_QUESTION);
    }
}
