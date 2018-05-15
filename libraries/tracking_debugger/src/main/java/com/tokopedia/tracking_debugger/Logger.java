package com.tokopedia.tracking_debugger;

import android.content.Context;
import android.content.Intent;

/**
 * @author okasurya on 5/14/18.
 */

public interface Logger {
    void save(String data, String name);

    void removeAll();

    Intent getLaunchIntent(Context context);
}
