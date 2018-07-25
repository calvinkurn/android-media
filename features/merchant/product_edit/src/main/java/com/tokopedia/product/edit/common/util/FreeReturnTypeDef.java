package com.tokopedia.product.edit.common.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.edit.common.util.FreeReturnTypeDef.TYPE_ACTIVE;
import static com.tokopedia.product.edit.common.util.FreeReturnTypeDef.TYPE_INACTIVE;

/**
 * Created by User on 8/11/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_ACTIVE, TYPE_INACTIVE})
public @interface FreeReturnTypeDef {
    int TYPE_ACTIVE = 3;
    int TYPE_INACTIVE = 0;
}
