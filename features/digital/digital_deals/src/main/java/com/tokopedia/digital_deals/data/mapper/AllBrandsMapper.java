package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.allbrandsresponse.AllBrandsResponse;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class AllBrandsMapper implements Func1<AllBrandsResponse, AllBrandsDomain> {

    @Override
    public AllBrandsDomain call(AllBrandsResponse categoryResponse) {

        AllBrandsDomain allBrandsDomain = new AllBrandsDomain();
        JsonArray brands = categoryResponse.getData().getBrands();


        List<BrandDomain> dealsBrandsDomain = new ArrayList<>();
        BrandDomain brandDomain;
        for (JsonElement jsonElement : brands) {
            brandDomain = new Gson().fromJson(jsonElement.getAsJsonObject(), BrandDomain.class);
            dealsBrandsDomain.add(brandDomain);
        }
        allBrandsDomain.setBrands(dealsBrandsDomain);

        JsonObject brand = categoryResponse.getData().getPage();
        PageDomain pageDomain = new Gson().fromJson(brand, PageDomain.class);
        allBrandsDomain.setPage(pageDomain);

        return allBrandsDomain;
    }
}
