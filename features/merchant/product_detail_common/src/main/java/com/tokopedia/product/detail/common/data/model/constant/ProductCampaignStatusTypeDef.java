package com.tokopedia.product.detail.common.data.model.constant;

import androidx.annotation.StringDef;

import static com.tokopedia.product.detail.common.data.model.constant.ProductCampaignStatusTypeDef.ACTIVE;
import static com.tokopedia.product.detail.common.data.model.constant.ProductCampaignStatusTypeDef.INACTIVE;
import static com.tokopedia.product.detail.common.data.model.constant.ProductCampaignStatusTypeDef.UPCOMING;


/**
 * Created by Yehezkiel on 17/07/20
 */
@StringDef({UPCOMING, INACTIVE, ACTIVE})
public @interface ProductCampaignStatusTypeDef {
    String UPCOMING = "UPCOMING";
    String INACTIVE = "INACTIVE";
    String ACTIVE = "ACTIVE";
}

