package com.tokopedia.events.domain.model.searchdomainmodel;

import com.tokopedia.events.domain.model.EventsItemDomain;

import java.util.List;

/**
 * Created by pranaymohapatra on 15/01/18.
 */

public class SearchDomainModel {
    private List<EventsItemDomain> events;
    private List<FilterDomainModel> filters;
    private PageDomain page;

    public List<EventsItemDomain> getEvents() {
        return events;
    }

    public void setEvents(List<EventsItemDomain> events) {
        this.events = events;
    }

    public List<FilterDomainModel> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDomainModel> filters) {
        this.filters = filters;
    }

    public PageDomain getPage() {
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }

}
