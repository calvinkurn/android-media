package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.view.DisplayMode;

/**
 * Created by errysuprayogi on 7/9/18.
 */

public interface DisplayChangeListener {

    void onDisplayChange(DisplayMode mode, int spanCount);
}
