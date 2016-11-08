
package com.tokopedia.core.recharge.model.status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attributes {

    @SerializedName("version")
    @Expose
    private Version version;
    @SerializedName("is_maintenance")
    @Expose
    private Boolean isMaintenance;

    /**
     * 
     * @return
     *     The version
     */
    public Version getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    public void setVersion(Version version) {
        this.version = version;
    }

    /**
     * 
     * @return
     *     The isMaintenance
     */
    public Boolean getIsMaintenance() {
        return isMaintenance;
    }

    /**
     * 
     * @param isMaintenance
     *     The is_maintenance
     */
    public void setIsMaintenance(Boolean isMaintenance) {
        this.isMaintenance = isMaintenance;
    }

}
