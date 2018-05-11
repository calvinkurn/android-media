package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.branddetailsresponse.BrandDetailsResponse;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class BrandDetailsTransformMapper implements Func1<BrandDetailsResponse, BrandDetailsDomain> {
    @Override
    public BrandDetailsDomain call(BrandDetailsResponse brandDetailsResponse) {

        BrandDetailsDomain dealsDomain = new BrandDetailsDomain();
        JsonArray deals = brandDetailsResponse.getData().getDeals();
        List<DealsCategoryItemDomain> dealsCategoryItemDomains = new ArrayList<>();
        DealsCategoryItemDomain dealsCategoryDomain;
        if (deals != null) {
            for (JsonElement entry : deals) {

                dealsCategoryDomain = new Gson().fromJson(entry.getAsJsonObject(), DealsCategoryItemDomain.class);
                dealsCategoryItemDomains.add(dealsCategoryDomain);
            }
        }
        dealsDomain.setDealItems(dealsCategoryItemDomains);


        JsonObject brand = brandDetailsResponse.getData().getBrand();
        BrandDomain brandDomain = new Gson().fromJson(brand, BrandDomain.class);

        dealsDomain.setDealBrand(brandDomain);

        return dealsDomain;
    }
}
