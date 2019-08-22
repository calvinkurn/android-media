package com.tokopedia.product.manage.item.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.common.util.ImageStatusTypeDef.ALREADY_DELETED;
import static com.tokopedia.product.manage.item.common.util.ImageStatusTypeDef.ALREADY_UPLOADED;
import static com.tokopedia.product.manage.item.common.util.ImageStatusTypeDef.DEFAULT;
import static com.tokopedia.product.manage.item.common.util.ImageStatusTypeDef.WILL_BE_DELETED;
import static com.tokopedia.product.manage.item.common.util.ImageStatusTypeDef.WILL_BE_UPLOADED;

/**
 * @author sebastianuskh on 5/16/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({DEFAULT, WILL_BE_DELETED, ALREADY_DELETED, WILL_BE_UPLOADED, ALREADY_UPLOADED})
public @interface ImageStatusTypeDef {
    int DEFAULT = 0;
    int WILL_BE_DELETED = 1;
    int ALREADY_DELETED = 2;
    int WILL_BE_UPLOADED = 3;
    int ALREADY_UPLOADED = 4;
}
