package com.tokopedia.core.referral.presenter;

/**
 * Created by ashwanityagi on 18/09/17.
 */

public interface IReferralPresenter {

    void initialize();

    void shareApp();

    void getReferralVoucherCode();

    void copyVoucherCode(String code);

    String getReferralContents();

    String getHowItWorks();

    String getVoucherCodeFromCache();

    Boolean isAppShowReferralButtonActivated();
}
