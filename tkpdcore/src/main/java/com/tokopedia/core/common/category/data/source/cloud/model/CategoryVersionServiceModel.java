
package com.tokopedia.core.common.category.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryVersionServiceModel {

    @SerializedName("version")
    @Expose
    private Long version;
    @SerializedName("interval")
    @Expose
    private Integer interval;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

}
