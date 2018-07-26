package com.tokopedia.shop.etalase.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.PagingList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by normansyahputa on 2/28/18.
 */

public class PagingListOther<T> extends PagingList<T> {
    @SerializedName("list_other")
    @Expose
    private List<T> listOther = new ArrayList();

    public List<T> getListOther() {
        return listOther;
    }

    public void setListOther(List<T> list) {
        this.listOther = list;
    }
}
