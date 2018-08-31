package com.tokopedia.mitra.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 30/08/18.
 */
public class ResponseRechargeCategoryDetail {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("title")
    @Expose
    private String title;

    private String operatorLabel;
    private String operatorStyle;
    private String defaultOperatorId;
    private String icon;
    private ResponseRenderOperator renderOperator;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

}
