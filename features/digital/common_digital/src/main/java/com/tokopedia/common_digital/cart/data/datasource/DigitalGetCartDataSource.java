package com.tokopedia.common_digital.cart.data.datasource;

import android.support.annotation.NonNull;

import com.tokopedia.common_digital.cart.data.entity.response.ResponseCartData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.cart.view.model.CartDigitalInfoData;
import com.tokopedia.common_digital.common.data.api.DigitalRestApi;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.network.utils.TKPDMapParam;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Rizky on 27/08/18.
 */
public class DigitalGetCartDataSource {

    private DigitalRestApi digitalRestApi;
    private ICartMapperData cartMapperData;

    public DigitalGetCartDataSource(DigitalRestApi digitalRestApi, ICartMapperData cartMapperData) {
        this.digitalRestApi = digitalRestApi;
        this.cartMapperData = cartMapperData;
    }

    public Observable<CartDigitalInfoData> getCart(String categoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", categoryId);
        return digitalRestApi.getCart(param).map(getFuncResponseToCartDigitalInfoData());
    }

    @NonNull
    private Func1<Response<TkpdDigitalResponse>, CartDigitalInfoData>
    getFuncResponseToCartDigitalInfoData() {
        return tkpdDigitalResponseResponse -> cartMapperData.transformCartInfoData(
                tkpdDigitalResponseResponse.body().convertDataObj(
                        ResponseCartData.class
                )
        );
    }

}
