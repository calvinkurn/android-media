
package com.tokopedia.tokopoints.view.model.section;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokopointsSection {

    @SerializedName("sectionContent")
    private List<SectionContent> sectionContent;

    public List<SectionContent> getSectionContent() {
        return sectionContent;
    }

    public void setSectionContent(List<SectionContent> sectionContent) {
        this.sectionContent = sectionContent;
    }

}
