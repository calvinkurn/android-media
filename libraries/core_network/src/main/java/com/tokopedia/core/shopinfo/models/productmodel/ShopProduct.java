
package com.tokopedia.core.shopinfo.models.productmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.PagingHandler;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ShopProduct {

    @SerializedName("search_url")
    @Expose
    private String searchUrl;
    @SerializedName("share_url")
    @Expose
    private String shareUrl;
    @SerializedName("paging")
    @Expose
    private PagingHandler.PagingHandlerModel paging;
    @SerializedName("st")
    @Expose
    private String st;
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<Product>();
    @SerializedName("has_catalog")
    @Expose
    private int hasCatalog;
    @SerializedName("hashtag")
    @Expose
    private Object hashtag;
    @SerializedName("breadcrumb")
    @Expose
    private Object breadcrumb;
    @SerializedName("department_id")
    @Expose
    private int departmentId;
    @SerializedName("locations")
    @Expose
    private Object locations;

    /**
     * 
     * @return
     *     The searchUrl
     */
    public String getSearchUrl() {
        return searchUrl;
    }

    /**
     * 
     * @param searchUrl
     *     The search_url
     */
    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    /**
     * 
     * @return
     *     The shareUrl
     */
    public String getShareUrl() {
        return shareUrl;
    }

    /**
     * 
     * @param shareUrl
     *     The share_url
     */
    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    /**
     * 
     * @return
     *     The paging
     */
    public PagingHandler.PagingHandlerModel getPaging() {
        return paging;
    }

    /**
     * 
     * @param paging
     *     The paging
     */
    public void setPaging(PagingHandler.PagingHandlerModel paging) {
        this.paging = paging;
    }

    /**
     * 
     * @return
     *     The st
     */
    public String getSt() {
        return st;
    }

    /**
     * 
     * @param st
     *     The st
     */
    public void setSt(String st) {
        this.st = st;
    }

    /**
     * 
     * @return
     *     The products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * 
     * @param products
     *     The products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

    /**
     * 
     * @return
     *     The hasCatalog
     */
    public int getHasCatalog() {
        return hasCatalog;
    }

    /**
     * 
     * @param hasCatalog
     *     The has_catalog
     */
    public void setHasCatalog(int hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    /**
     * 
     * @return
     *     The hashtag
     */
    public Object getHashtag() {
        return hashtag;
    }

    /**
     * 
     * @param hashtag
     *     The hashtag
     */
    public void setHashtag(Object hashtag) {
        this.hashtag = hashtag;
    }

    /**
     * 
     * @return
     *     The breadcrumb
     */
    public Object getBreadcrumb() {
        return breadcrumb;
    }

    /**
     * 
     * @param breadcrumb
     *     The breadcrumb
     */
    public void setBreadcrumb(Object breadcrumb) {
        this.breadcrumb = breadcrumb;
    }

    /**
     * 
     * @return
     *     The departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * 
     * @param departmentId
     *     The department_id
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 
     * @return
     *     The locations
     */
    public Object getLocations() {
        return locations;
    }

    /**
     * 
     * @param locations
     *     The locations
     */
    public void setLocations(Object locations) {
        this.locations = locations;
    }

}
