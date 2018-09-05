package com.tokopedia.transactiondata.apiservice;

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartResponseErrorException extends ResponseErrorException {

    private static final long serialVersionUID = -3672249531132491023L;

    private final int httpErrorCode;
    private final String internalServiceErrorCode;

    CartResponseErrorException(String message) {
        super(message);
        this.httpErrorCode = 0;
        this.internalServiceErrorCode = "0";
    }

    CartResponseErrorException(int httpErrorCode, String internalServiceErrorCode, String messageError) {
        super(messageError);
        this.internalServiceErrorCode = internalServiceErrorCode;
        this.httpErrorCode = httpErrorCode;
    }

    CartResponseErrorException() {
        super();
        this.httpErrorCode = 0;
        this.internalServiceErrorCode = "0";
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public String getInternalServiceErrorCode() {
        return internalServiceErrorCode;
    }
}
