package com.tokopedia.core.product.model.productdetail.promowidget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Data;
import com.tokopedia.core.product.model.productdetail.mosthelpful.Links;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoWidgetResponse {

    @SerializedName("data")
    @Expose
    private DataPromoWidget data;

    public DataPromoWidget getData() {
        return data;
    }

    public void setData(DataPromoWidget data) {
        this.data = data;
    }
}

