package com.tokopedia.shop.analytic.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef.ETALASE_HIGHLIGHT;
import static com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef.FEATURED;
import static com.tokopedia.shop.analytic.model.ShopTrackProductTypeDef.PRODUCT;

/**
 * @author normansyahputa on 4/25/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PRODUCT, FEATURED, ETALASE_HIGHLIGHT})
public @interface ShopTrackProductTypeDef {
    int PRODUCT = 1;
    int FEATURED = 2;
    int ETALASE_HIGHLIGHT = 3;
}
