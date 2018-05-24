package com.tokopedia.otp.common.network;

/**
 * Created by nisie on 3/14/17.
 */

public class OtpErrorException extends RuntimeException {

    public OtpErrorException(String errorMessage) {
        super(errorMessage);
    }
}
