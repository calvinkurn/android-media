package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.applink.internal.ApplinkConstInternal;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.UriUtil;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.discovery.DetailProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.loyaltytokopoint.ILoyaltyRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Nisie on 28/10/15.
 * Modified by Alifa
 */
public class DeepLinkChecker {

    public static final int OTHER = -1;
    public static final int BROWSE = 0;
    public static final int HOT = 1;
    public static final int CATALOG = 2;
    public static final int PRODUCT = 3;
    public static final int SHOP = 4;
    public static final int TOPPICKS = 5;
    public static final int HOT_LIST = 6;
    public static final int CATEGORY = 7;
    public static final int HOME = 8;
    public static final int PROMO = 9;
    public static final int ETALASE = 10;
    public static final int APPLINK = 11;
    public static final int INVOICE = 12;
    public static final int ACCOUNTS = 13;
    public static final int RECHARGE = 14;
    public static final int BLOG = 15;
    public static final int PELUANG = 16;
    public static final int DISCOVERY_PAGE = 17;
    public static final int FLIGHT = 18;
    public static final int REFERRAL = 19;
    public static final int TOKOPOINT = 20;
    public static final int GROUPCHAT = 21;
    public static final int SALE = 22;
    public static final int WALLET_OVO = 23;
    public static final int PLAY = 24;


    public static final String IS_DEEP_LINK_SEARCH = "IS_DEEP_LINK_SEARCH";
    private static final String FLIGHT_SEGMENT = "flight";
    private static final String KEY_PROMO = "promo";
    private static final String KEY_SALE = "sale";
    private static final String GROUPCHAT_SEGMENT = "groupchat";
    private static final String PLAY_SEGMENT = "play";
    private static final String MYBILLS = "mybills";

    private static final String APP_EXCLUDED_URL = "app_excluded_url";
    private static final String APP_EXCLUDED_HOST = "app_excluded_host";

    public static int getDeepLinkType(String url) {
        Uri uriData = Uri.parse(url);

        List<String> linkSegment = uriData.getPathSegments();
        CommonUtils.dumper("DEEPLINK " + linkSegment.toString());
        if (uriData.toString().contains("accounts.tokopedia.com"))
            return ACCOUNTS;
        else if (uriData.getScheme().equals(Constants.APPLINK_CUSTOMER_SCHEME))
            return APPLINK;

        try {
            if (isExcludedHostUrl(uriData))
                return OTHER;
            else if (isPlay(linkSegment))
                return PLAY;
            else if (isGroupChat(linkSegment))
                return GROUPCHAT;
            else if (isExcludedUrl(uriData))
                return OTHER;
            else if (isFlight(linkSegment))
                return FLIGHT;
            else if (isPromo(linkSegment))
                return PROMO;
            else if (isSale(linkSegment))
                return SALE;
            else if (isInvoice(linkSegment))
                return INVOICE;
            else if (isBlog(linkSegment))
                return BLOG;
            else if (isPeluang(linkSegment))
                return PELUANG;
            else if (isHome(url, linkSegment))
                return HOME;
            else if (isCategory(linkSegment))
                return CATEGORY;
            else if (isBrowse(linkSegment))
                return BROWSE;
            else if (isHot(linkSegment))
                return HOT;
            else if (isHotList(linkSegment))
                return HOT_LIST;
            else if (isCatalog(linkSegment))
                return CATALOG;
            else if (isDiscoveryPage(linkSegment))
                return DISCOVERY_PAGE;
            else if (isPulsa(linkSegment))
                return RECHARGE;
            else if (isTopPicks(linkSegment))
                return TOPPICKS;
            else if (isEtalase(linkSegment))
                return ETALASE;
            else if (isProduct(linkSegment))
                return PRODUCT;
            else if (isShop(linkSegment))
                return SHOP;
            else if (isReferral(linkSegment))
                return REFERRAL;
            else if (isTokoPoint(linkSegment))
                return TOKOPOINT;
            else if (isWalletOvo(linkSegment))
                return WALLET_OVO;
            else return OTHER;
        } catch (Exception e) {
            e.printStackTrace();
            return OTHER;
        }
    }

    private static boolean isMyBills(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equalsIgnoreCase(MYBILLS);
    }

    private static boolean isGroupChat(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equalsIgnoreCase(GROUPCHAT_SEGMENT);
    }

    private static boolean isPlay(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equalsIgnoreCase(PLAY_SEGMENT);
    }

    private static boolean isFlight(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equalsIgnoreCase(FLIGHT_SEGMENT);
    }

    public static List<String> getLinkSegment(String url) {
        return Uri.parse(url).getPathSegments();
    }

    private static boolean isBrowse(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("search");
    }

    private static boolean isCategory(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("p")
        );
    }

    private static boolean isCatalog(List<String> linkSegment) {
        return (linkSegment.get(0).equals("catalog"));
    }

    private static boolean isContent(List<String> linkSegment) {
        return (linkSegment.get(0).equals("content"));
    }

    private static boolean isPromo(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals(KEY_PROMO));
    }

    private static boolean isSale(List<String> linkSegment) {
        return linkSegment.size() > 0 && (linkSegment.get(0).equals(KEY_SALE));
    }

    private static boolean isHome(String url, List<String> linkSegment) {
        return (Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.WEB_DOMAIN).getHost())
                || Uri.parse(url).getHost().contains(Uri.parse(TkpdBaseURL.MOBILE_DOMAIN).getHost())) && linkSegment.size() == 0;
    }

    private static boolean isHot(List<String> linkSegment) {
        return (linkSegment.get(0).equals("hot") && linkSegment.size() > 1);
    }

    private static boolean isHotList(List<String> linkSegment) {
        return (linkSegment.get(0).equals("hot") && linkSegment.size() == 1);
    }

    private static boolean isTopPicks(List<String> linkSegment) {
        return (linkSegment.get(0).equals("toppicks"));
    }

    private static boolean isDiscoveryPage(List<String> linkSegment) {
        return (linkSegment.get(0).equals("b") && linkSegment.size() == 2 ||
                linkSegment.get(0).equals("discovery") && linkSegment.size() == 2);
    }

    private static boolean isEGold(List<String> linkSegment) {
        return linkSegment.get(0).equals("emas");
    }

    private static boolean isMutualFund(List<String> linkSegment) {
        return linkSegment.get(0).equals("reksa-dana");
    }

    public static String getDiscoveryPageId(String url) {
        if (getDeepLinkType(url) != DISCOVERY_PAGE) return "";
        Uri uriData = Uri.parse(url);
        List<String> linkSegment = uriData.getPathSegments();
        return linkSegment.get(1);
    }

    private static boolean isHelp(List<String> linkSegment) {
        return (linkSegment.get(0).equals("bantuan"));
    }

    private static boolean isEvents(List<String> linkSegment) {
        return (linkSegment.get(0).equals("events"));
    }

    private static boolean isProduct(List<String> linkSegment) {
        return (linkSegment.size() == 2
                && !isEvents(linkSegment)
                && !isHelp(linkSegment)
                && !isBrowse(linkSegment)
                && !isHot(linkSegment)
                && !isContent(linkSegment)
                && !isCatalog(linkSegment)
                && !isTopPicks(linkSegment))
                && !isTokoPoint(linkSegment)
                && !isEGold(linkSegment)
                && !isMutualFund(linkSegment)
                && !isWalletOvo(linkSegment)
                && !isKycTerms(linkSegment);
    }

    private static boolean isShop(List<String> linkSegment) {
        return (linkSegment.size() == 1
                && !linkSegment.get(0).equals("search")
                && !linkSegment.get(0).equals("hot")
                && !linkSegment.get(0).equals("about")
                && !linkSegment.get(0).equals("reset.pl")
                && !linkSegment.get(0).equals("activation.pl")
                && !linkSegment.get(0).equals("referral"))
                && !isTokoPoint(linkSegment)
                && !isEGold(linkSegment)
                && !isMutualFund(linkSegment)
                && !isMyBills(linkSegment)
                && !linkSegment.get(0).equals("contact-us");
    }

    private static boolean isSearch(String url) {
        return (getLinkSegment(url).get(0).equals("search"));
    }

    private static boolean isEtalase(List<String> linkSegment) {
        return (linkSegment.size() == 3 && linkSegment.get(1).equals("etalase"));
    }

    private static boolean isReferral(List<String> linkSegment) {
        return (linkSegment.get(0).equals("referral"));
    }

    private static boolean isTokoPoint(List<String> linkSegment) {
        return (linkSegment.get(0).equals("tokopoints"));
    }

    private static boolean isWalletOvo(List<String> linkSegment) {
        return (linkSegment.get(0).equals("ovo"));
    }

    private static boolean isKycTerms(List<String> linkSegment) {
        return (linkSegment.get(0).equals("terms")) && (linkSegment.get(1).equals("merchantkyc"));
    }

    public static String getQuery(String url, String q) {
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
        Bundle bundle = new Bundle();

        String departmentId = uriData.getQueryParameter("sc");
        String searchQuery = uriData.getQueryParameter("q");
        String source = BrowseProductRouter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;

        bundle.putBoolean(IS_DEEP_LINK_SEARCH, true);
        bundle.putString(BrowseProductRouter.DEPARTMENT_ID, departmentId);
        bundle.putString(BrowseProductRouter.EXTRAS_SEARCH_TERM, searchQuery);

        Intent intent;
        if (TextUtils.isEmpty(departmentId)) {
            intent = BrowseProductRouter.getSearchProductIntent(context);
            intent.putExtras(bundle);
        } else {
            intent = RouteManager.getIntentInternal(context,
                    UriUtil.buildUri(ApplinkConstInternal.Marketplace.DISCOVERY_CATEGORY_DETAIL,departmentId));
        }
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
        context.startActivity(
                BrowseProductRouter.getHotlistIntent(context, url)
        );
    }

    public static void openCatalog(String url, Context context) {
        context.startActivity(DetailProductRouter.getCatalogDetailActivity(context, getLinkSegment(url).get(1)));
    }

    public static void openCategory(String url, Context context) {
        String departmentId = getLinkSegment(url).get(1);
        Intent intent = RouteManager.getIntentInternal(context,
                UriUtil.buildUri(ApplinkConstInternal.Marketplace.DISCOVERY_CATEGORY_DETAIL, departmentId));
        context.startActivity(intent);
    }

    public static void openProduct(String url, Context context) {
        if (context != null) {
            Bundle bundle = new Bundle();
            if (getLinkSegment(url).size() > 1) {
                bundle.putString("shop_domain", getLinkSegment(url).get(0));
                bundle.putString("product_key", getLinkSegment(url).get(1));
            }
            bundle.putString("url", url);
            Intent intent = ProductDetailRouter.createInstanceProductDetailInfoActivity(context);
            intent.putExtras(bundle);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }

    public static void openShop(String url, Activity context) {
        Intent intent = ((TkpdCoreRouter) context.getApplication()).getShopPageIntentByDomain(context, getLinkSegment(url).get(0));
        context.startActivity(intent);
    }

    public static void openHomepage(Context context, int tab) {
        if (context != null &&
                context.getApplicationContext() != null &&
                context.getApplicationContext() instanceof TkpdCoreRouter) {
            Intent intent = ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
            intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, tab);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            context.startActivity(intent);
        }
    }

    public static void openShopWithParameter(String url, Context context, Bundle parameter) {
        if (MainApplication.getAppContext() instanceof TkpdCoreRouter) {
            Intent intent = ((TkpdCoreRouter) MainApplication.getAppContext()).getShopPageIntentByDomain(context, getLinkSegment(url).get(0));
            MainApplication.getAppContext().startActivity(intent);
        }
    }

    public static void openTokoPoint(Context context, String url) {
        if (context.getApplicationContext() instanceof ILoyaltyRouter) {
            ((ILoyaltyRouter) context.getApplicationContext()).openTokoPoint(context, url);
        }
    }

    private static boolean isExcludedUrl(Uri uriData) {
        RemoteConfig firebaseRemoteConfig = new FirebaseRemoteConfigImpl(MainApplication.getAppContext());
        String excludedUrl = firebaseRemoteConfig.getString(APP_EXCLUDED_URL);
        if (!TextUtils.isEmpty(excludedUrl)) {
            List<String> listExcludedString = Arrays.asList(excludedUrl.split(","));
            for (String excludedString : listExcludedString) {
                if (uriData.getPath().endsWith(excludedString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isExcludedHostUrl(Uri uriData) {
        RemoteConfig firebaseRemoteConfig = new FirebaseRemoteConfigImpl(MainApplication.getAppContext());
        String excludedHost = firebaseRemoteConfig.getString(APP_EXCLUDED_HOST);
        if (!TextUtils.isEmpty(excludedHost)) {
            List<String> listExcludedString = Arrays.asList(excludedHost.split(","));
            for (String excludedString : listExcludedString) {
                if (uriData.getPath().startsWith(excludedString)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isInvoice(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).startsWith("invoice.pl");
    }

    private static boolean isPulsa(List<String> linkSegment) {
        return linkSegment.size() == 1 && linkSegment.get(0).equals("pulsa");
    }

    private static boolean isBlog(List<String> linkSegment) {
        return linkSegment.size() > 0 && linkSegment.get(0).equals("blog");
    }

    private static boolean isPeluang(List<String> linkSegment) {
        return linkSegment.size() > 0 && (
                linkSegment.get(0).equals("peluang") || linkSegment.get(0).equals("peluang.pl")
        );
    }
}
