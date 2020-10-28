package com.tokopedia.product.detail.common.data.model.constant;

import androidx.annotation.IntDef;

import static com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef.AUTOADS_USER;
import static com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef.MANUAL_USER;
import static com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef.NO_ADS;
import static com.tokopedia.product.detail.common.data.model.constant.TopAdsShopCategoryTypeDef.NO_PRODUCT;

/**
 * Created by Yehezkiel on 01/09/20
 */
@IntDef({NO_PRODUCT, NO_ADS, MANUAL_USER, AUTOADS_USER})
public @interface TopAdsShopCategoryTypeDef {
    int NO_PRODUCT = 1;
    int NO_ADS = 2;
    int MANUAL_USER = 3;
    int AUTOADS_USER = 4;
}
