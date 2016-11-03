package com.tokopedia.tkpd.myproduct.utils;

/**
 * Created by zulfikarrahman on 10/13/16.
 */

public class MessageErrorException extends RuntimeException {
    /**
     * Constructs a new {@code RuntimeException} with the current stack trace
     * and the specified detail message.
     *
     * @param detailMessage
     *            the detail message for this exception.
     */
    public MessageErrorException(String detailMessage) {
        super(detailMessage);
    }
}
