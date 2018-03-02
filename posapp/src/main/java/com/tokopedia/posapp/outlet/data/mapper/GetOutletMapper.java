package com.tokopedia.posapp.outlet.data.mapper;

import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.outlet.data.pojo.OutletResponse;
import com.tokopedia.posapp.outlet.domain.model.OutletItemDomain;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;

import java.util.ArrayList;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletMapper implements Func1<Response<TkpdResponse>, OutletDomain> {

    public GetOutletMapper() {
    }

    @Override
    public OutletDomain call(Response<TkpdResponse> response) {
        return mapResponse(response);
    }

    private OutletDomain mapResponse(Response<TkpdResponse> response) {
        if(response.body() != null && response.isSuccessful()) {
            OutletResponse outletResponse = response.body().convertDataObj(OutletResponse.class);

            if(outletResponse != null && outletResponse.getList() != null) {
                OutletDomain outletDomain = getOutletFromResponse(outletResponse);
                return outletDomain;
            }
        }

        return null;
    }

    private OutletDomain getOutletFromResponse(OutletResponse outletResponse) {
        OutletDomain outletDomain = new OutletDomain();
        outletDomain.setListOutlet(new ArrayList<OutletItemDomain>());
        for(AddressModel addressModel: outletResponse.getList()) {
            OutletItemDomain outlet = new OutletItemDomain();
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
