package com.tokopedia.core.referral.presenter;

import android.support.v4.app.FragmentManager;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public interface IReferralPresenter {

    void initialize();

    void shareApp(FragmentManager fragmentManager);

    void getReferralVoucherCode();

    void copyVoucherCode(String code);

    String getReferralContents();

    String getHowItWorks();

    String getVoucherCodeFromCache();

    Boolean isAppShowReferralButtonActivated();
}
