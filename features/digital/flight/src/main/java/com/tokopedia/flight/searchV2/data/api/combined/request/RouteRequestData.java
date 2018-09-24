package com.tokopedia.flight.searchV2.data.api.combined.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 19/09/18.
 */
class RouteRequestData {

    @SerializedName("term")
    @Expose
    private String term;

}
