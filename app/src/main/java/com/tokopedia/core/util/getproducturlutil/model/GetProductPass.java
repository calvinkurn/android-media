package com.tokopedia.core.util.getproducturlutil.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nisie on 6/2/16.
 */
public class GetProductPass {

//    private static final String PARAM_ETALASE_ID = "etalase_id";
//    private static final String PARAM_KEYWORD = "keyword";
//    private static final String PARAM_PAGE = "page";
//    private static final String PARAM_PER_PAGE = "per_page";
//    private static final String PARAM_ORDER_BY = "order_by";
//    private static final String PARAM_SHOP_DOMAIN = "shop_domain";
//    private static final String PARAM_SHOP_ID = "shop_id";
//    private static final String PARAM_WHOLESALE = "wholesale";

    private static final String PARAM_QUERY = "q";
    private static final String PARAM_CATEGORY = "sc";
    private static final String PARAM_SORT = "ob";
    private static final String PARAM_MIN_PRICE = "pmin";
    private static final String PARAM_MAX_PRICE = "pmax";
    private static final String PARAM_GOLD_SHOP = "fshop";
    private static final String PARAM_ROWS = "rows";
    private static final String PARAM_START = "start";
    private static final String PARAM_DEVICE = "device";
    private static final String PARAM_LOCATION = "floc";
    private static final String PARAM_SHOP_ID = "shop_id";
    private static final String PARAM_WHOLESALE = "wholesale";
    private static final String PARAM_CATALOG = "ctg_id";
    private static final String PARAM_ETALASE = "etalase";
    private static final String PARAM_SHIPPING = "shipping";
    private static final String PARAM_PREORDER = "preorder";
    private static final String PARAM_CONDITION = "condition";
    private static final String PARAM_SOURCE = "source";


    private static final String IS_GOLD = "2";
    private static final String NOT_GOLD = "1";
    private static final String DEVICE_ANDROID = "android";
    public static final String DEFAULT_ROWS = "10";
    private static final String SOURCE_SHOP_PRODUCT = "shop_product";

    private String query;
    private String category;
    private String sort;
    private String minPrice;
    private String maxPrice;
    private String goldShop;
    private String rows;
    private String start;
    private String device;
    private String shopId;
    private String location;
    private String wholesale;
    private String catalog;
    private String etalase;
    private String shipping;
    private String preorder;
    private String condition;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getGoldShop() {
        return goldShop;
    }

    public void setGoldShop(String goldShop) {
        this.goldShop = goldShop;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWholesale() {
        return wholesale;
    }

    public void setWholesale(String wholesale) {
        this.wholesale = wholesale;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEtalase() {
        return etalase;
    }

    public void setEtalase(String etalase) {
        this.etalase = etalase;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public String getPreorder() {
        return preorder;
    }

    public void setPreorder(String preorder) {
        this.preorder = preorder;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Map<String, String> getProductUrlParam() {
        Map<String, String> params = new HashMap<>();
        if (getCatalog() != null && !getCatalog().equals(""))
            params.put(PARAM_CATALOG, getCatalog());
        if (getCategory() != null && !getCategory().equals(""))
            params.put(PARAM_CATEGORY, getCategory());
        if (getCondition() != null && !getCondition().equals(""))
            params.put(PARAM_CONDITION, getCondition());
        params.put(PARAM_DEVICE, DEVICE_ANDROID);
        if (getEtalase() != null && !getEtalase().equals(""))
            params.put(PARAM_ETALASE, getEtalase());
        if (getGoldShop() != null && !getGoldShop().equals(""))
            params.put(PARAM_GOLD_SHOP, getGoldShop());
        if (getLocation() != null && !getLocation().equals(""))
            params.put(PARAM_LOCATION, getLocation());
        if (getMaxPrice() != null && !getMaxPrice().equals(""))
            params.put(PARAM_MAX_PRICE, getMaxPrice());
        if (getMinPrice() != null && !getMinPrice().equals(""))
            params.put(PARAM_MIN_PRICE, getMinPrice());
        if (getPreorder() != null && !getPreorder().equals(""))
            params.put(PARAM_PREORDER, getPreorder());
        if (getQuery() != null && !getQuery().equals(""))
            params.put(PARAM_QUERY, getQuery());
        params.put(PARAM_ROWS, DEFAULT_ROWS);
        if (getShipping() != null && !getShipping().equals(""))
            params.put(PARAM_SHIPPING, getShipping());
        if (getShopId() != null && !getShopId().equals(""))
            params.put(PARAM_SHOP_ID, getShopId());
        if (getSort() != null && !getSort().equals(""))
            params.put(PARAM_SORT, getSort());
        if (getStart() != null && !getStart().equals(""))
            params.put(PARAM_START, getStart());
        if (getWholesale() != null && !getWholesale().equals(""))
            params.put(PARAM_WHOLESALE, getWholesale());
            params.put(PARAM_SOURCE, SOURCE_SHOP_PRODUCT);
        return params;
    }
}
