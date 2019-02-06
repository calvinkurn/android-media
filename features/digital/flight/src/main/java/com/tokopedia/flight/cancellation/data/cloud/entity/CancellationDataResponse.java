package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;

import java.util.List;

/**
 * @author by furqan on 29/10/18.
 */

public class CancellationDataResponse<T> extends DataResponse<T> {
    @SerializedName("included")
    @Expose
    private List<CancellationIncluded> includedList;

    public List<CancellationIncluded> getIncludedList() {
        return includedList;
    }

    public void setIncludedList(List<CancellationIncluded> includedList) {
        this.includedList = includedList;
    }
}
