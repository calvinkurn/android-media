package com.tokopedia.digital.widget.data.entity.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nabillasabbaha on 9/20/17.
 */

public class AttributesEntity {

    @SerializedName("version")
    @Expose
    private VersionEntity version;
    @SerializedName("is_maintenance")
    @Expose
    private boolean isMaintenance;

    public VersionEntity getVersion() {
        return version;
    }

    public void setVersion(VersionEntity version) {
        this.version = version;
    }

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }
}
