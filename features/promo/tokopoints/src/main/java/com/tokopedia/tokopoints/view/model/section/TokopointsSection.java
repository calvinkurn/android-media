
package com.tokopedia.tokopoints.view.model.section;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
