package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreParams {
    private String keyword;
    private List<FilterViewModel> filters;
    private SortViewModel sort;
    private boolean isCanLoadMore;
    private String cursor;
    private boolean isLoading;

    public ExploreParams() {
        resetParams();
    }

    public void resetParams() {
        keyword = "";
        filters = new ArrayList<>();
        sort = new SortViewModel();
        isCanLoadMore = true;
        cursor = "";
    }

    public void setFirstData() {
        isCanLoadMore = true;
        cursor = "";
        resetFilterSort();
    }

    public void setPullToRefreshData() {
        isCanLoadMore = true;
        cursor = "";
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

    public void resetForFilterClick() {
        isCanLoadMore = true;
        this.cursor = "";
    }


    public void setSort(SortViewModel sort) {
        this.sort = sort;
    }

    public String getKeyword() {
        return keyword;
    }

    private void resetFilterSort() {
        setSort(new SortViewModel());
        setFilters(new ArrayList<>());
    }

    public List<FilterViewModel> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterViewModel> filters) {
        this.filters = filters;
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
