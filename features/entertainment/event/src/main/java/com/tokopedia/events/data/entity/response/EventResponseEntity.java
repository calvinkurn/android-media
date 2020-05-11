
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResponseEntity {

    @SerializedName("filters")
    @Expose
    private List<FilterResponseEntity> filters = null;
    @SerializedName("home")
    @Expose
    private HomeResponseEntity home;

    public List<FilterResponseEntity> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterResponseEntity> filters) {
        this.filters = filters;
    }

    public HomeResponseEntity getHome() {
        return home;
    }

    public void setHome(HomeResponseEntity home) {
        this.home = home;
    }

}
