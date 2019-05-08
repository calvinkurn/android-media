package com.tokopedia.search.result.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.network.exception.ResponseErrorException;

import java.io.IOException;
import java.util.List;

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

    public String getErrorKey() {
        return ERROR_KEY;
    }

    public boolean hasBody(){
        return (errors!= null && errors.size() > 0);
    }

    @Override
    public IOException createException() {
        return new ResponseErrorException(errors.toString());
    }
}