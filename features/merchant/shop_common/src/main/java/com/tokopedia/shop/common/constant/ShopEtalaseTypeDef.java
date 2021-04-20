package com.tokopedia.shop.common.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_CUSTOM;
import static com.tokopedia.shop.common.constant.ShopEtalaseTypeDef.ETALASE_DEFAULT;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({ETALASE_DEFAULT, ETALASE_CUSTOM})
public @interface ShopEtalaseTypeDef {
    int ETALASE_DEFAULT = -1;   // Generated etalase
    int ETALASE_CAMPAIGN = -2;
    int ETALASE_THEMATIC_CAMPAIGN = -3;
    int ETALASE_CUSTOM = 1;
}
