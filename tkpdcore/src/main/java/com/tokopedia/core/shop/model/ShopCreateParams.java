package com.tokopedia.core.shop.model;

import com.tokopedia.core.shipping.model.openshopshipping.OpenShopData;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Toped18 on 5/23/2016.
 */
@Parcel
public class ShopCreateParams {

    String shopImgAvatarSrc = "";
    String shopDomain       = "";
    String shopShortDesc    = "";
    String shopName         = "";
    String shopTagLine      = "";
    Map<String,String> shippingData;
    OpenShopData openShopData;

    public OpenShopData getOpenShopData() {
        return openShopData;
    }

    public void setOpenShopData(OpenShopData openShopData) {
        this.openShopData = openShopData;
    }

    public String getShopImgAvatarSrc() {
        return shopImgAvatarSrc;
    }

    public void setShopImgAvatarSrc(String shopImgAvatarSrc) {
        this.shopImgAvatarSrc = shopImgAvatarSrc;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopShortDesc() {
        return shopShortDesc;
    }

    public void setShopShortDesc(String shopShortDesc) {
        this.shopShortDesc = shopShortDesc;
    }

    public String getShopTagLine() {
        return shopTagLine;
    }

    public void setShopTagLine(String shopTagLine) {
        this.shopTagLine = shopTagLine;
    }

    public Map<String, String> getShippingData() {
        return shippingData;
    }

    public void setShippingData(Map<String, String> shippingData) {
        this.shippingData = shippingData;
    }

    public Map<String, String> toHashMap() {
        HashMap <String, String> openShopValidationParam = new HashMap<String, String>();
        openShopValidationParam.put("shop_domain", shopDomain);
        openShopValidationParam.put("shop_name", shopName);
        openShopValidationParam.put("shop_short_desc", shopShortDesc);
        openShopValidationParam.put("shop_tag_line", shopTagLine);
        if(openShopData != null) {
            openShopValidationParam.putAll(openShopData.getOpenShopHashMap());
        }
        return openShopValidationParam;
    }

    public void saveFromMap(Map<String, String> shopParamData) {
        shopDomain = shopParamData.get("shop_domain");
        shopParamData.remove("shop_domain");

        shopName = shopParamData.get("shop_name");
        shopParamData.remove("shop_name");

        shopTagLine = shopParamData.get("shop_tag_line");
        shopParamData.remove("shop_tag_line");

        shopShortDesc = shopParamData.get("shop_short_desc");
        shopParamData.remove("shop_short_desc");

        shippingData = shopParamData;
    }
}
