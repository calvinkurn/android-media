package com.tokopedia.core.router;

import android.content.Intent;

/**
 * Created by normansyahputa on 12/20/17.
 */

public interface OnActivityResultListener<T> {
    void onActivityResult(T rawData);
}
