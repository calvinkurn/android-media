package com.tokopedia.tkpd.campaign.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;

import java.util.List;

/**
 * Created by sandeepgoyal on 13/02/18.
 */

public class CampaignErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "message_error";

    @SerializedName(ERROR_KEY)
    @Expose
    private String error;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return error!= null;
    }

    @Override
    public RuntimeException createException() {
        return new RuntimeException(error);
    }

    @Override
    public boolean hasCustomAdditionalError() {
        return false;
    }
}
