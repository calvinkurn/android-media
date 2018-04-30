package com.tokopedia.otp.common.network;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.otp.R;

/**
 * @author by Nisie
 */
public class OtpErrorHandler {

    public static String getErrorMessage(Throwable e, final Context context) {

        if (e instanceof OtpErrorException
                && !TextUtils.isEmpty(e.getLocalizedMessage())) {
            return e.getLocalizedMessage();
        } else if (e instanceof ErrorMessageException
                && !TextUtils.isEmpty(e.getLocalizedMessage())){
            return e.getLocalizedMessage();
        } else {
            return ErrorHandler.getErrorMessage(context, e);
        }
    }

    public static String getDefaultErrorCodeMessage(int errorCode, Context context) {
        return context.getString(R.string.default_request_error_unknown) + " (" + errorCode + ")";
    }
}
