package com.tokopedia.core.referral.listener;

import android.app.Activity;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.referral.data.PromoContent;
import com.tokopedia.core.referral.data.ReferralCodeEntity;
import com.tokopedia.core.referral.model.ShareApps;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public interface ReferralView extends CustomerView {

    void renderVoucherCodeData(ReferralCodeEntity referralData);

    Activity getActivity();

    void showToastMessage(String message);

    void closeView();

    void navigateToLoginPage();

    void showVerificationPhoneNumberPage();

    String getReferralCodeFromTextView();

    void showProcessDialog();

    void hideProcessDialog();

    void renderErrorGetVoucherCode(String message);

    void renderSharableApps(ShareApps shareApps , int index);
}