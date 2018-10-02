package com.tokopedia.loginregister.common.view;

import com.facebook.AccessToken;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.network.ErrorHandler;

/**
 * @author by nisie on 10/11/17.
 */

public class GetFacebookCredentialSubscriber {

    public interface GetFacebookCredentialListener {
        void onErrorGetFacebookCredential(Exception errorMessage);

        void onSuccessGetFacebookCredential(AccessToken accessToken, String email);
    }

    private final GetFacebookCredentialListener viewListener;

    public GetFacebookCredentialSubscriber(GetFacebookCredentialListener viewListener) {
        this.viewListener = viewListener;
    }

    public void onError(Exception e) {
        viewListener.onErrorGetFacebookCredential(e);
    }

    public void onSuccess(AccessToken accessToken, String email) {
        viewListener.onSuccessGetFacebookCredential(accessToken, email);
    }
}
