package com.tokopedia.product.detail.view.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.detail.view.util.AppBarState.COLLAPSED;
import static com.tokopedia.product.detail.view.util.AppBarState.EXPANDED;
import static com.tokopedia.product.detail.view.util.AppBarState.IDLE;

@Retention(RetentionPolicy.SOURCE)
@IntDef({EXPANDED, COLLAPSED, IDLE})
public @interface AppBarState {
    int EXPANDED = -1;
    int COLLAPSED = 1;
    int IDLE = 0;
}
