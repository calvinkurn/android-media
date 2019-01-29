package com.tokopedia.topads.sdk.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.topads.sdk.domain.model.CpmData;
import com.tokopedia.topads.sdk.domain.model.Product;
import java.util.Map;

/**
 * Author errysuprayogi on 24,January,2019
 */
public class TopAdsGtmTracker {

    public static AnalyticTracker getTracker(Context context) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return null;
        }
        return ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
    }


    public static void eventHomeProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "homepage",
                    "eventAction", "product recommendation impression - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "varian", "none/other",
                                    "list", "/ - p2 - rekomendasi untuk anda - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHomeProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "homepage",
                    "eventAction", "product recommendation click - topads",
                    "eventLabel", 1 + "." + (position + 1) + " - topads",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/ - p2 - rekomendasi untuk anda - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "varian", "none/other",
                                            "list", "/ - p2 - rekomendasi untuk anda - topads",
                                            "position", position + 1))))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultProductView(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "search result",
                    "eventAction", "impression - topads product",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "varian", "none/other",
                                    "list", "/searchproduct - product - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoShopView(Context context, String tabName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "search result",
                    "eventAction", tabName+" - catalog impression - topads headline shop",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - "+tabName+" - catalog - topads headline shop",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoProductView(Context context, String tabName, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "search result",
                    "eventAction", tabName+" - catalog impression - topads headline product",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", product.getId(),
                                                    "name", "/search - "+tabName+" - catalog - topads headline product",
                                                    "creative", product.getUri(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoShopClick(Context context, String tabName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "search result",
                    "eventAction", tabName+" - click catalog - topads headline shop",
                    "eventLabel", cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - "+tabName+" - catalog - topads headline shop",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoProductClick(Context context, String tabName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "search result",
                    "eventAction", tabName+" - click catalog - topads headline product",
                    "eventLabel", cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - "+tabName+" - catalog - topads headline product",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultProductClick(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "search result",
                    "eventAction", "click - topads product",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/ - p2 - product - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf(
                                            "name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", product.getCategory().getId(),
                                            "varian", "none/other",
                                            "list", "/ - p2 - rekomendasi untuk anda - topads",
                                            "position", position + 1))))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventProductDetailProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "product detail page",
                    "eventAction", "impression - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf(
                                    "name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "category", product.getCategory().getId(),
                                    "varian", "none/other",
                                    "list", "/searchproduct - product - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventProductDetailProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "product detail page",
                    "eventAction", "click - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/productdetail - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf(
                                            "name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", product.getCategory().getId(),
                                            "varian", "none/other",
                                            "position", position + 1))))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistPromoView(Context context, String hotlistKey, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "hotlist page",
                    "eventAction", "topads headline shop impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/hot topads headline shop/"+hotlistKey+" - hotlist lainnya",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistShopPromoClick(Context context, String keyword, String hotlistKey, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "hotlist page",
                    "eventAction", "topads headline shop click",
                    "eventLabel", "keyword: "+keyword+" - applink: "+cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/hot topads headline shop/"+hotlistKey+" - hotlist lainnya",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistProductPromoClick(Context context, String keyword, String hotlistKey, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "hotlist page",
                    "eventAction", "topads headline product click",
                    "eventLabel", "keyword: "+keyword+" - applink: "+cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/hot topads headline product/"+hotlistKey+" - hotlist lainnya",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistProductView(Context context, String keyword, String hotlistKey, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "hotlist page",
                    "eventAction", "product list impression - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf(
                                    "name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "category", product.getCategory().getId(),
                                    "varian", "none/other",
                                    "list", "/hot/" + hotlistKey + " - topads",
                                    "position", position + 1),
                                    "attribution", ""))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistProductClick(Context context, String keyword, String hotlistKey, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "hotlist page",
                    "eventAction", "click product list - topads",
                    "eventLabel", keyword + " - applink:" + product.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/hot/" + hotlistKey + " - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf(
                                            "name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", product.getCategory().getId(),
                                            "varian", "none/other",
                                            "position", position + 1))))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCategoryProductView(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "category page",
                    "eventAction", "product list impression - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf(
                                    "name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "category", product.getCategory().getId(),
                                    "varian", "none/other",
                                    "list", "/category/" + product.getCategory().getId() + " - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCategoryProductClick(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "category page",
                    "eventAction", "click product list - topads",
                    "eventLabel", keyword + " - url:" + product.getUri(),
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/category/" + product.getCategory().getId() + " - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf(
                                            "name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", product.getCategory().getId(),
                                            "varian", "none/other",
                                            "position", position + 1),
                                            "attribution", "")))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCategoryPromoView(Context context, String categoryName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "category page",
                    "eventAction", "headline shop topads impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/category - "+categoryName+" - topads headline shop",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "promo_id", "none/other",
                                                    "promo_code", "none/other",
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCategoryPromoProductClick(Context context, String categoryName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "category page",
                    "eventAction", "headline product topads click",
                    "eventLabel", "keyword: "+categoryName+" url: "+cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/category - "+categoryName+" - topads headline product",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "promo_id", "none/other",
                                                    "promo_code", "none/other",
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCategoryPromoShopClick(Context context, String categoryName, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "category page",
                    "eventAction", "headline shop topads click",
                    "eventLabel", "keyword: "+categoryName+" url: "+cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/category - "+categoryName+" - topads headline shop",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "promo_id", "none/other",
                                                    "promo_code", "none/other",
                                                    "position", position))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventWishlistProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "wishlist page",
                    "eventAction", "product impression - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf(
                                    "name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "category", product.getCategory().getId(),
                                    "varian", "none/other",
                                    "list", "/wishlist - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventWishlistProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "wishlist page",
                    "eventAction", "click product - topads",
                    "eventLabel", (position + 1),
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/wishlist - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "category", product.getCategory().getId(),
                                            "varian", "none/other",
                                            "list", "/ - p2 - rekomendasi untuk anda - topads",
                                            "position", position + 1))))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }
}
