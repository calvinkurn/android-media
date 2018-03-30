package com.tokopedia.posapp.outlet.data.mapper;

import com.tokopedia.posapp.base.data.pojo.PosResponse;
import com.tokopedia.posapp.outlet.data.pojo.OutletItemResponse;
import com.tokopedia.posapp.outlet.data.pojo.OutletResponse;
import com.tokopedia.posapp.outlet.domain.model.OutletItemDomain;
import com.tokopedia.posapp.outlet.domain.model.OutletDomain;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 7/31/17.
 */

public class GetOutletMapper implements Func1<Response<PosResponse<OutletResponse>>, OutletDomain> {

    @Inject
    public GetOutletMapper() {
    }

    @Override
    public OutletDomain call(Response<PosResponse<OutletResponse>> response) {
        if(response.body() != null && response.isSuccessful()) {
            OutletResponse outletResponse = response.body().getData();

            if(outletResponse != null && outletResponse.getList() != null) {
                return getOutletFromResponse(outletResponse);
            }
        }

        return null;
    }

    private OutletDomain getOutletFromResponse(OutletResponse outletResponse) {
        OutletDomain outletDomain = new OutletDomain();
        outletDomain.setListOutlet(new ArrayList<OutletItemDomain>());
        for(OutletItemResponse addressModel: outletResponse.getList()) {
            OutletItemDomain outlet = new OutletItemDomain();
            outlet.setOutletId(addressModel.getOutletId());
            outlet.setOutletAddres(addressModel.getAddress());
            outlet.setOutletName(addressModel.getName());
            outlet.setOutletPhone("");
            outletDomain.getListOutlet().add(outlet);
        }

        outletDomain.setUriNext("");
        outletDomain.setUriPrevious("");

        return outletDomain;
    }

}
