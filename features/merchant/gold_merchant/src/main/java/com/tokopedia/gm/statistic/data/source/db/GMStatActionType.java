package com.tokopedia.gm.statistic.data.source.db;

import android.support.annotation.IntDef;

import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.BUYER;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.KEYWORD;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.POPULAR_PRODUCT;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.PROD_GRAPH;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.SHOP_CAT;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.TRANS_GRAPH;
import static com.tokopedia.gm.statistic.data.source.db.GMStatActionType.TRANS_TABLE;

/**
 * @author normansyahputa on 4/25/17.
 */
@IntDef({BUYER, KEYWORD, POPULAR_PRODUCT, PROD_GRAPH, SHOP_CAT, TRANS_GRAPH, TRANS_TABLE})
public @interface GMStatActionType {
    int BUYER = 1;
    int KEYWORD = 2;
    int POPULAR_PRODUCT = 3;
    int PROD_GRAPH = 4;
    int SHOP_CAT = 5;
    int TRANS_GRAPH = 6;
    int TRANS_TABLE = 7;
}