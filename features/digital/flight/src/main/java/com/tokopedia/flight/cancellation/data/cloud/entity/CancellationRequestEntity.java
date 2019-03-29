package com.tokopedia.flight.cancellation.data.cloud.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by furqan on 11/04/18.
 */

public class CancellationRequestEntity {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;

}
