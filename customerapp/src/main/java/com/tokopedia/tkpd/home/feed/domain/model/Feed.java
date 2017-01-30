package com.tokopedia.tkpd.home.feed.domain.model;

import java.util.List;

/**
 * @author Kulomady on 12/8/16.
 */

public class Feed {
    private List<ProductFeed> mProducts;
    private Paging mPaging;
    private String shareUrl;
    private String locations;
    private String departmentId;
    private String hashtag;
    private String hasCatalog;
    private String searchTitle;
    private String breadcrumb;

    private boolean isValid;

    public boolean isValid() {
        return isValid;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public List<ProductFeed> getProducts() {
        return mProducts;
    }

    public void setProducts(List<ProductFeed> products) {
        mProducts = products;
    }

    public Paging getPaging() {
        return mPaging;
    }

    public void setPaging(Paging paging) {
        mPaging = paging;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getHasCatalog() {
        return hasCatalog;
    }

    public void setHasCatalog(String hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public String getBreadcrumb() {
        return breadcrumb;
    }

    public void setBreadcrumb(String breadcrumb) {
        this.breadcrumb = breadcrumb;
    }
}
