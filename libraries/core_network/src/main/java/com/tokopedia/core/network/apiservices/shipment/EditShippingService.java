package com.tokopedia.core.network.apiservices.shipment;

import com.tokopedia.core.network.apiservices.shipment.apis.EditShippingApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.services.AuthService;

import retrofit2.Retrofit;

/**
 * @author Kris on 2/22/2016.
 */

@Deprecated
public class EditShippingService extends AuthService<EditShippingApi>{

    public EditShippingService(){
        super();
    }

    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(EditShippingApi.class);
    }

    @Override
    protected String getBaseUrl() {
        return TkpdBaseURL.Shop.URL_MY_SHOP_SHIPMENT;
    }

    @Override
    public EditShippingApi getApi() {
        return api;
    }
}
