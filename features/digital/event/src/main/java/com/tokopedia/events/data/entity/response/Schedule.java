
package com.tokopedia.events.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Schedule {

    @SerializedName("schedule")
    @Expose
    private Schedule_ schedule;
    @SerializedName("address_detail")
    @Expose
    private AddressDetail addressDetail;
    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;

    public Schedule_ getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule_ schedule) {
        this.schedule = schedule;
    }

    public AddressDetail getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(AddressDetail addressDetail) {
        this.addressDetail = addressDetail;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
