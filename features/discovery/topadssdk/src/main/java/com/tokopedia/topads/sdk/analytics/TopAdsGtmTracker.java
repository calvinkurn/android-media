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
                    "eventAction", "homepage - product impression - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "category", "none/other",
                                    "varian", "none/other",
                                    "list", "/homepage - product topads - product upload",
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
                    "eventAction", "homepage - product click - topads",
                    "eventLabel", 1 + "." + (position + 1) + " - topads",
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/homepage - product topads - product upload"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                            "id", product.getId(),
                                            "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                            "brand", "none/other",
                                            "varian", "none/other",
                                            "category", product.getCategory().getId(),
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
                    "eventAction", "impression - product - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf("currencyCode", "IDR",
                            "impressions", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
                                    "id", product.getId(),
                                    "price", product.getPriceFormat().replaceAll("[^0-9]", ""),
                                    "brand", "none/other",
                                    "varian", "none/other",
                                    "category", product.getCategory().getId(),
                                    "list", "/searchproduct - topads  productlist",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoView(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "search result",
                    "eventAction", "topads headline impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - headline",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoShopClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "search result",
                    "eventAction", "topads headline shop",
                    "eventLabel", cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - headline shop",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventSearchResultPromoProductClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "search result",
                    "eventAction", "topads headline product",
                    "eventLabel", cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/search - headline product'",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
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
                    "eventAction", "click - product - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/searchproduct - topads  productlist"),
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

    public static void eventProductDetailProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "product detail page",
                    "eventAction", "impression product - topads",
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
                                    "list", "/productdetail - topads",
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
                    "eventAction", "click product - topads",
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

    public static void eventProductDetailPromoView(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "product detail page",
                    "eventAction", "topads headline impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/product detail - topads headline",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
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
                    "eventAction", "topads headline impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/hot topads headline/"+hotlistKey+" - hotlist lainnya",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventProductDetailShopPromoClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "product detail page",
                    "eventAction", "topads headline shop click",
                    "eventLabel", cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/product detail - topads headline shop",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventProductDetailProductPromoClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "product detail page",
                    "eventAction", "topads headline product click",
                    "eventLabel", cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/product detail page - topads headline product",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
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
                    "eventLabel", cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/hot topads headline shop/"+hotlistKey+" - hotlist lainnya",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
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
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventIntermediaryShopPromoClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "intermediary page",
                    "eventAction", "topads headline shop click",
                    "eventLabel", cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "intermediary - topads headline shop",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventIntermediaryProductPromoClick(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "intermediary page",
                    "eventAction", "topads headline product click",
                    "eventLabel", cpm.getApplinks(),
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "intermediary - topads headline product",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventIntermediaryPromoView(Context context, CpmData cpm, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "intermediary page",
                    "eventAction", "topads headline impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", "/intermediary - headline",
                                                    "creative", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventIntermediaryProductView(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "intermediary page",
                    "eventAction", "impression - product - topads",
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
                                    "list", "/intermediary page - topads - promoted",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventIntermediaryProductClick(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "intermediary page",
                    "eventAction", "click - product - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/intermediary page - topads - promoted"),
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

    public static void eventHotlistProductView(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "hotlist page",
                    "eventAction", "impression - product - topads",
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
                                    "list", "/hotlist - topads - promoted",
                                    "position", position + 1),
                                    "attribution", ""))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventHotlistProductClick(Context context, String keyword, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "hotlist page",
                    "eventAction", "click - product - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/hotlist - topads - promoted"),
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
                    "eventAction", "impression product - topads",
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
                                    "list", "/category/"+product.getCategory().getId()+" - topads",
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
                    "eventAction", "click product - topads",
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
                    "eventAction", "topads headline impression",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "promoView", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", categoryName+" - topads headline - subcategory",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "position", position + 1))
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
                    "eventAction", "topads headline product click",
                    "eventLabel", "keyword: "+categoryName+" url: "+cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", categoryName+" - topads headline product - subcategory",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "position", position + 1))
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
                    "eventAction", "topads headline shop click",
                    "eventLabel", "keyword: "+categoryName+" url: "+cpm.getRedirect(),
                    "ecommerce", DataLayer.mapOf(
                            "promoClick", DataLayer.mapOf(
                                    "promotions", DataLayer.listOf(
                                            DataLayer.mapOf(
                                                    "id", cpm.getId(),
                                                    "name", categoryName+" - topads headline shop - subcategory",
                                                    "creative", cpm.getCpm().getName(),
                                                    "creative_url", cpm.getRedirect(),
                                                    "position", position + 1))
                            ))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventWishlistProductView(Context context, Product product, String keyword, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "wishlist page",
                    "eventAction", "product impression - topads",
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
                                    "list", "/wishlist - product topads - product upload",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventWishlistEmptyProductView(Context context, Product product, String keyword, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "wishlist page",
                    "eventAction", "empty wishlist - product impression - topads",
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
                                    "list", "/wishlist - product topads - product upload",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventWishlistProductClick(Context context, Product product, String keyword, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "wishlist page",
                    "eventAction", "product click - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/wishlist - product topads - product upload"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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

    public static void eventWishlistEmptyProductClick(Context context, Product product, String keyword, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "wishlist page",
                    "eventAction", "empty wishlist - product click - topads",
                    "eventLabel", keyword,
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/empty wishlist - product topads - product upload"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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

    public static void eventCartEmptyProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "cart",
                    "eventAction", "empty cart - product impression topads",
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
                                    "list", "/empty cart - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCartEmptyProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "cart",
                    "eventAction", "empty cart - product click topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/empty cart - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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

    public static void eventCartProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "cart",
                    "eventAction", "product impression topads",
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
                                    "list", "/cart - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCartProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "cart",
                    "eventAction", "cart - product click topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/cart - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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

    public static void eventCartAfterProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "cart",
                    "eventAction", "after_cart - product impression topads",
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
                                    "list", "/cart - topads",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventCartAfterProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "after_cart",
                    "eventAction", "after_cart - product click topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/cart - topads"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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

    public static void eventRecentViewProductView(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productView",
                    "eventCategory", "recent view",
                    "eventAction", "impression - product - topads",
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
                                    "list", "/recent view - topads - promoted",
                                    "position", position + 1)))
            );
            tracker.sendEnhancedEcommerce(map);
        }
    }

    public static void eventRecentViewProductClick(Context context, Product product, int position) {
        AnalyticTracker tracker = getTracker(context);
        if (tracker != null) {
            Map<String, Object> map = DataLayer.mapOf(
                    "event", "productClick",
                    "eventCategory", "recent view",
                    "eventAction", "click - product - topads",
                    "eventLabel", "",
                    "ecommerce", DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "click", DataLayer.mapOf(
                                    "actionField", DataLayer.mapOf("list", "/recent view - topads - promoted"),
                                    "product", DataLayer.listOf(DataLayer.mapOf("name", product.getName(),
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
}
