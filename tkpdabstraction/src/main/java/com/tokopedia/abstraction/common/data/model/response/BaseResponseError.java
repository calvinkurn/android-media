package com.tokopedia.abstraction.common.data.model.response;

/**
 * Created by User on 9/15/2017.
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
    public abstract RuntimeException createException();

    /**
     * @return the error has additional content
     */
    public abstract boolean hasCustomAdditionalError();

}
