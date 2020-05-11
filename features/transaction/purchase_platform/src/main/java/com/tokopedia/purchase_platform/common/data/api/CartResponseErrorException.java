package com.tokopedia.purchase_platform.common.data.api;

import com.tokopedia.abstraction.common.network.exception.ResponseErrorException;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartResponseErrorException extends ResponseErrorException {

    private static final long serialVersionUID = -3672249531132491023L;

    public CartResponseErrorException(String message) {
        super(message);
    }

    CartResponseErrorException() {
        super();
    }

}
