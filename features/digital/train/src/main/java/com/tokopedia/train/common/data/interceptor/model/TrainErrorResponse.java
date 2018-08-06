package com.tokopedia.train.common.data.interceptor.model;

import android.os.Build;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.BaseResponseError;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.train.common.data.interceptor.TrainNetworkException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrainErrorResponse extends BaseResponseError {
    private static final String ERROR_KEY = "errors";

    @SerializedName(ERROR_KEY)
    @Expose
    private List<TrainNetworkError> errorList;

    public TrainErrorResponse() {
    }

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
        List<TrainError> errors = new ArrayList<>();
        String message = getConcattedMessage(errors);

        return new TrainNetworkException(message, errors);
    }

    private String getConcattedMessage(List<TrainError> errors) {
        List<String> results = new ArrayList<>();
        Gson gson = new Gson();
        TrainError error = null;
        for (TrainNetworkError errorNetwork : errorList) {
            try {
                error = gson.fromJson(errorNetwork.getMessage(), TrainError.class);
                if (error != null) {
                    errors.add(error);
                    results.add(error.getMessage());
                    continue;
                }
            } catch (JsonSyntaxException e) {
                CommonUtils.dumper(errorNetwork.getMessage());
            }

            results.add(errorNetwork.getMessage());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return String.join(", ", results);
        } else {
            return TextUtils.join(", ", results);
        }
    }
}
