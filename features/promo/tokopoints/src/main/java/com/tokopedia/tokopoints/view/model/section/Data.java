
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("tokopointsSection")
    @Expose
    private TokopointsSection tokopointsSection;

    public TokopointsSection getTokopointsSection() {
        return tokopointsSection;
    }

    public void setTokopointsSection(TokopointsSection tokopointsSection) {
        this.tokopointsSection = tokopointsSection;
    }

}
