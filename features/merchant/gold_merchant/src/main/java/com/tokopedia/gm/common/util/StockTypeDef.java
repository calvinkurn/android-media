package com.tokopedia.gm.common.util;

import androidx.annotation.IntDef;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({StockTypeDef.TYPE_ACTIVE, StockTypeDef.TYPE_ACTIVE_LIMITED, StockTypeDef.TYPE_WAREHOUSE})
public @interface StockTypeDef {
    int TYPE_ACTIVE = 1; // from api
    int TYPE_ACTIVE_LIMITED = 2; // only for view
    int TYPE_WAREHOUSE = 3; // from api
}