package com.tokopedia.core.network.retrofit.response;

import java.io.IOException;

/**
 * Created by Hendry on 3/1/2017.
 */

@Deprecated
public abstract class BaseResponseError {

    /**
     * Check if this response error is valid
     */
    public boolean isResponseErrorValid() {
        return hasBody();
    }

    /**
     * @return if the error is filled, return true
     */
    public abstract boolean hasBody();

    /**
     * @return the exception from this Error
     */
    public abstract IOException createException();

}
