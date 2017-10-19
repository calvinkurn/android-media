package com.tokopedia.posapp.data.mapper;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.posapp.data.pojo.etalase.EtalaseItemResponse;
import com.tokopedia.posapp.data.pojo.etalase.EtalaseResponse;
import com.tokopedia.posapp.domain.model.shop.EtalaseDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 9/19/17.
 */

public class GetEtalaseMapper implements Func1<Response<TkpdResponse>, List<EtalaseDomain>> {
    private Gson gson;

    public GetEtalaseMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<EtalaseDomain> call(Response<TkpdResponse> response) {
        if(response.isSuccessful()
                && response.body() != null) {
            EtalaseResponse etalaseResponse = response.body().convertDataObj(EtalaseResponse.class);
            if(etalaseResponse != null
                    && etalaseResponse.getData() != null) {
                return mapToDomain(etalaseResponse.getData());
            }
        }
        return null;
    }

    private List<EtalaseDomain> mapToDomain(List<EtalaseItemResponse> etalaseItems) {
        List<EtalaseDomain> domains = new ArrayList<>();
        for(EtalaseItemResponse item: etalaseItems) {
            EtalaseDomain etalaseDomain = new EtalaseDomain();
            etalaseDomain.setEtalaseId(item.getMenuId());
            etalaseDomain.setEtalaseName(item.getMenuName());
            etalaseDomain.setEtalaseAlias(item.getMenuAlias());
            etalaseDomain.setUseAce(item.getUseAce());
            domains.add(etalaseDomain);
        }

        return domains;
    }
}
