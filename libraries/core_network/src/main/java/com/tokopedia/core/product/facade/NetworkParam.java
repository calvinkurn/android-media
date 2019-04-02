package com.tokopedia.core.product.facade;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Angga.Prasetiyo on 02/11/2015.
 */

@Deprecated
public class NetworkParam {
    private static final String TAG = NetworkParam.class.getSimpleName();

    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_ITEM_ID = "item_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_ADS_TYPE = "type";
    private static final String PARAM_DEVICE_ID = "device";
    private static final String PARAM_PRODUCT_KEY = "product_key";
    private static final String PARAM_SHOP_DOMAIN = "shop_domain";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_CATALOG_ID = "shop_id";
    private static final String PARAM_DEPARTMENT_ID = "department_id";
    private static final String PARAM_CONDITION = "condition";
    private static final String PARAM_ETALASE_ID = "etalase_id";
    private static final String PARAM_KEYWORD = "keyword";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_PER_PAGE = "per_page";
    private static final String PARAM_PICTURE_STATUS = "picture_status";
    private static final String PARAM_SORT = "sort";
    private static final String PARAM_PRODUCT_ETALASE_ID = "product_etalase_id";
    private static final String PARAM_PRODUCT_ETALASE_NAME = "product_etalase_name";
    private static final String PARAM_ID_FOR_OTHER_PRODUCT = "-id";
    private static final String VALUE_DEVICE = "android";
    private static final String PARAM_SOURCE = "source";
    private static final String VALUE_SNEAK_PEAK = "sneak_peak";


    public static Map<String, String> paramProductDetail(ProductPass productPass) {
        Map<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_PRODUCT_ID, productPass.getProductId());
        params.put(PARAM_PRODUCT_KEY, productPass.getProductKey());
        params.put(PARAM_SHOP_DOMAIN, productPass.getShopDomain());
        return params;
    }

    public static TKPDMapParam<String, String> paramProductDetailTest2(ProductPass productPass) {
        TKPDMapParam<String, String> params = new TKPDMapParam<>();
        params.put(PARAM_DEVICE_ID, VALUE_DEVICE);
        params.put(PARAM_PRODUCT_ID, productPass.getProductId());
        params.put(PARAM_PRODUCT_KEY, productPass.getProductKey());
        params.put(PARAM_SHOP_DOMAIN, productPass.getShopDomain());
        return params;
    }

    public static Map<String, String> paramFaveShop(String shopId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SHOP_ID, shopId);
        return params;
    }

    public static Map<String, String> paramToNewEtalase(int productId, String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, String.valueOf(productId));
        params.put(PARAM_PRODUCT_ETALASE_ID, "new");
        params.put(PARAM_PRODUCT_ETALASE_NAME, name);
        return params;
    }

    public static Map<String, String> paramToEtalase(int productId, Etalase etalase) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, String.valueOf(productId));
        params.put(PARAM_PRODUCT_ETALASE_ID, String.valueOf(etalase.getEtalaseId()));
        params.put(PARAM_PRODUCT_ETALASE_NAME, etalase.getEtalaseName());
        return params;
    }

    public static Map<String, String> paramCheckPermission(ProductDetailData data) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_CATALOG_ID, "");
        params.put(PARAM_CONDITION, "");
        params.put(PARAM_DEPARTMENT_ID, "");
        params.put(PARAM_ETALASE_ID, data.getInfo().getProductEtalaseId());
        params.put(PARAM_KEYWORD, "");
        params.put(PARAM_PAGE, "1");
        params.put(PARAM_PER_PAGE, "1");
        params.put(PARAM_PICTURE_STATUS, "");
        params.put(PARAM_SORT, "");
        return params;
    }

    public static Map<String, String> paramToWarehouse(int productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, String.valueOf(productId));
        return params;
    }

    public static Map<String, String> paramPromote(int productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, String.valueOf(productId));
        return params;
    }

    public static Map<String, String> paramAddWishList(Integer productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, String.valueOf(productId));
        return params;
    }

    public static Map<String, String> paramOtherProducts(ProductDetailData data) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SHOP_ID, String.valueOf(data.getShopInfo().getShopId()));
        params.put(PARAM_ID_FOR_OTHER_PRODUCT, String.valueOf(data.getInfo().getProductId()));
        return params;
    }

    public static Map<String, String> paramDownloadReportType(Integer productId) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PRODUCT_ID, productId + "");
        return params;
    }

    public static Map<String, String> paramProductTalk(String productId, String shopId) {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_PRODUCT_ID, productId);
        param.put("page", String.valueOf(1));
        param.put("per_page", String.valueOf(10));
        param.put("shop_domain", shopId);
        return param;
    }

    public static Map<String, String> paramTalkComment(String talkId, String shopId) {
        Map<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(1));
        param.put("talk_id", talkId);
        param.put("shop_id", shopId);
        return param;
    }

    public static Map<String, String> paramCheckAds(String shopId, String itemId, String userId, String adsType){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_SHOP_ID, shopId);
        params.put(PARAM_ITEM_ID, itemId);
        params.put(PARAM_USER_ID, userId);
        params.put(PARAM_ADS_TYPE, adsType);
        params.put(PARAM_DEVICE_ID, VALUE_DEVICE);
        return params;
    }

    public static Map<String, String> paramProductSneakPeakTalk(String productId, String shopId) {
        Map<String, String> param = paramProductTalk(productId, shopId);
        param.put(PARAM_PER_PAGE, String.valueOf(1));
        param.put(PARAM_SOURCE, VALUE_SNEAK_PEAK);
        return param;
    }

    public static Map<String, String> paramSneakPeakTalkComment(String talkId, String shopId) {
        Map<String, String> param = paramTalkComment(talkId, shopId);
        param.put(PARAM_PER_PAGE, String.valueOf(1));
        param.put(PARAM_SOURCE, VALUE_SNEAK_PEAK);
        return param;
    }
}
