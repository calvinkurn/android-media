package com.tokopedia.kol.common.network;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;

/**
 * @author by milhamj on 19/04/18.
 */

public class GraphqlErrorHandler {

    public static String getErrorMessage(final Context context, Throwable e) {
        if (e instanceof GraphqlErrorException) {
            return e.getLocalizedMessage();
        } else {
            return ErrorHandler.getErrorMessage(context, e);
        }
    }

}
