package com.tokopedia.flight.orderlist.network.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/28/2017.
 */

public class FlightOrderErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "errors";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<FlightOrderError> errorList;

    @Override
    public String getErrorKey() {
        return ERROR_KEY;
    }

    @Override
    public boolean hasBody() {
        return errorList != null && errorList.size() > 0;
    }

    @Override
    public IOException createException() {
        String message = getConcattedMessage();
        return new FlightOrderException(message, errorList);
    }

    private String getConcattedMessage() {
        List<String> results = new ArrayList<>();
        for (FlightOrderError error : errorList) {
            results.add(error.getTitle());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(", ", results);
        } else {
            return TextUtils.join(", ", results);
        }
    }
}
