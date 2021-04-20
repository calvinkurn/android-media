package com.tokopedia.logisticCommon.data.entity.address;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetPeopleAddress {

    @SerializedName("list")
    @Expose
    private List<AddressModel> list = new ArrayList<>();

    @SerializedName("paging")
    @Expose
    private Paging paging;

    @SerializedName("token")
    @Expose
    private Token token;

    /**
     *
     * @return
     *     The paging
     */
    public Paging getPaging() {
        return paging;
    }

    /**
     *
     * @param paging
     *     The paging
     */
    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    /**
     *
     * @return
     *     The list
     */
    public List<AddressModel> getList() {
        return list;
    }

    /**
     *
     * @param list
     *     The list
     */
    public void setList(List<AddressModel> list) {
        this.list = list;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
