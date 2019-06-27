package com.tokopedia.core.network.exception;

import java.io.IOException;

/**
 * @author anggaprasetiyo on 2/27/17.
 */

@Deprecated
public class ResponseDataNullException extends IOException {

    private static final long serialVersionUID = 3945927716104271468L;

    public ResponseDataNullException(String message) {
        super(message);
    }
}
