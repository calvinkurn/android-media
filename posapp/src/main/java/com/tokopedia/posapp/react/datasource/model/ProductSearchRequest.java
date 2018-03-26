package com.tokopedia.posapp.react.datasource.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by okasurya on 9/28/17.
 */

public class ProductSearchRequest {
    @SerializedName("keyword")
    @Expose
    private String keyword;

    @SerializedName("etalase_id")
    @Expose
    private String etalaseId;

    @SerializedName("offset")
    @Expose
    private Integer offset;

    @SerializedName("limit")
    @Expose
    private Integer limit;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getEtalaseId() {
        return etalaseId;
    }

    public void setEtalaseId(String etalaseId) {
        this.etalaseId = etalaseId;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
