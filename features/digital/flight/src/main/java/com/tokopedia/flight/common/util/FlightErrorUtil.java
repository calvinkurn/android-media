package com.tokopedia.flight.common.util;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.common.data.model.FlightError;
import com.tokopedia.flight.common.data.model.FlightException;

/**
 * Created by User on 11/28/2017.
 */

public class FlightErrorUtil {
    public static String getMessageFromException(Context context, Throwable e) {
        if (e instanceof FlightException) {
            return TextUtils.join(",", ((FlightException) e).getErrorList());
        } else {
            return ErrorHandler.getErrorMessage(context, e);
        }
    }

    public static int getErrorCode(FlightError flightError) {
        try {
            return Integer.parseInt(flightError.getId());
        } catch (Exception e) {
            return -1;
        }
    }
}
