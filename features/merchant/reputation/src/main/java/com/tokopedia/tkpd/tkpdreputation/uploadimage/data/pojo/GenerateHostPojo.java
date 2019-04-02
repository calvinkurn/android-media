package com.tokopedia.tkpd.tkpdreputation.uploadimage.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 9/5/17.
 */

public class GenerateHostPojo {
    @SerializedName("generated_host")
    @Expose
    private GeneratedHost generatedHost;

    public GeneratedHost getGeneratedHost() {
        return generatedHost;
    }

    public void setGeneratedHost(GeneratedHost generatedHost) {
        this.generatedHost = generatedHost;
    }
}
