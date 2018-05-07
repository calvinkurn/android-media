package com.tokopedia.shop.common.util;

import java.util.List;

/**
 * Created by nathan on 2/25/18.
 */

public class WishListUtils {

    public static boolean isWishList(String productId, List<String> productWishList) {
        for (String productWishListId : productWishList) {
            if (productId.equalsIgnoreCase(productWishListId)) {
                return true;
            }
        }
        return false;
    }
}
