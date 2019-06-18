
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokopointsSectionOuter {

    @SerializedName("tokopointsHomepage")
    private TokopointsSection sectionContent;

    public TokopointsSection getSectionContent() {
        return sectionContent;
    }

    public void setSectionContent(TokopointsSection sectionContent) {
        this.sectionContent = sectionContent;
    }
}
