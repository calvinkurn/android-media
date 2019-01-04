package com.tokopedia.digital.nostylecategory.digitalcategory.data.api.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rizky on 30/08/18.
 */
public class ResponseAgentDigitalCategory {

    @SerializedName("rechargeCategoryDetail")
    @Expose
    private ResponseRechargeCategoryDetail rechargeCategoryDetail;

    public ResponseRechargeCategoryDetail getRechargeCategoryDetail() {
        return rechargeCategoryDetail;
    }

}