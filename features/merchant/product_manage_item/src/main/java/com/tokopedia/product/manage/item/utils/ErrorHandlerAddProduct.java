package com.tokopedia.product.manage.item.utils;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.product.manage.item.R;
import com.tokopedia.product.manage.item.main.base.data.exception.ImageUploadErrorException;

/**
 * Created by zulfikarrahman on 7/18/18.
 */

public class ErrorHandlerAddProduct {

    public static String getErrorMessage(Context context, Throwable e) {
        if (e instanceof ImageUploadErrorException) {
            return context.getString(R.string.product_label_message_error_upload_image);
        }else{
            return ErrorHandler.getErrorMessage(context, e);
        }
    }
}
