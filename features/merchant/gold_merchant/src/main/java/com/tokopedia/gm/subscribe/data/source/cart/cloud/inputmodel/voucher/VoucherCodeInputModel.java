package com.tokopedia.gm.subscribe.data.source.cart.cloud.inputmodel.voucher;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class VoucherCodeInputModel {
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("promocode")
    @Expose
    private String promocode;
    @SerializedName("os_type")
    private String osType;

    public static VoucherCodeInputModel buildInputModel(String voucherCode, Integer selectedProduct) {
        VoucherCodeInputModel inputModel = new VoucherCodeInputModel();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setProductId(selectedProduct);
        item.setQty(1);
        items.add(item);
        inputModel.setItems(items);
        inputModel.setPromocode(voucherCode);
        inputModel.setOsType("1");
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

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }
}
