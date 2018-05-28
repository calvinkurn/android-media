package com.tokopedia.digital_deals.data.mapper;

import android.util.Log;

import com.tokopedia.digital_deals.data.entity.response.searchresponse.FiltersItem;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.GridLayoutItem;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.digital_deals.data.entity.response.searchresponse.ValuesItem;
import com.tokopedia.digital_deals.domain.model.DealsCategoryItemDomain;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.digital_deals.domain.model.PageDomain;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.digital_deals.domain.model.searchdomainmodel.ValuesItemDomain;

import java.util.ArrayList;
import java.util.List;


public class DealsSearchMapper {
    public static DealsSearchMapper singletonInstance;

    synchronized public static DealsSearchMapper getSearchMapper() {
        if (singletonInstance == null)
            singletonInstance = new DealsSearchMapper();
        return singletonInstance;
    }

    private DealsSearchMapper() {
        Log.d("DealsSearchMapper", "DealsSearchMapper instance");
    }

    public SearchDomainModel convertToSearchDomain(SearchResponse source) {
        SearchDomainModel target = new SearchDomainModel();
        List<DealsCategoryItemDomain> dealsItemDomains = new ArrayList<>();
        List<FilterDomainModel> filterDomainModels = new ArrayList<>();
        try {
            if(source.getData().getGridLayout()!=null) {
                for (GridLayoutItem dealItem : source.getData().getGridLayout()) {
                    DealsCategoryItemDomain itemDomain = new DealsCategoryItemDomain();
                    itemDomain.setCategoryId(dealItem.getCategoryId());
                    itemDomain.setCityName(dealItem.getCityName());

                    itemDomain.setDisplayName(dealItem.getDisplayName());

                    itemDomain.setForms(dealItem.getForms());
                    itemDomain.setId(dealItem.getId());
//                itemDomain.setImageApp(dealItem.getImageApp());
                    itemDomain.setImageWeb(dealItem.getImageWeb());
                    itemDomain.setIsFeatured(dealItem.getIsFeatured());
                    itemDomain.setIsSearchable(dealItem.getIsSearchable());
                    itemDomain.setLongRichDesc(dealItem.getLongRichDesc());
                    itemDomain.setMaxEndDate(dealItem.getMaxEndDate());
                    itemDomain.setMinStartDate(dealItem.getMinStartDate());
                    itemDomain.setMrp(dealItem.getMrp());
                    itemDomain.setProviderId(dealItem.getProviderId());
                    itemDomain.setProviderProductId(dealItem.getProviderProductId());
                    itemDomain.setProviderProductName(dealItem.getProviderProductName());
                    itemDomain.setQuantity(dealItem.getQuantity());
                    itemDomain.setRating(dealItem.getRating());
                    itemDomain.setSaleEndDate(dealItem.getSaleEndDate());
                    itemDomain.setSaleEndTime(dealItem.getSaleEndTime());
                    itemDomain.setSalesPrice(dealItem.getSalesPrice());
                    itemDomain.setSaleStartDate(dealItem.getSaleStartDate());
                    itemDomain.setSaleStartTime(dealItem.getSaleStartTime());
                    itemDomain.setSchedules(dealItem.getSchedules());
                    itemDomain.setSellRate(dealItem.getSellRate());
                    itemDomain.setStatus(dealItem.getStatus());
                    itemDomain.setUrl(dealItem.getUrl());
//                itemDomain.setThumbnailApp(dealItem.getThumbnailApp());
                    itemDomain.setThumbnailWeb(dealItem.getThumbnailWeb());
                    itemDomain.setThumbsDown(dealItem.getThumbsDown());
                    itemDomain.setThumbsUp(dealItem.getThumbsUp());
                    itemDomain.setSoldQuantity(dealItem.getSoldQuantity());
                    itemDomain.setDisplayTags(dealItem.getDisplayTags());
                    BrandDomain brandDomain=new BrandDomain();
                    brandDomain.setTitle(dealItem.getBrand().getTitle());
                    brandDomain.setFeaturedImage(dealItem.getBrand().getFeaturedImage());
                    brandDomain.setFeaturedThumbnailImage(dealItem.getBrand().getFeaturedThumbnailImage());
                    itemDomain.setBrand(brandDomain);

                    dealsItemDomains.add(itemDomain);
                }
            }
            target.setEvents(dealsItemDomains);

        } catch (IndexOutOfBoundsException e) {
            Log.d("SearchMapper", "IOB 1");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("SearchMapper", "NPE 1");
            e.printStackTrace();
        }

        try {
            for (FiltersItem filtersItem : source.getData().getFilters()) {
                if (filtersItem.getName().equals("tags"))
                    continue;
                FilterDomainModel filter = new FilterDomainModel();
                List<ValuesItemDomain> filterValues = new ArrayList<>();
                filter.setName(filtersItem.getName());
                filter.setAttributeName(filtersItem.getAttributeName());
                filter.setLabel(filtersItem.getLabel());
                filter.setKind(filtersItem.getKind());
                for (ValuesItem valuesItem : filtersItem.getValues()) {
                    ValuesItemDomain valuesItemDomain = new ValuesItemDomain();
                    valuesItemDomain.setId(valuesItem.getId());
                    valuesItemDomain.setName(valuesItem.getName());
                    if (filter.getKind().equals("linear-rectangular"))
                        valuesItemDomain.setMulti(true);
                    else if (filter.getKind().equals("linear-radio"))
                        valuesItemDomain.setMulti(false);
                    filterValues.add(valuesItemDomain);
                }
                filter.setValuesItems(filterValues);
                filterDomainModels.add(filter);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("SearchMapper", "IOB 2");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("SearchMapper", "NPE 2");
            e.printStackTrace();
        }
        target.setFilters(filterDomainModels);
        PageDomain pageDomain = new PageDomain();
        pageDomain.setUriNext(source.getData().getPage().getUriNext());
        pageDomain.setUriPrev(source.getData().getPage().getUriPrev());
        target.setPage(pageDomain);
        return target;
    }

}
