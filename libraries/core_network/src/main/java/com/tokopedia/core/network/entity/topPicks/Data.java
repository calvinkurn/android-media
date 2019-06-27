
package com.tokopedia.core.network.entity.topPicks;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Deprecated
public class Data {

    @SerializedName("groups")
    @Expose
    private List<Group> groups = null;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
