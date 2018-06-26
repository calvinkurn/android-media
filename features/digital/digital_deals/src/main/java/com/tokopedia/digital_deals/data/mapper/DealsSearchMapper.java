package com.tokopedia.digital_deals.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;


public class DealsSearchMapper  implements Func1<SearchResponse, SearchDomainModel> {
    @Override
    public SearchDomainModel call(SearchResponse searchResponseEntity) {
        CommonUtils.dumper("inside SearchResponseEntity = " + searchResponseEntity);
        SearchDomainModel searchDomainModel = new SearchDomainModel();
        List<DealsCategoryItemDomain> dealsCategoryItemDomains = new ArrayList<>();
        DealsCategoryItemDomain dealsCategoryDomain;
        if (searchResponseEntity.getDeals() != null) {
            for (JsonElement entry : searchResponseEntity.getDeals()) {

                dealsCategoryDomain = new Gson().fromJson(entry.getAsJsonObject(), DealsCategoryItemDomain.class);
                dealsCategoryItemDomains.add(dealsCategoryDomain);
            }
        }
        searchDomainModel.setDeals(dealsCategoryItemDomains);


        List<FilterDomainModel> domainModels = new ArrayList<>();
        FilterDomainModel domainModel;
        if(searchResponseEntity.getFilters()!=null) {
            for (JsonElement jsonElement : searchResponseEntity.getFilters()) {
                domainModel = new Gson().fromJson(jsonElement.getAsJsonObject(), FilterDomainModel.class);
                domainModels.add(domainModel);
            }
        }
        searchDomainModel.setFilters(domainModels);

        if(searchResponseEntity.getPage()!=null) {
            PageDomain pageDomain = new Gson().fromJson(searchResponseEntity.getPage(), PageDomain.class);

            searchDomainModel.setPage(pageDomain);
        }
        searchDomainModel.setCount(searchResponseEntity.getCount());

        return searchDomainModel;
    }
}
