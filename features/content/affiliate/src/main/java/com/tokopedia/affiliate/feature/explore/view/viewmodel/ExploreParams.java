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
    private boolean isLoading;

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

    public void resetSearch() {
        this.keyword = "";
        setFirstData();
    }

    public String getKeyword() {
        return keyword;
    }

    public ArrayList<ExploreFilter> getFilter() {
        return filter;
    }

    public ExploreSort getSort() {
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
