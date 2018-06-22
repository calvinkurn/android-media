package com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.checkout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmCheckoutInputModel {
    private static final int UNDEFINED_AUTO_EXTEND = -1;
    private static final String EMPTY_VOUCHER_CODE = "";
    private static final String ANDROID_DEVICE_TYPE = "1";
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("promocode")
    private String promocode;
    @SerializedName("device_id")
    private String deviceId;

    public static GmCheckoutInputModel getBodyModel(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        GmCheckoutInputModel inputModel = new GmCheckoutInputModel();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setProductId(selectedProduct);
        item.setQty(1);
        if (autoExtendSelectedProduct != UNDEFINED_AUTO_EXTEND) {
            Map<String, String> map = new HashMap<>();
            map.put(Item.AUTOEXTEND, String.valueOf(autoExtendSelectedProduct));
            item.setConfiguration(map);
        }
        items.add(item);
        inputModel.setItems(items);
        if (!voucherCode.equals(EMPTY_VOUCHER_CODE)) {
            inputModel.setPromocode(voucherCode);
        }
        inputModel.setDeviceId(ANDROID_DEVICE_TYPE);
        return inputModel;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
