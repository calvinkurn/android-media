package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author rizkyfadillah on 30/04/18.
 */
public class DigitalCategoryDetailEntity {

    @SerializedName("data")
    private ResponseCategoryDetailData data;

    @SerializedName("included")
    private List<ResponseCategoryDetailIncluded> included;

    public DigitalCategoryDetailEntity(ResponseCategoryDetailData responseCategoryDetailData,
                                       List<ResponseCategoryDetailIncluded> responseCategoryDetailIncludeds) {
        this.data = responseCategoryDetailData;
        this.included = responseCategoryDetailIncludeds;
    }

    public ResponseCategoryDetailData getData() {
        return data;
    }

    public List<ResponseCategoryDetailIncluded> getIncluded() {
        return included;
    }

}
