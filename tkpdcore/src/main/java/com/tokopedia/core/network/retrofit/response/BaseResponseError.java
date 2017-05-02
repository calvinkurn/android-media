package com.tokopedia.core.network.retrofit.response;

import java.io.IOException;

/**
 * Created by Hendry on 3/1/2017.
 * see TkpdResponseError for example.
 */

public abstract class BaseResponseError {

    /**
     * @return the String key if the json Response indicated that is an error response.
     */
    public abstract String getErrorKey();

    /**
     * @return if the error is filled, return true
     */
    public abstract boolean hasBody();

    /**
     * @return the exception from this Error
     */
    public abstract IOException createException();

}
