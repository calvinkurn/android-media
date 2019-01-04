
package com.tokopedia.home.beranda.domain.gql.feed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pagination {

    @SerializedName("__typename")
    @Expose
    private String typename;
    @SerializedName("current_page")
    @Expose
    private int currentPage;
    @SerializedName("next_page")
    @Expose
    private int nextPage;
    @SerializedName("prev_page")
    @Expose
    private int prevPage;

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }

}
