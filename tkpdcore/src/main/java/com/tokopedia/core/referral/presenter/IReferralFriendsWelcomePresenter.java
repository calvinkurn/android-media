package com.tokopedia.core.referral.presenter;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public interface IReferralFriendsWelcomePresenter {

    void initialize();

    void copyVoucherCode(String voucherCode);

    String getReferralWelcomeMsg();
}