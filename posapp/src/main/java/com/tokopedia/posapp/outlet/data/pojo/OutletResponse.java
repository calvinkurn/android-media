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
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("outlet_list")
    @Expose
    private List<OutletItemResponse> list = new ArrayList<>();

    public List<OutletItemResponse> getList() {
        return list;
    }

    public void setList(List<OutletItemResponse> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
