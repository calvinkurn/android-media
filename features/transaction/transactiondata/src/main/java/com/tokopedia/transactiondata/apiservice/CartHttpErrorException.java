package com.tokopedia.transactiondata.apiservice;

import com.tokopedia.abstraction.common.network.exception.HttpErrorException;

/**
 * @author anggaprasetiyo on 20/08/18.
 */
public class CartHttpErrorException extends HttpErrorException {

    private static final long serialVersionUID = 284092320965069473L;

    public CartHttpErrorException(int errorCode) {
        super(errorCode);
    }
}
