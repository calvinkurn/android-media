package com.tokopedia.affiliate.feature.explore.view.viewmodel;

import java.util.ArrayList;

/**
 * @author by yfsx on 08/10/18.
 */
public class ExploreParams {
    private String keyword;
    private ArrayList<ExploreFilter> filter;
    private ExploreSort sort;
    private boolean isCanLoadMore;
    private String cursor;

    public ExploreParams() {
        resetParams();
    }

    public void resetParams() {
        keyword = "";
        filter = new ArrayList<>();
        sort = new ExploreSort("", false);
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

    public ArrayList<ExploreFilter> getFilter() {
        return filter;
    }

    public void setFilter(ArrayList<ExploreFilter> filter) {
        this.filter = filter;
    }

    public ExploreSort getSort() {
        return sort;
    }

    public void setSort(ExploreSort sort) {
        this.sort = sort;
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
