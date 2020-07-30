package com.tokopedia.flight.orderlist.util;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.flight.orderlist.network.model.FlightOrderError;
import com.tokopedia.flight.orderlist.network.model.FlightOrderException;

/**
 * Created by User on 11/28/2017.
 */

public class FlightErrorUtil {
    public static String getMessageFromException(Context context, Throwable e) {
        if (e instanceof FlightOrderException) {
            return TextUtils.join(",", ((FlightOrderException) e).getErrorList());
        } else {
            return ErrorHandler.getErrorMessage(context, e);
        }
    }

    public static int getErrorCode(FlightOrderError flightError) {
        try {
            return Integer.parseInt(flightError.getId());
        } catch (Exception e) {
            return -1;
        }
    }
}
