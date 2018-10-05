package com.tokopedia.merchantvoucher.common.gql.data;

import com.tokopedia.abstraction.common.network.exception.MessageErrorException;

public class MessageTitleErrorException extends MessageErrorException {
    private String errorMessageTitle;

    public MessageTitleErrorException(String messageTitle, String message, String errorCode) {
        super(message, errorCode);
        this.errorMessageTitle = messageTitle;
    }

    public String getErrorMessageTitle() {
        return errorMessageTitle;
    }
}
