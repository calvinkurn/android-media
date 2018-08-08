package com.tokopedia.core.manage.people.profile.presenter;

/**
 * Created by nisie on 9/29/16.
 */

public interface EmailVerificationPresenter {
    void changeEmailClicked();

    void onRequestOTP();

    void onDestroyView();
}
