package com.tokopedia.tkpd.manage.people.address.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetPeopleAddress {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("list")
    @Expose
    private List<AddressModel> list = new ArrayList<>();

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

}
