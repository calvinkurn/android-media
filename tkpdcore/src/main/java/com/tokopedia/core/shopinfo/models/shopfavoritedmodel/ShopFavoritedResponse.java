package com.tokopedia.core.shopinfo.models.shopfavoritedmodel;

/**
 * Created by Alifa on 10/6/2016.
 */
import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopFavoritedResponse {

    @SerializedName("total_page")
    @Expose
    private Integer totalPage;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("list")
    @Expose
    private java.util.List<ShopFavoritee> list = new ArrayList<ShopFavoritee>();

    /**
     *
     * @return
     * The totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     *
     * @param totalPage
     * The total_page
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The list
     */
    public java.util.List<ShopFavoritee> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<ShopFavoritee> list) {
        this.list = list;
    }

}
