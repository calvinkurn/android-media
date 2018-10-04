package com.tokopedia.topads.sdk.domain.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by errysuprayogi on 10/25/17.
 */

public class MerlinRecomendation {

    public static final String KEY_CONFIDENCE_SCORE = "confidence_score";
    public static final String KEY_PRODUCT_CATEGORY_ID = "product_category_id";

    private int confidenceScore;
    private List<ProductCategoryId> productCategoryId = new ArrayList<>();

    public MerlinRecomendation(JSONObject object) throws JSONException {
        if(!object.isNull(KEY_PRODUCT_CATEGORY_ID)) {
            JSONArray array = object.getJSONArray(KEY_PRODUCT_CATEGORY_ID);
            for (int i = 0; i < array.length(); i++) {
                productCategoryId.add(new ProductCategoryId(array.getJSONObject(i)));
            }

        }
        if(!object.isNull(KEY_CONFIDENCE_SCORE)) {
            setConfidenceScore(object.getInt(KEY_CONFIDENCE_SCORE));
        }
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public List<ProductCategoryId> getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(List<ProductCategoryId> productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

}
