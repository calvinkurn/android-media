package com.tokopedia.core.product.interactor;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.product.model.productdetail.promowidget.DataPromoWidget;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoAttributes;
import com.tokopedia.core.product.model.productdetail.promowidget.PromoWidget;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoWidgetMapper implements Func1<Response<TkpdResponse>, DataPromoWidget> {

    public PromoWidgetMapper() {
    }

    @Override
    public DataPromoWidget call(Response<TkpdResponse> response) {
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                return new GsonBuilder().create().fromJson(response.body().getStringData(), DataPromoWidget.class);
            }
        }
        return null;
    }

}