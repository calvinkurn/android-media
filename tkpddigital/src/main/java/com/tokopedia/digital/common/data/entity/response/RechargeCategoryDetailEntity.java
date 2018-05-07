
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Vishal Gupta 7th May, 2018
 */
public class RechargeCategoryDetailEntity {

    @SerializedName("recharge_category_detail")
    @Expose
    private RechargeCategoryDetail rechargeCategoryDetail;

    public RechargeCategoryDetail getRechargeCategoryDetail() {
        return rechargeCategoryDetail;
    }

    public void setRechargeCategoryDetail(RechargeCategoryDetail rechargeCategoryDetail) {
        this.rechargeCategoryDetail = rechargeCategoryDetail;
    }

}
