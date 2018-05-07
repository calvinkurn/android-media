package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 4/28/17.
 */

public class Banner {

    @SerializedName("data")
    @Expose
    private List<RelationData> data = new ArrayList<>();

    public List<RelationData> getData() {
        return data;
    }
}
