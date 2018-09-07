package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.tokocash.network.exception.TokoCashException;

import java.io.IOException;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class TokoCashErrorResponse extends BaseResponseError {

    private static final String ERROR = "error";

    @SerializedName(ERROR)
    @Expose
    private String errorMessage;
    @SerializedName("error_description")
    @Expose
    private String errorDesc;

    @Override
    public String getErrorKey() {
        return ERROR;
    }

    @Override
    public boolean hasBody() {
        return errorMessage != null;
    }

    @Override
    public IOException createException() {
        return new TokoCashException(errorMessage);
    }
}
