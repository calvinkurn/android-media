package com.tokopedia.qrscanner;

import java.io.IOException;

/**
 * Created by nabillasabbaha on 2/13/18.
 */

public class CampaignException extends IOException {
    public static int MISSING_AUTHORIZATION_CREDENTIALS = 401;
    int error_code;

    public CampaignException(String message, int code) {
        super(message);
        this.error_code = code;
    }

    public int getError_code() {
        return error_code;
    }

    public boolean isMissingAuthorizationCredentials() {
        return error_code == MISSING_AUTHORIZATION_CREDENTIALS;
    }
}
