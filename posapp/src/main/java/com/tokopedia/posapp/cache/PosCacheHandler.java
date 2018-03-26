package com.tokopedia.posapp.cache;

import android.content.Context;

import com.tokopedia.posapp.cart.data.source.db.CartDbManager;
import com.tokopedia.posapp.etalase.EtalaseDbManager;
import com.tokopedia.posapp.product.common.data.source.local.ProductDbManager;
import com.tokopedia.posapp.base.domain.model.DataStatus;

import rx.Observable;
import rx.functions.Func3;

/**
 * Created by okasurya on 10/5/17.
 */

public class PosCacheHandler {
    public static void clearUserData(Context context) {
        CartDbManager cartDbManager = new CartDbManager();
        EtalaseDbManager etalaseDbManager = new EtalaseDbManager();
        ProductDbManager productDbManager = new ProductDbManager();
        Observable.zip(
            cartDbManager.deleteAll(),
            etalaseDbManager.deleteAll(),
            productDbManager.deleteAll(),
            new Func3<DataStatus, DataStatus, DataStatus, Boolean>() {
                @Override
                public Boolean call(DataStatus dataStatus, DataStatus dataStatus2, DataStatus dataStatus3) {
                    return dataStatus.isOk() && dataStatus2.isOk() && dataStatus3.isOk();
                }
            }
        ).subscribe();
    }
}
