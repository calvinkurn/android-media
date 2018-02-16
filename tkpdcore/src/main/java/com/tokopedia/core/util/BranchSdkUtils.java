package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.CommerceEvent;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.LinkProperties;

/**
 * Created by ashwanityagi on 04/10/17.
 */

public class BranchSdkUtils {
    private static final String BRANCH_ANDROID_DEEPLINK_PATH_KEY = "$android_deeplink_path";
    private static final String BRANCH_IOS_DEEPLINK_PATH_KEY = "$ios_deeplink_path";
    private static final String BRANCH_DESKTOP_URL_KEY = "$desktop_url";
    private static final String CAMPAIGN_NAME = "Android App";
    private static final String ORDERID_KEY = "orderId";
    private static final String PRODUCTTYPE_KEY = "productType";
    private static final String USERID_KEY = "userId";
    public static final String PRODUCTTYPE_DIGITAL = "digital";
    public static final String PRODUCTTYPE_MARKETPLACE = "marketplace";

    private static BranchUniversalObject createBranchUniversalObject(ShareData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(data.getType())
                .setTitle(data.getName())
                .setContentDescription(data.getDescription())
                .setContentImageUrl(data.getImgUri())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
        return branchUniversalObject;
    }

    public static void generateBranchLink(final ShareData data, final Activity activity, final GenerateShareContents ShareContentsCreateListener) {

        if (isBranchUrlActivated(activity, data.getType())) {
            BranchUniversalObject branchUniversalObject = createBranchUniversalObject(data);
            LinkProperties linkProperties = createLinkProperties(data, data.getSource(), activity);
            branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {

                    if (error == null) {
                        ShareContentsCreateListener.onCreateShareContents(data.getTextContentForBranch(url), url, url);
                    } else {
                        ShareContentsCreateListener.onCreateShareContents(data.getTextContent(activity), data.renderShareUri(), url);

                    }
                }
            });
        } else {
            ShareContentsCreateListener.onCreateShareContents(data.getTextContent(activity), data.renderShareUri(), data.renderShareUri());

        }
    }

    private static LinkProperties createLinkProperties(ShareData data, String channel, Activity activity) {
        LinkProperties linkProperties = new LinkProperties();
        String deeplinkPath;
        String desktopUrl = null;
        if (ShareData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.PRODUCT_INFO, data.getId());
        } else if (isappShowReferralButtonActivated(activity) && ShareData.APP_SHARE_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.REFERRAL_WELCOME, data.getId());
            deeplinkPath = deeplinkPath.replaceFirst("\\{.*?\\} ?", SessionHandler.getLoginName(activity) == null ? "" : SessionHandler.getLoginName(activity));
        } else if (ShareData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.SHOP, data.getId());//"shop/" + data.getId();
        } else if (ShareData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else if (ShareData.CATALOG_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_CATALOG, data.getId());
        } else {
            deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        }

        if (desktopUrl == null) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri());

        }

        linkProperties.setCampaign(CAMPAIGN_NAME);
        linkProperties.setChannel(channel);
        linkProperties.setFeature(data.getType());
        // linkProperties.addControlParameter(URI_REDIRECT_MODE_KEY, URI_REDIRECT_MODE_VALUE);
        linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        return linkProperties;
    }

    private static boolean isBranchUrlActivated(Activity activity, String type) {
        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(type)) {
            return true;
        } else {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
            return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true);
        }
    }

    private static String getApplinkPath(String url, String id) {
        if (url.contains(Constants.Schemes.APPLINKS + "://")) {
            url = url.replace(Constants.Schemes.APPLINKS + "://", "");
            url = url.replaceFirst("\\{.*?\\} ?", id == null ? "" : id);
        } else if (url.contains(TkpdBaseURL.WEB_DOMAIN)) {
            url = url.replace(TkpdBaseURL.WEB_DOMAIN, "");
        } else if (url.contains(TkpdBaseURL.MOBILE_DOMAIN)) {
            url = url.replace(TkpdBaseURL.MOBILE_DOMAIN, "");
        }
        return url;
    }

    public static void sendCommerceEvent(ArrayList<Product> locaProducts, String revenue, String totalShipping) {
        try {
            if (Branch.getInstance() != null && revenue != null && locaProducts != null) {
                List<io.branch.referral.util.Product> branchProductList = new ArrayList<>();
                for (com.tokopedia.core.analytics.model.Product locaProduct : locaProducts) {
                    io.branch.referral.util.Product product = new io.branch.referral.util.Product();
                    product.setSku(locaProduct.getId());
                    product.setName(locaProduct.getName());
                    product.setPrice(convertStringToDouble(locaProduct.getPrice()));
                    branchProductList.add(product);
                }

                CommerceEvent commerceEvent = new CommerceEvent();
                commerceEvent.setRevenue(convertStringToDouble(revenue));
                commerceEvent.setCurrencyType(CurrencyType.IDR);
                commerceEvent.setShipping(convertStringToDouble("" + totalShipping));
                commerceEvent.setProducts(branchProductList);
                CommonUtils.dumper("Revenue" + " old" + revenue + "  "+ totalShipping+ " "+ commerceEvent.toString());
              //  Branch.getInstance().sendCommerceEvent(commerceEvent, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void sendCommerceEvent(Purchase purchase, String productType) {
        try {
            if (Branch.getInstance() != null && purchase != null && purchase.getListProduct() != null) {

                List<io.branch.referral.util.Product> branchProductList = new ArrayList<>();

                for (Object objProduct : purchase.getListProduct()) {
                    Map<String, Object> product = (Map<String, Object>) objProduct;
                    io.branch.referral.util.Product branchProduct = new io.branch.referral.util.Product();

                    branchProduct.setSku("" + product.get(com.tokopedia.core.analytics.nishikino.model.Product.KEY_ID));
                    branchProduct.setName("" + product.get(com.tokopedia.core.analytics.nishikino.model.Product.KEY_NAME));
                    branchProduct.setPrice(convertStringToDouble(""+product.get(com.tokopedia.core.analytics.nishikino.model.Product.KEY_PRICE)));
                    branchProductList.add(branchProduct);

                }
                CommerceEvent commerceEvent = new CommerceEvent();
                commerceEvent.setTransactionID(purchase.getPaymentId());
                commerceEvent.setRevenue(convertStringToDouble("" + purchase.getRevenue()));
                commerceEvent.setCurrencyType(CurrencyType.IDR);
                commerceEvent.setShipping(convertStringToDouble("" + purchase.getShipping()));
                commerceEvent.setProducts(branchProductList);

                JSONObject metadata=new JSONObject();
                metadata.put(ORDERID_KEY,purchase.getTransactionID());
                metadata.put(PRODUCTTYPE_KEY,productType);
                metadata.put(USERID_KEY,purchase.getUserId());

                Branch.getInstance().sendCommerceEvent(commerceEvent, metadata, null);
                CommonUtils.dumper("Revenue"+ purchase.getRevenue()+" n " + purchase.getShipping() +"   " + commerceEvent.toString() );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Set userId to Branch.io sdk, userId, 127 chars or less
    public static void sendIdentityEvent(String userId) {
        if (Branch.getInstance() != null) {
            Branch.getInstance().setIdentity(userId);
        }
    }

    public static void sendLogoutEvent() {
        if (Branch.getInstance() != null) {
            Branch.getInstance().logout();
        }
    }

    public static void sendLoginEvent(String email, String phone) {
        if (Branch.getInstance() != null) {
            JSONObject metadata = new JSONObject();
            try {
                metadata.put(AppEventTracking.Branch.EMAIL, email);
                metadata.put(AppEventTracking.Branch.PHONE, normalizePhoneNumber(phone));
            } catch (JSONException e) {
            }
            Branch.getInstance().userCompletedAction(AppEventTracking.EventBranch.EVENT_LOGIN, metadata);
        }
    }

    public static void sendRegisterEvent(String email, String phone) {
        if (Branch.getInstance() != null) {
            JSONObject metadata = new JSONObject();
            try {
                metadata.put(AppEventTracking.Branch.EMAIL, email);
                metadata.put(AppEventTracking.Branch.PHONE, normalizePhoneNumber(phone));
            } catch (JSONException e) {
            }
            Branch.getInstance().userCompletedAction(AppEventTracking.EventBranch.EVENT_REGISTER, metadata);
        }
    }

    private static double convertStringToDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Boolean isappShowReferralButtonActivated(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    private static String normalizePhoneNumber(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum))
            return phoneNum.replaceFirst("^0(?!$)", "62");
        else
            return "";
    }

    public interface GenerateShareContents {
        void onCreateShareContents(String shareContents, String shareUri, String branchUrl);
    }
}