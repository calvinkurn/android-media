
package com.tokopedia.core.database.recharge.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("version")
    @Expose
    private Version version;
    @SerializedName("is_maintenance")
    @Expose
    private boolean isMaintenance;

    /**
     * @return The version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * @return The isMaintenance
     */
    public boolean getIsMaintenance() {
        return isMaintenance;
    }

    /**
     * @param isMaintenance The is_maintenance
     */
    public void setIsMaintenance(boolean isMaintenance) {
        this.isMaintenance = isMaintenance;
    }

}
