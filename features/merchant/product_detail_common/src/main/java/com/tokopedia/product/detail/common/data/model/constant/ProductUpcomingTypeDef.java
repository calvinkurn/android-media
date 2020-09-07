package com.tokopedia.product.detail.common.data.model.constant;

import androidx.annotation.StringDef;
import static com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef.UPCOMING_DEALS;
import static com.tokopedia.product.detail.common.data.model.constant.ProductUpcomingTypeDef.UPCOMING_NPL;

/**
 * Created by Yehezkiel on 14/07/20
 */
@StringDef({UPCOMING_NPL, UPCOMING_DEALS})
public @interface ProductUpcomingTypeDef {
    String UPCOMING_NPL = "UPCOMING_NPL";
    String UPCOMING_DEALS = "UPCOMING_DEALS";
}
