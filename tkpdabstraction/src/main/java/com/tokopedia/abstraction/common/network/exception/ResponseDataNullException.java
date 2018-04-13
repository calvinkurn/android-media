package com.tokopedia.abstraction.common.network.exception;

import java.io.IOException;

/**
 * Created by kris on 3/29/18. Tokopedia
 */

public class ResponseDataNullException extends IOException {

    private static final long serialVersionUID = 3945927716104271468L;

    public ResponseDataNullException(String message) {
        super(message);
    }
}
