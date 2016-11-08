package com.tokopedia.core.network.apiservices.shipment;

import com.tokopedia.core.network.apiservices.shipment.apis.EditShippingApi;
import com.tokopedia.core.network.retrofit.services.BaseService;

import retrofit2.Retrofit;

/**
 * @author Kris on 2/24/2016.
 */
public class EditShippingTestService extends BaseService<EditShippingApi>{


    @Override
    protected void initApiService(Retrofit retrofit) {
        api = retrofit.create(EditShippingApi.class)  ;
    }

    @Override
    protected String getBaseUrl() {
        return "http://private-44b4fe-logistics9.apiary-mock.com/services_v2";
    }

    @Override
    public EditShippingApi getApi() {
        return api;
    }
}
