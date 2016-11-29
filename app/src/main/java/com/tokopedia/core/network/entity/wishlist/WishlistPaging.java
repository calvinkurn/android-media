package com.tokopedia.core.network.entity.wishlist;

/**
 * Created by ricoharisin on 4/18/16.
 */
public class WishlistPaging {

    Pagination pagination;
    int page = 1;

    public WishlistPaging() {

    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Boolean CheckNextPage() {
        return pagination != null;
    }

    public void nextPage() {
        page++;
    }

    public void resetPage() {
        page = 1;
    }
}
