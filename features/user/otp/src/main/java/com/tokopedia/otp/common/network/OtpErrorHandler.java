package com.tokopedia.otp.common.network;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.otp.R;

/**
 * @author by Nisie
 */
public class OtpErrorHandler {

    public static String getErrorMessage(Throwable e, final Context context, boolean
            showErrorCode) {

        if (e instanceof OtpErrorException
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            return showErrorCode ? formatString(e.getLocalizedMessage(),
                    String.valueOf(OtpErrorCode.WS_ERROR))
                    : e.getLocalizedMessage();
        } else if (e instanceof ErrorMessageException
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            return showErrorCode ? formatString(e.getLocalizedMessage(), ((ErrorMessageException)
                    e).getErrorCode()) : e.getLocalizedMessage();
        } else {
            return ErrorHandler.getErrorMessage(context, e);
        }
    }

    private static String formatString(String message, String errorCode) {
        return String.format("%s ( %s )", message, String.valueOf(errorCode));
    }

    public static String getDefaultErrorCodeMessage(int errorCode, Context context) {
        return context.getString(R.string.default_request_error_unknown) + " (" + errorCode + ")";
    }
}
