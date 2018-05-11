package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.HomeResponse;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DealsEntityMapper {

    public DealsDomain transform(DealsResponse dealsResponse) {
        HomeResponse homeResponse = dealsResponse.getData().getHome();

        DealsDomain dealsDomain = new DealsDomain();
        JsonObject layout = homeResponse.getLayout();
        List<DealsCategoryDomain> dealsCategoryDomains = new ArrayList<>();
        DealsCategoryDomain dealsCategoryDomain;
        for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
            JsonObject object = entry.getValue().getAsJsonObject();
            dealsCategoryDomain = new Gson().fromJson(object, DealsCategoryDomain.class);
            dealsCategoryDomains.add(dealsCategoryDomain);
        }
        dealsDomain.setDealsCategory(dealsCategoryDomains);


        JsonArray brands = dealsResponse.getData().getBrands();
        List<BrandDomain> dealsBrandsDomain = new ArrayList<>();
        BrandDomain brandDomain;
        for (JsonElement jsonElement : brands) {
            brandDomain = new Gson().fromJson(jsonElement.getAsJsonObject(), BrandDomain.class);
            dealsBrandsDomain.add(brandDomain);
        }
        dealsDomain.setDealsBrands(dealsBrandsDomain);

        return dealsDomain;
    }
}
