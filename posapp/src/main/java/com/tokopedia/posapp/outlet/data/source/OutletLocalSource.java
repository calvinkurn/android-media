package com.tokopedia.posapp.outlet.data.source;

import android.text.TextUtils;

import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;
import com.tokopedia.posapp.product.common.ProductConstant;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author okasurya on 5/7/18.
 */

public class OutletLocalSource {
    private PosSessionHandler posSessionHandler;

    @Inject
    OutletLocalSource(PosSessionHandler posSessionHandler) {
        this.posSessionHandler = posSessionHandler;
    }

    public Observable<Boolean> selectOutlet(RequestParams requestParams) {
        return Observable.just(requestParams).map(new Func1<RequestParams, Boolean>() {
            @Override
            public Boolean call(RequestParams requestParams) {
                String id = requestParams.getString(ProductConstant.Key.OUTLET_ID, "");
                if(!TextUtils.isEmpty(id)) {
                    posSessionHandler.setOutletId(id);
                    posSessionHandler.setOutletName(requestParams.getString(ProductConstant.Key.OUTLET_NAME, ""));
                    return true;
                } else return false;
            }
        });
    }

}
