package com.tokopedia.similarsearch.analytics;

/**
 * Created by sandeepgoyal on 03/01/18.
 */

public class SimilarSearchAppEventTracking {
    interface Event {
        String GenericProductView = "productView";
        String GenericProductClick = "productClick";
        String GenericViewSearchResult = "viewSearchResult";
        String CLICK_WISHLIST = "clickWishlist";
    }

    interface Category {
        String SIMILAR_PRODUCT = "similar product";
    }

    interface Action {
        String EventImpressionProduct = "impression - product";
        String EventClickSimilarProduct = "click - similar product";
        String EventNoSimilarProduct = "no similar product";
        String ADD_WISHLIST = "add wishlist";
        String REMOVE_WISHLIST = "remove wishlist";
        String MODULE = "module";
        String LOGIN = "login";
        String NON_LOGIN = "nonlogin";
    }

    interface Label {
        String LabelProductIDTitle = "product id: %s";
        String LabelScreeName = "%s";
        String LableOriginProductId = "origin product id: %s - " + LabelScreeName;
        String TOPADS = "topads";
        String GENERAL = "general";
    }
}
