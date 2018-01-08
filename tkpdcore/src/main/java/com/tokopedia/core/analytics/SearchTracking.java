package com.tokopedia.core.analytics;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking extends TrackingUtils {

    public static void trackEventClickSearchResultProduct(String name,
                                                          String id,
                                                          String price,
                                                          int itemPosition,
                                                          String userId,
                                                          String eventLabel) {
        getGTMEngine().enhanceClickSearchResultProduct(createProductList(name, id, price, itemPosition, userId),
                eventLabel,
                ""
        );
    }

    private static Map<String, Object> createProductList(String name, String id, String price,
                                                         int itemPosition,
                                                         String userId) {
        com.tokopedia.core.analytics.model.Product product = new com.tokopedia.core.analytics.model.Product();
        product.setName(name);
        product.setId(id);
        product.setPrice(price);
        product.setBrand("");
        product.setCategoryId("");
        product.setCategoryName("");
        product.setVariant("");
        product.setList("");
        product.setPosition(itemPosition);
        product.setUserId(userId);

        return product.getProductAsDataLayerForSearchResultItemClick();
    }

    public static void eventImpressionSearchResultProduct(List<Object> list, String eventLabel) {
        getGTMEngine().enhanceImpressionSearchResultProduct(list, eventLabel);
    }
}
