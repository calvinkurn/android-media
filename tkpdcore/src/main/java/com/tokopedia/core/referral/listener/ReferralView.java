package com.tokopedia.core.referral.listener;

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

    void navigateToLoginPage();

    void showVerificationPhoneNumberPage();

    String getReferralCodeFromTextView();

    void showProcessDialog();

    void hideProcessDialog();

    void renderErrorGetVoucherCode(String message);
}