package com.tokopedia.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.URLParser;
import com.tokopedia.core.database.manager.DbManagerImpl;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;

import java.util.List;

/**
 * Created by Nisie on 28/10/15.
 * Modified by Alifa
 */
public class DeepLinkChecker {

    public static final int BROWSE = 0;
    public static final int HOT = 1;
    public static final int CATALOG = 2;
    public static final int PRODUCT = 3;
    public static final int SHOP = 4;
    public static final int TOPPICKS = 5;

    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";

    public static int getDeepLinkType(String url) {
        List<String> linkSegment = getLinkSegment(url);
        CommonUtils.dumper("DEEPLINK " + linkSegment.toString());
        try {
            if (isBrowse(linkSegment))
                return BROWSE;
            else if (isHot(linkSegment))
                return HOT;
            else if (isCatalog(linkSegment))
                return CATALOG;
            else if (isProduct(linkSegment))
                return PRODUCT;
            else if (isShop(linkSegment))
                return SHOP;
            else if (isTopPicks(linkSegment))
                return TOPPICKS;
            else return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static List<String> getLinkSegment(String url) {
        return Uri.parse(url).getPathSegments();
    }

    private static boolean isBrowse(List<String> linkSegment) {
        return (linkSegment.get(0).equals("search") || linkSegment.get(0).equals("p")
                && !isHot(linkSegment)
                && !isCatalog(linkSegment)
                && !isTopPicks(linkSegment));
    }

    private static boolean isCatalog(List<String> linkSegment) {
        return (linkSegment.get(0).equals("catalog"));
    }

    private static boolean isHot(List<String> linkSegment) {
        return (linkSegment.get(0).equals("hot"));
    }

    private static boolean isTopPicks(List<String> linkSegment) {
        return (linkSegment.get(0).equals("toppicks"));
    }

    private static boolean isProduct(List<String> linkSegment) {
        return (linkSegment.size() == 2
                && !isBrowse(linkSegment)
                && !isHot(linkSegment)
                && !isCatalog(linkSegment)
                && !isTopPicks(linkSegment));
    }

    private static boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() == 1
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("reset.pl")
                && !linkSegment.get(0).equals("activation.pl"));
    }


    private static boolean isSearch(String url) {
        return (getLinkSegment(url).get(0).equals("search"));
    }

    private static String getQuery(String url, String q) {
        CommonUtils.dumper("DEEPLINK " + Uri.parse(url).getQueryParameter(q));
        return Uri.parse(url).getQueryParameter(q);
    }

    private static Integer getStType(String st) {
        switch (st) {
            case "shop":
                return 2;
            case "product":
                return 1;
            default:
                return null;
        }

    }

    private static boolean isCategory(String url) {
        return (getLinkSegment(url).get(0).equals("p"));
    }

    public static void openBrowse(String url, Context context) {
        Uri uriData = Uri.parse(url);
        List<String> linkSegment = uriData.getPathSegments();
        Bundle bundle = new Bundle();
        String departmentId = "0";
        String searchQuery = "";
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
        if (isSearch(url)) {
            departmentId = uriData.getQueryParameter("sc");
            searchQuery = uriData.getQueryParameter("q");
            source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
            bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
            bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);
        } else if (isCategory(url)) {
            String iden = linkSegment.get(1);
            for (int i = 2; i < linkSegment.size(); i++) {
                iden = iden + "_" + linkSegment.get(i);
            }
            CategoryDB dep =
                    DbManagerImpl.getInstance().getCategoryDb(iden);
            if (dep != null) {
                departmentId = dep.getDepartmentId() + "";
                bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
            }
            bundle.putInt(BrowseProductRouter.FRAGMENT_ID, BrowseProductRouter.VALUES_PRODUCT_FRAGMENT_ID);
            source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_DIRECTORY;
        }

        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.AD_SRC, TopAdsApi.SRC_HOTLIST);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);
        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, source);
        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private static boolean isHotBrowse(String url) {
        return (getLinkSegment(url).size() == 1 && !isHotAlias(url));
    }

    private static boolean isHotLink(String url) {
        return (getLinkSegment(url).size() == 2);
    }

    private static boolean isHotAlias(String url) {
        return (getQuery(url, "alk") != null);
    }

    public static void openHot(String url, Context context) {
        URLParser urlParser = new URLParser(url);
        Bundle bundle = new Bundle();
        bundle.putString(BrowseProductRouter.EXTRAS_DISCOVERY_ALIAS, urlParser.getHotAlias());
        bundle.putString(BrowseProductRouter.EXTRA_SOURCE, BrowseProductRouter.VALUES_DYNAMIC_FILTER_HOT_PRODUCT);
        Intent intent = BrowseProductRouter.getDefaultBrowseIntent(context);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public static void openCatalog(String url, Context context) {
        Bundle bundle = new Bundle();
//        bundle.putString("ctg_id", getLinkSegment(url).get(1));
//        Intent intent = new Intent(context, Catalog.class);
//        intent.putExtras(bundle);
        context.startActivity(DetailProductRouter.getCatalogDetailActivity(context, getLinkSegment(url).get(1)));
    }

    public static void openProduct(String url, Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("shop_domain", getLinkSegment(url).get(0));
        bundle.putString("product_key", getLinkSegment(url).get(1));
        bundle.putString("url", url);
        Intent intent = new Intent(context, ProductInfoActivity.class);
        intent.putExtras(bundle);
        intent.setData(Uri.parse(url) );
        context.startActivity(intent);
    }

    public static void openShop(String url, Context context) {
        Bundle bundle = ShopInfoActivity.createBundle("", getLinkSegment(url).get(0));
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
