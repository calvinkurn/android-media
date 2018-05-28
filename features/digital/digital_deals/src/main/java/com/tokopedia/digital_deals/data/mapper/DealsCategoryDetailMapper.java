package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tokopedia.digital_deals.data.entity.response.categorydetailresponse.CategoryResponse;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class DealsCategoryDetailMapper implements Func1<CategoryResponse, CategoryDetailsDomain> {

    @Override
    public CategoryDetailsDomain call(CategoryResponse categoryResponse) {

        CategoryDetailsDomain categoryDetailsDomain = new CategoryDetailsDomain();
        JsonArray brands = categoryResponse.getData().getBrands();
        JsonArray deals = categoryResponse.getData().getDeals();
        List<DealsCategoryItemDomain> dealsCategoryItemDomains = new ArrayList<>();
        DealsCategoryItemDomain dealsCategoryDomain;
        if (deals != null) {
            for (JsonElement entry : deals) {

                dealsCategoryDomain = new Gson().fromJson(entry.getAsJsonObject(), DealsCategoryItemDomain.class);
                dealsCategoryItemDomains.add(dealsCategoryDomain);
            }
        }
        categoryDetailsDomain.setDealItems(dealsCategoryItemDomains);

        List<BrandDomain> dealsBrandsDomain = new ArrayList<>();
        BrandDomain brandDomain;
        for (JsonElement jsonElement : brands) {
            brandDomain = new Gson().fromJson(jsonElement.getAsJsonObject(), BrandDomain.class);
            dealsBrandsDomain.add(brandDomain);
        }
        categoryDetailsDomain.setDealBrands(dealsBrandsDomain);

        JsonObject brand = categoryResponse.getData().getPage();
        PageDomain pageDomain = new Gson().fromJson(brand, PageDomain.class);
        categoryDetailsDomain.setPage(pageDomain);

        return categoryDetailsDomain;
    }
}
