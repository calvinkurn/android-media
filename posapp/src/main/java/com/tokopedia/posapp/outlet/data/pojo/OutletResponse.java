package com.tokopedia.posapp.outlet.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.manage.people.address.model.AddressModel;
import com.tokopedia.core.manage.people.address.model.Paging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by okasurya on 7/31/17.
 */

public class OutletResponse {
    @SerializedName("paging")
    @Expose
    private Paging paging;

    @SerializedName("list")
    @Expose
    private List<AddressModel> list = new ArrayList<>();

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<AddressModel> getList() {
        return list;
    }

    public void setList(List<AddressModel> list) {
        this.list = list;
    }
}
