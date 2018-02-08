package com.tokopedia.posapp;

import android.content.Context;

import com.tokopedia.posapp.database.manager.CartDbManager;
import com.tokopedia.posapp.database.manager.EtalaseDbManager;
import com.tokopedia.posapp.database.manager.ProductDbManager;
import com.tokopedia.posapp.domain.model.DataStatus;

import rx.Observable;
import rx.functions.Func3;

/**
 * Created by okasurya on 10/5/17.
 */

class PosCacheHandler {
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
