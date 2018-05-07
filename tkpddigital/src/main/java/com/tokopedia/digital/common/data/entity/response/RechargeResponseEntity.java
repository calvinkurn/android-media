
package com.tokopedia.digital.common.data.entity.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Vishal Gupta 7th May, 2018
 */
public class RechargeResponseEntity {

    @SerializedName("recharge_category_detail")
    @Expose
    private RechargeCategoryDetail rechargeCategoryDetail;

    @SerializedName("recharge_favorite_number")
    @Expose
    private RechargeFavoritNumberResponseEntity rechargeFavoritNumberResponseEntity;

    public RechargeCategoryDetail getRechargeCategoryDetail() {
        return rechargeCategoryDetail;
    }

    public void setRechargeCategoryDetail(RechargeCategoryDetail rechargeCategoryDetail) {
        this.rechargeCategoryDetail = rechargeCategoryDetail;
    }

    public RechargeFavoritNumberResponseEntity getRechargeFavoritNumberResponseEntity() {
        return rechargeFavoritNumberResponseEntity;
    }

    public void setRechargeFavoritNumberResponseEntity(RechargeFavoritNumberResponseEntity rechargeFavoritNumberResponseEntity) {
        this.rechargeFavoritNumberResponseEntity = rechargeFavoritNumberResponseEntity;
    }
}
