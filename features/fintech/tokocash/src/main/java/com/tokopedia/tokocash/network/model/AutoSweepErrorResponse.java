package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.tokocash.network.exception.AutoSweepException;

import java.io.IOException;
import java.util.List;

public class AutoSweepErrorResponse extends BaseResponseError {

    private static final String ERRORS = "errors";

    @SerializedName(ERRORS)
    @Expose
    private List<String> errorMessage;

    @Override
    public String getErrorKey() {
        return ERRORS;
    }

    @Override
    public boolean hasBody() {
        return errorMessage != null;
    }

    @Override
    public IOException createException() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < errorMessage.size(); i++) {
            stringBuilder.append(errorMessage.get(i));
            if (i < errorMessage.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return new AutoSweepException(stringBuilder.toString().trim());
    }
}
