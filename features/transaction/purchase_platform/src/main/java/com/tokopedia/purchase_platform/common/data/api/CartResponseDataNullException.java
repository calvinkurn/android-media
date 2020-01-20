package com.tokopedia.purchase_platform.common.data.api;

import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException;

/**
 * @author anggaprasetiyo on 20/08/18.
 */
public class CartResponseDataNullException extends ResponseDataNullException {

    private static final long serialVersionUID = 1792032688048080976L;

    public CartResponseDataNullException(String message) {
        super(message);
    }
}
