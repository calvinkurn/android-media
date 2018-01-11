package com.tokopedia.core.analytics;

import com.tkpd.library.utils.CurrencyFormatHelper;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class SearchTracking extends TrackingUtils {

    public static final String ACTION_FIELD = "/search result - product 2 - product list";

    public static void trackEventClickSearchResultProduct(String name,
                                                          String id,
                                                          String price,
                                                          int itemPosition,
                                                          String userId,
                                                          String eventLabel) {
        getGTMEngine().enhanceClickSearchResultProduct(createProductList(name, id, price, itemPosition, userId),
                eventLabel,
                ACTION_FIELD
        );
    }

    private static Map<String, Object> createProductList(String name, String id, String price,
                                                         int itemPosition,
                                                         String userId) {
        com.tokopedia.core.analytics.model.Product product = new com.tokopedia.core.analytics.model.Product();
        product.setName(name);
        product.setId(id);
        product.setPrice(Integer.toString(CurrencyFormatHelper.convertRupiahToInt(price)));
        product.setBrand("");
        product.setCategoryId("");
        product.setCategoryName("");
        product.setVariant("");
        product.setList(ACTION_FIELD);
        product.setPosition(itemPosition);
        product.setUserId(userId);

        return product.getProductAsDataLayerForSearchResultItemClick();
    }

    public static void eventImpressionSearchResultProduct(List<Object> list, String eventLabel) {
        getGTMEngine().enhanceImpressionSearchResultProduct(list, eventLabel);
    }
}
