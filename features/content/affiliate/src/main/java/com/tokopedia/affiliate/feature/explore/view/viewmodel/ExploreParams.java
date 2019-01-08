package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreParams {
    private String keyword;
    private FilterViewModel filter;
    private SortViewModel sort;
    private boolean isCanLoadMore;
    private String cursor;
    private boolean isLoading;

    public ExploreParams() {
        resetParams();
    }

    public void resetParams() {
        keyword = "";
        filter = new FilterViewModel();
        sort = new SortViewModel();
        isCanLoadMore = true;
        cursor = "";
    }

    public void setFirstData() {
        isCanLoadMore = true;
        cursor = "";
        resetFilterSort();
    }

    public void setSearchParam(String keyword) {
        this.keyword = keyword;
        setFirstData();
    }

    public void disableLoadMore() {
        this.isCanLoadMore = false;
        this.cursor = "";
    }

    public void setCursorForLoadMore(String cursor) {
        this.isCanLoadMore = true;
        this.cursor = cursor;
    }

    public void resetSearch() {
        this.keyword = "";
        setFirstData();
    }

    public void setFilter(FilterViewModel filter) {
        this.filter = filter;
    }

    public void setSort(SortViewModel sort) {
        this.sort = sort;
    }

    public String getKeyword() {
        return keyword;
    }

    private void resetFilterSort() {
        setSort(new SortViewModel());
        setFilter(new FilterViewModel());
    }

    public FilterViewModel getFilter() {
        return filter;
    }

    public SortViewModel getSort() {
        return sort;
    }

    public boolean isCanLoadMore() {
        return isCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        isCanLoadMore = canLoadMore;
    }

    public String getCursor() {
        return cursor;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
