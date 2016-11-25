package com.tokopedia.core.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.product.activity.ProductInfoActivity;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;

import java.util.List;

/**
 * Created by Nisie on 28/10/15.
 */
public class DeepLinkChecker {

    public static final int BROWSE = 0;
    public static final int HOT = 1;
    public static final int CATALOG = 2;
    public static final int PRODUCT = 3;
    public static final int SHOP = 4;

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
            else return -1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static List<String> getLinkSegment(String url) {
        return Uri.parse(url).getPathSegments();
    }

    private static boolean isBrowse(List<String> linkSegment) {
        return linkSegment.get(0).equals("search") || linkSegment.get(0).equals("p");
    }

    private static boolean isCatalog(List<String> linkSegment) {
        return (linkSegment.get(0).equals("catalog"));
    }

    private static boolean isHot(List<String> linkSegment) {
        return (linkSegment.get(0).equals("hot"));
    }

    private static boolean isProduct(List<String> linkSegment) {
        return (linkSegment.size() == 2
                && !isBrowse(linkSegment)
                && !isHot(linkSegment)
                && !isCatalog(linkSegment));
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
//        Bundle bundle = new Bundle();
//
//        if (isSearch(url)) {
//            CommonUtils.dumper("DEEPLINK is search");
//            bundle.putString("d_id", getQuery(url, "sc"));
//            bundle.putString("st", getQuery(url, "st"));
//            Integer stType = getStType(getQuery(url, "st"));
//            try {
//                bundle.putInt("vi", Integer.parseInt(getQuery(url, "vi")));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (stType != null) {
//                bundle.putInt("vi", stType);
//            }
//            bundle.putString("search_query", getQuery(url, "q"));
//            bundle.putInt("state", 5);
//        } else if (isCategory(url)) {
//            List<String> linkSegment = getLinkSegment(url);
//            String iden = linkSegment.get(1);
//            for (int i = 2; i < linkSegment.size(); i++) {
//                iden = iden + "_" + linkSegment.get(i);
//            }
//            Kategori dep =
//            new com.activeandroid.query.Select().from(Kategori.class)
//                    .where(Kategori.KATEGORI_NAMA_IDENTIFIER+" LIKE \'"+iden+"\'")
//                    .executeSingle();
//            String dep_id = dep.getDepartmentId()+"";
//            bundle.putString("d_id", dep_id);
//            bundle.putString("st", "product");
//            bundle.putInt("vi", 2);
//        }
//
//        Intent intent = new Intent(context, BrowseCategory.class);
//        intent.putExtras(bundle);
//        context.startActivity(intent);
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
//        Intent intent;
//        Bundle bundle = new Bundle();
//        if (isHotBrowse(url)) {
//            intent = new Intent(context, ParentIndexHome.class);
//            intent.putExtra("fragment",3);
//        } else if (isHotAlias(url)) {
//            bundle.putString("alias", getQuery(url, "alk"));
//            intent = new Intent(context, BrowseHotDetail.class);
//            intent.putExtras(bundle);
//        } else if (isHotLink(url)) {
//            bundle.putString("alias", getLinkSegment(url).get(1));
//            intent = new Intent(context, BrowseHotDetail.class);
//            intent.putExtras(bundle);
//        } else return;
//        context.startActivity(intent);
    }


    public static void openCatalog(String url, Context context) {
        Bundle bundle = new Bundle();
//        bundle.putString("ctg_id", getLinkSegment(url).get(1));
//        Intent intent = new Intent(context, Catalog.class);
//        intent.putExtras(bundle);
        context.startActivity(BrowseProductRouter.getCatalogDetailActivity(context, getLinkSegment(url).get(1)));
    }

    public static void openProduct(String url, Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("shop_domain", getLinkSegment(url).get(0));
        bundle.putString("product_key", getLinkSegment(url).get(1));
        bundle.putString("url", url);
        Intent intent = new Intent(context, ProductInfoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openShop(String url, Context context) {
        Bundle bundle = ShopInfoActivity.createBundle("", getLinkSegment(url).get(0));
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
