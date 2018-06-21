package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.DealsResponse;
import com.tokopedia.digital_deals.data.entity.response.homeresponse.HomeResponse;
import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.FilterDomainModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.functions.Func1;

public class DealsTransformMapper implements Func1<DealsResponse, DealsDomain> {

    @Override
    public DealsDomain call(DealsResponse dealsResponse) {
        CommonUtils.dumper("inside DealsResponse = " + dealsResponse);
        HomeResponse homeResponse = dealsResponse.getHome();

        DealsDomain dealsDomain = new DealsDomain();
        JsonObject layout = homeResponse.getLayout();
        List<DealsCategoryDomain> dealsCategoryDomains = new ArrayList<>();
        DealsCategoryDomain dealsCategoryDomain;
        if (layout != null) {
            for (Map.Entry<String, JsonElement> entry : layout.entrySet()) {
                JsonObject object = entry.getValue().getAsJsonObject();
                dealsCategoryDomain = new Gson().fromJson(object, DealsCategoryDomain.class);
                dealsCategoryDomains.add(dealsCategoryDomain);
            }
        }
        dealsDomain.setDealsCategory(dealsCategoryDomains);


        JsonArray brands = dealsResponse.getBrands();
        List<BrandDomain> dealsBrandsDomain = new ArrayList<>();
        BrandDomain brandDomain;
        if (brands != null) {
            for (JsonElement jsonElement : brands) {
                brandDomain = new Gson().fromJson(jsonElement.getAsJsonObject(), BrandDomain.class);
                dealsBrandsDomain.add(brandDomain);
            }
        }
        dealsDomain.setDealsBrands(dealsBrandsDomain);

        JsonArray filters = dealsResponse.getFilters();
        List<FilterDomainModel> filterDomainModels = new ArrayList<>();
        FilterDomainModel filterDomainModel;
        if (filters != null) {
            for (JsonElement jsonElement : filters) {
                filterDomainModel = new Gson().fromJson(jsonElement.getAsJsonObject(), FilterDomainModel.class);
                filterDomainModels.add(filterDomainModel);
            }
        }

        dealsDomain.setFilters(filterDomainModels);
        return dealsDomain;
    }
}
