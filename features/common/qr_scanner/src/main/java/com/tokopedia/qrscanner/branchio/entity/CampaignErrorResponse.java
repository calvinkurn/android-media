package com.tokopedia.qrscanner.branchio.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.network.data.model.response.BaseResponseError;

import java.io.IOException;

/**
 * Created by sandeepgoyal on 13/02/18.
 */

public class CampaignErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "message_error";
    private static final String DATA_KEY = "data";

    public CampaignResponseEntity getErrorCode() {
        return data;
    }

    public void setErrorCode(CampaignResponseEntity data) {
        this.data = data;
    }

    @SerializedName(DATA_KEY)
    @Expose
    private CampaignResponseEntity data;

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
        return new CampaignException(error[0], data.getStatus());
    }
}
