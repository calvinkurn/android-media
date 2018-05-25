package com.tokopedia.tokocash.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.tokocash.network.exception.WalletException;

import java.io.IOException;
import java.util.List;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class WalletErrorResponse extends BaseResponseError {

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
        return new WalletException(stringBuilder.toString().trim());
    }
}
