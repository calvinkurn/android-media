package com.tokopedia.linker.model;

import java.util.List;

public class RechargeLinkerData {

    private LinkerData linkerData;
    private List<String> groupWiseCategoryId;

    public LinkerData getLinkerData() {
        return linkerData;
    }

    public void setLinkerData(LinkerData linkerData) {
        this.linkerData = linkerData;
    }

    public List<String> getCategoryIds() {
        return groupWiseCategoryId;
    }

    public void setCategoryIds(List<String> categoryId) {
        this.groupWiseCategoryId = categoryId;
    }

}
