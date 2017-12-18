package com.tokopedia.core.referral.listner;

import android.app.Activity;

/**
 * Created by ashwanityagi on 04/12/17.
 */

public interface FriendsWelcomeView {

    Activity getActivity();

    void showToastMessage(String message);

    void closeView();

    void renderReferralCode(String code);

}