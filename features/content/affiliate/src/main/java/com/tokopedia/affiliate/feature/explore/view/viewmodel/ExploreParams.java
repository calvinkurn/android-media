package com.tokopedia.affiliate.feature.explore.view.viewmodel;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreParams {
    private String keyword;
    private String filterKey;
    private String filterValue;
    private String sortKey;
    private boolean sortAsc;
    private boolean isCanLoadMore;
    private String cursor;

    public ExploreParams() {
        resetParams();
    }

    public void resetParams() {
        keyword = "";
        filterKey = "";
        filterValue = "";
        sortKey = "";
        sortAsc = false;
        isCanLoadMore = true;
        cursor = "";
    }

    public void setFirstData() {
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public boolean isSortAsc() {
        return sortAsc;
    }

    public void setSortAsc(boolean sortAsc) {
        this.sortAsc = sortAsc;
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

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
}
