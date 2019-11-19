package com.tokopedia.product.manage.item.utils.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.product.manage.item.utils.constant.ProductConditionTypeDef.TYPE_NEW;
import static com.tokopedia.product.manage.item.utils.constant.ProductConditionTypeDef.TYPE_RECON;


/**
 * Created by User on 8/11/2017.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_NEW, TYPE_RECON})
public @interface ProductConditionTypeDef {
    int TYPE_NEW = 1;
    int TYPE_RECON = 2;
}

