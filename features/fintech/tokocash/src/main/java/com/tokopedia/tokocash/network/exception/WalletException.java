package com.tokopedia.tokocash.network.exception;

import java.io.IOException;

/**
 * Created by nabillasabbaha on 1/19/18.
 */

public class WalletException extends IOException {

    public WalletException(String message) {
        super(message);
    }
}
