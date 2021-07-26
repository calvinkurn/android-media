package com.tokopedia.linker.model;

import java.util.List;

public class RechargeLinkerData {

    private LinkerData linkerData;
    private String categoryIds;

    public LinkerData getLinkerData() {
        return linkerData;
    }

    public void setLinkerData(LinkerData linkerData) {
        this.linkerData = linkerData;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryId) {
        this.categoryIds = categoryId;
    }

}
