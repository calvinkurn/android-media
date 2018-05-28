package com.tokopedia.contactus.common.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;

/**
 * Created by sandeepgoyal on 13/02/18.
 */

public class ContactUsErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "message_error";

    @SerializedName(ERROR_KEY)
    @Expose
    private String[] error;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return error != null;
    }

    @Override
    public IOException createException() {
        return new ContactUsException(error[0]);
    }
}
