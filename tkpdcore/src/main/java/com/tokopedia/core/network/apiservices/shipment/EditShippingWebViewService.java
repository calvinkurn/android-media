package com.tokopedia.core.network.apiservices.shipment;

import com.tokopedia.core.network.apiservices.shipment.apis.EditShippingApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Kris on 5/13/2016.
 */
public class EditShippingWebViewService extends AuthService<EditShippingApi> {

    public EditShippingWebViewService() {
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(EditShippingApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_SHIPPING_WEBVIEW;
    }

    @Override
    public EditShippingApi getApi() {
        return api;
    }
}
