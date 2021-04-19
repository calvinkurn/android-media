package com.tokopedia.linker.model;

import java.util.List;

public class RechargeLinkerData {

    private LinkerData linkerData;
    private List<String> categoryIds;

    public LinkerData getLinkerData() {
        return linkerData;
    }

    public void setLinkerData(LinkerData linkerData) {
        this.linkerData = linkerData;
    }

    public String getCategoryIds() {
        return categoryIds.toString();
    }

    public void setCategoryIds(List<String> categoryId) {
        this.categoryIds = categoryId;
    }

}
