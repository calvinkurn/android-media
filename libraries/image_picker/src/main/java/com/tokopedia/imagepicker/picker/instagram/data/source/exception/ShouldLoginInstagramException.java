package com.tokopedia.imagepicker.picker.instagram.data.source.exception;

import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * Created by zulfikarrahman on 5/4/18.
 */

public class ShouldLoginInstagramException extends RuntimeException {
    public ShouldLoginInstagramException() {
    }

    public ShouldLoginInstagramException(String message) {
        super(message);
    }

    public ShouldLoginInstagramException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShouldLoginInstagramException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ShouldLoginInstagramException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
