package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hangnadi on 9/14/17.
 */

public class Promotion {

    private Map<String, Object> actionField = new HashMap<>();
    private Map<String, Object> promotionImpression = new HashMap<>();

    public void setPromotionID(String promotionID) {
        this.promotionImpression.put("id", promotionID);
    }

    public void setPromotionName(String promotionName) {
        this.promotionImpression.put("name", promotionName);
    }

    public void setPromotionAlias(String promotionAlias) {
        this.promotionImpression.put("creative",
                promotionAlias.trim().replaceAll(" ", "-")
        );
    }

    public void setPromotionPosition(int promotionPosition) {
        this.promotionImpression.put("position", "slider_banner_" + promotionPosition);
    }

    public Map<String, Object> getPromotionImpressionEvent() {
        return promotionImpression;
    }
}
