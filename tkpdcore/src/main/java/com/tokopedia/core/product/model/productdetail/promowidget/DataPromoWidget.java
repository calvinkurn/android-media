package com.tokopedia.core.product.model.productdetail.promowidget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alifa on 9/13/17.
 */

public class DataPromoWidget {
    @SerializedName("list")
    @Expose
    private List<PromoWidget> promoWidgetList = new ArrayList<PromoWidget>();

    public List<PromoWidget> getPromoWidgetList() {
        return promoWidgetList;
    }

    public void setPromoWidgetList(List<PromoWidget> promoWidgetList) {
        this.promoWidgetList = promoWidgetList;
    }
}
