package com.tokopedia.flight.search.data.api.single.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;

import java.util.List;

/**
 * Created by User on 11/14/2017.
 */

public class FlightDataResponse<T> extends DataResponse<T> {
    @SerializedName(value="meta")
    @Expose
    private Meta meta;
    @SerializedName(value="included")
    @Expose
    private List<Included> included;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<Included> getIncluded() {
        return included;
    }

    public void setIncluded(List<Included> included) {
        this.included = included;
    }
}
