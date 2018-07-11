package com.tokopedia.posapp.product.productlist.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 4/13/18.
 */

public class ProductShop {
    @SerializedName("shop_id")
    @Expose
    private long id;
    @SerializedName("shop_name")
    @Expose
    private String name;
    @SerializedName("shop_domain")
    @Expose
    private String domain;
    @SerializedName("shop_url")
    @Expose
    private String url;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
