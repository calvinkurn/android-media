package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

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

    @SerializedName("operator_label")
    @Expose
    private String operatorLabel;

    @SerializedName("operator_style")
    @Expose
    private String operatorStyle;

    @SerializedName("default_operator_id")
    @Expose
    private String defaultOperatorId;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("render_operator")
    @Expose
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

    public String getOperatorLabel() {
        return operatorLabel;
    }

    public String getOperatorStyle() {
        return operatorStyle;
    }

    public String getDefaultOperatorId() {
        return defaultOperatorId;
    }

    public String getIcon() {
        return icon;
    }

    public ResponseRenderOperator getRenderOperator() {
        return renderOperator;
    }

}
