
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("pagination")
    @Expose
    private LinksPagination pagination;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public LinksPagination getPagination() {
        return pagination;
    }

    public void setPagination(LinksPagination pagination) {
        this.pagination = pagination;
    }

}
