package com.tokopedia.core.product.model.productdetail.promowidget;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alifa on 9/13/17.
 */

public class PromoWidget {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("attributes")
    @Expose
    private PromoAttributes promoAttributes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PromoAttributes getPromoAttributes() {
        return promoAttributes;
    }

    public void setPromoAttributes(PromoAttributes promoAttributes) {
        this.promoAttributes = promoAttributes;
    }
}
