package com.tokopedia.core.referral.listner;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public interface ReferralView extends CustomerView {

    void renderVoucherCode(String voucherCode);

    Activity getActivity();

    void showToastMessage(String message);

    void closeView();

    boolean isUserLoggedIn();

    boolean isUserPhoneNumberVerified();

    void navigateToLoginPage();

    void showVerificationPhoneNumberPage();

    String getReferralCodeFromTextView();

    void showProcessDialog();

    void hideProcessDialog();

    void setShareButtonEnable(Boolean enable);

}