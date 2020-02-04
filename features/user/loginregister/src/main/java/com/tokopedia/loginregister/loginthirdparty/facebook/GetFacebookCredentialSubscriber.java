package com.tokopedia.loginregister.loginthirdparty.facebook;

import com.facebook.AccessToken;

/**
 * @author by nisie on 10/11/17.
 */

public class GetFacebookCredentialSubscriber {

    public interface GetFacebookCredentialListener {
        void onErrorGetFacebookCredential(Exception errorMessage);

        void onSuccessGetFacebookEmailCredential(AccessToken accessToken, String email);

        void onSuccessGetFacebookPhoneCredential(AccessToken accessToken, String phone);
    }

    private final GetFacebookCredentialListener viewListener;

    public GetFacebookCredentialSubscriber(GetFacebookCredentialListener viewListener) {
        this.viewListener = viewListener;
    }

    public void onError(Exception e) {
        viewListener.onErrorGetFacebookCredential(e);
    }

    public void onSuccessEmail(AccessToken accessToken, String email) {
        viewListener.onSuccessGetFacebookEmailCredential(accessToken, email);
    }

    public void onSuccessPhone(AccessToken accessToken, String phone){
        viewListener.onSuccessGetFacebookPhoneCredential(accessToken, phone);
    }
}
