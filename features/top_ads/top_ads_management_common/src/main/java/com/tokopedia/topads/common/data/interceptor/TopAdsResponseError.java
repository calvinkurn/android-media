package com.tokopedia.topads.common.data.interceptor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.topads.common.data.exception.ResponseErrorException;
import com.tokopedia.topads.common.data.response.Error;

import java.io.IOException;
import java.util.List;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsResponseError extends BaseResponseError {
    private static final String ERROR_KEY = "errors";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<Error> errors = null;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody(){
        return (errors!= null && errors.size() > 0);
    }

    @Override
    public IOException createException() {
        return new ResponseErrorException(errors);
    }
}
