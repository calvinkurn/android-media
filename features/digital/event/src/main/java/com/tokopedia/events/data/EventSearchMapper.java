package com.tokopedia.events.data;

import android.util.Log;

import com.tokopedia.events.data.entity.response.searchresponse.FiltersItem;
import com.tokopedia.events.data.entity.response.searchresponse.GridLayoutItem;
import com.tokopedia.events.data.entity.response.searchresponse.SearchResponse;
import com.tokopedia.events.data.entity.response.searchresponse.ValuesItem;
import com.tokopedia.events.domain.model.EventsItemDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.FilterDomainModel;
import com.tokopedia.events.domain.model.searchdomainmodel.PageDomain;
import com.tokopedia.events.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.events.domain.model.searchdomainmodel.ValuesItemDomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaymohapatra on 15/01/18.
 */

public class EventSearchMapper {
    public static EventSearchMapper singletonInstance;

    synchronized public static EventSearchMapper getSearchMapper() {
        if (singletonInstance == null)
            singletonInstance = new EventSearchMapper();
        return singletonInstance;
    }

    private EventSearchMapper() {
        Log.d("EventSearchMapper", "EventSearchMapper instance");
    }

    public SearchDomainModel convertToSearchDomain(SearchResponse source) {
        SearchDomainModel target = new SearchDomainModel();
        List<EventsItemDomain> eventsItemDomains = new ArrayList<>();
        List<FilterDomainModel> filterDomainModels = new ArrayList<>();
        try {
            for (GridLayoutItem eventItem : source.getGridLayout()) {
                EventsItemDomain itemDomain = new EventsItemDomain();
                itemDomain.setActionText(eventItem.getActionText());
                itemDomain.setAutocode(eventItem.getAutocode());
                itemDomain.setCategoryId(eventItem.getCategoryId());
                itemDomain.setCensor(eventItem.getCensor());
                itemDomain.setChildCategoryIds(eventItem.getChildCategoryIds());
                itemDomain.setCityIds(eventItem.getCityIds());
                itemDomain.setCityName(eventItem.getCityName());
                itemDomain.setConvenienceFee(eventItem.getConvenienceFee());
                itemDomain.setCustomLabels(eventItem.getCustomLabels());
                itemDomain.setDateRange(eventItem.isDateRange());
                itemDomain.setDisplayName(eventItem.getDisplayName());
                itemDomain.setDisplayTags(eventItem.getDisplayTags());
                itemDomain.setDuration(eventItem.getDuration());
                itemDomain.setForm(eventItem.getForm());
                itemDomain.setForms(eventItem.getForms());
                itemDomain.setGenre(eventItem.getGenre());
                itemDomain.setHasSeatLayout(eventItem.getHasSeatLayout());
                itemDomain.setId(eventItem.getId());
                itemDomain.setImageApp(eventItem.getImageApp());
                itemDomain.setImageWeb(eventItem.getImageWeb());
                itemDomain.setIsFeatured(eventItem.getIsFeatured());
                itemDomain.setIsFoodAvailable(eventItem.getIsFoodAvailable());
                itemDomain.setIsPromo(eventItem.getIsPromo());
                itemDomain.setIsSearchable(eventItem.getIsSearchable());
                itemDomain.setIsTop(eventItem.getIsTop());
                itemDomain.setLongRichDesc(eventItem.getLongRichDesc());
                itemDomain.setMaxEndDate(eventItem.getMaxEndDate());
                itemDomain.setMaxEndTime(eventItem.getMaxEndTime());
                itemDomain.setMetaDescription(eventItem.getMetaDescription());
                itemDomain.setMetaKeywords(eventItem.getMetaKeywords());
                itemDomain.setMetaTitle(eventItem.getMetaTitle());
                itemDomain.setMinStartDate(eventItem.getMinStartDate());
                itemDomain.setMinStartTime(eventItem.getMinStartTime());
                itemDomain.setMrp(eventItem.getMrp());
                itemDomain.setParentId(eventItem.getParentId());
                itemDomain.setProviderId(eventItem.getProviderId());
                itemDomain.setOfferText(eventItem.getOfferText());
                itemDomain.setPriority(eventItem.getPriority());
                itemDomain.setPromotionText(eventItem.getPromotionText());
                itemDomain.setProviderProductCode(eventItem.getProviderProductCode());
                itemDomain.setProviderProductId(eventItem.getProviderProductId());
                itemDomain.setProviderProductName(eventItem.getProviderProductName());
                itemDomain.setQuantity(eventItem.getQuantity());
                itemDomain.setRating(eventItem.getRating());
                itemDomain.setRedirect(eventItem.getRedirect());
                itemDomain.setSaleEndDate(eventItem.getSaleEndDate());
                itemDomain.setSaleEndTime(eventItem.getSaleEndTime());
                itemDomain.setSalesPrice(eventItem.getSalesPrice());
                itemDomain.setSaleStartDate(eventItem.getSaleStartDate());
                itemDomain.setSaleStartTime(eventItem.getSaleStartTime());
                itemDomain.setSalientFeatures(eventItem.getSalientFeatures());
                itemDomain.setSchedules(eventItem.getSchedules());
                itemDomain.setSearchTags(eventItem.getSearchTags());
                itemDomain.setSellRate(eventItem.getSellRate());
                itemDomain.setStatus(eventItem.getStatus());
                itemDomain.setSeatChartTypeId(eventItem.getSeatChartTypeId());
                itemDomain.setUrl(eventItem.getUrl());
                itemDomain.setTnc(eventItem.getTnc());
                itemDomain.setThumbnailApp(eventItem.getThumbnailApp());
                itemDomain.setThumbnailWeb(eventItem.getThumbnailWeb());
                itemDomain.setThumbsDown(eventItem.getThumbsDown());
                itemDomain.setThumbsUp(eventItem.getThumbsUp());
                itemDomain.setSoldQuantity(eventItem.getSoldQuantity());
                itemDomain.setTitle(eventItem.getTitle());
                itemDomain.setShortDesc(eventItem.getShortDesc());

                eventsItemDomains.add(itemDomain);
            }
            target.setEvents(eventsItemDomains);
        } catch (IndexOutOfBoundsException e) {
            Log.d("SearchMapper", "IOB 1");
            e.printStackTrace();
        } catch (NullPointerException e) {
            Log.d("SearchMapper", "NPE 1");
            e.printStackTrace();
        }

        try {
            for (FiltersItem filtersItem : source.getFilters()) {
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
        pageDomain.setUriNext(source.getPage().getUriNext());
        pageDomain.setUriPrev(source.getPage().getUriPrev());
        target.setPage(pageDomain);
        return target;
    }

}
