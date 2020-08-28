package com.tokopedia.topads.dashboard.utils;

import java.io.IOException;
import java.util.List;

/**
 * Created by zulfikarrahman on 12/27/17.
 */

public class TomeException extends IOException {
    List<String> messageErrorList;
    String errorCode;

    public List<String> getMessageError() {
        return messageErrorList;
    }

    public String getErrorCode() {
        return errorCode;
    }
    public TomeException(String errorCode, List<String> messages) {
        super(join(messages, ","));
        this.errorCode = errorCode;
        this.messageErrorList = messages;
    }

    private static String join(List<String> messages, String errorCode) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages) {
            sb.append(message).append(errorCode);
        }
        return sb.toString();
    }

    public TomeException(String errorListMessage) {
        super(errorListMessage);
    }

    public TomeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TomeException(Throwable cause) {
        super(cause);
    }

}
