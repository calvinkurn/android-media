package com.tokopedia.product.detail.common.data.model.constant;

import androidx.annotation.IntDef;

import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.DELETED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.OPEN;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.CLOSED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.MODERATED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.INACTIVE;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.MODERATED_PERMANENTLY;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.INCUBATED;
import static com.tokopedia.product.detail.common.data.model.constant.ProductShopStatusTypeDef.INCOMPLETE;

/**
 * Created by Yehezkiel on 05/06/20
 */
@IntDef({DELETED, OPEN, CLOSED, MODERATED, INACTIVE, MODERATED_PERMANENTLY, INCUBATED, INCOMPLETE})
public @interface ProductShopStatusTypeDef {
    int DELETED = 0;
    int OPEN = 1;
    int CLOSED = 2;
    int MODERATED = 3;
    int INACTIVE = 4;
    int MODERATED_PERMANENTLY = 5;
    int INCUBATED = 6;
    int INCOMPLETE = 7;
}
