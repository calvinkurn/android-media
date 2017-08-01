package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.OutletResponse;
import com.tokopedia.posapp.domain.model.outlet.DataOutletDomain;
import com.tokopedia.posapp.domain.model.outlet.OutletDomain;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletMapper implements Func1<Response<TkpdResponse>, OutletDomain> {

    private final Gson gson;

    public GetOutletMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public OutletDomain call(Response<TkpdResponse> response) {
        return mapResponse(response);
    }

    private OutletDomain mapResponse(Response<TkpdResponse> response) {
        if(response.body() != null && response.isSuccessful()) {
            OutletResponse outletResponse = gson.fromJson(
                    response.body().getStringData(), OutletResponse.class
            );

            if(outletResponse != null && outletResponse.getList() != null) {
                OutletDomain outletDomain = getOutletFromResponse(outletResponse);
                return outletDomain;
            }
        }

        return null;
    }

    private OutletDomain getOutletFromResponse(OutletResponse outletResponse) {
        OutletDomain outletDomain = new OutletDomain();
        outletDomain.setListOutlet(new ArrayList<DataOutletDomain>());
        for(AddressModel addressModel: outletResponse.getList()) {
            DataOutletDomain outlet = new DataOutletDomain();
            outlet.setOutletId(addressModel.getAddressId());
            outlet.setOutletAddres(addressModel.getAddressStreet());
            outlet.setOutletName(addressModel.getAddressName());
            outlet.setOutletPhone(addressModel.getReceiverPhone());
            outletDomain.getListOutlet().add(outlet);
        }

        outletDomain.setUriNext(outletResponse.getPaging().getUriNext());
        outletDomain.setUriPrevious(outletResponse.getPaging().getUriPrevious());

        return outletDomain;
    }

}
